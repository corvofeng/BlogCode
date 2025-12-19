/*
Usage examples (place in a Jenkinsfile or shared-library step):

// 1) Minimal - rely on environment variables `GIT_REPO`, `CHANGE_ID`, `GIT_TOKEN`:
//    addPrComment(message: 'Build finished successfully')

// 2) Explicit repository, PR and token:
//    addPrComment(repo: 'owner/repo', pr: '123', token: env.GIT_TOKEN, message: 'LGTM!')

// 3) Use `messageFile` to read a file (useful for long reports):
//    addPrComment(repo: 'owner/repo', pr: '123', token: env.GIT_TOKEN, messageFile: 'build/report.txt')

// Notes:
// - `repo` should be in the form 'owner/repo'.
// - `token` must be a GitHub personal access token with `repo` scope (or appropriate scopes for the target repo).
// - `pr` may be the pull request number (string or integer). If running in a multibranch PR job, `CHANGE_ID` can be used.
// - This implementation always sends the JSON payload by writing a temporary payload file
//   and posting it with `curl --data-binary @<file>` to avoid quoting/encoding issues.
*/

/* groovylint-disable MethodReturnTypeRequired */
/* groovylint-disable MethodParameterTypeRequired */
/* groovylint-disable-next-line NoDef */
def call(Map config = [:]) {
    def repo = config.repo ?: env.GIT_REPO
    def pr = config.pr ?: env.CHANGE_ID
    def token = config.token ?: env.GIT_TOKEN
    def message = config.message ?: config.comment ?: ''

    if (!repo) {
        error 'Missing `repo` (owner/repo). Provide config.repo or env.GIT_REPO.'
    }
    if (!pr) {
        error 'Missing `pr` number. Provide config.pr or run in a multibranch PR context (CHANGE_ID).'
    }
    if (!token) {
        error 'Missing `token`. Provide config.token or env.GIT_TOKEN.'
    }
    if (!message) {
        error 'Missing `message`. Provide config.message or config.comment.'
    }

    // Always use a temporary payload file and post with --data-binary to avoid
    // shell quoting problems for large or complex JSON bodies.
    def uuid = java.util.UUID.randomUUID().toString().replaceAll('-', '')
    def payloadPath = "payload-${env.BUILD_ID ?: 'local'}-${uuid}.json"

    sh """
    cat ${message}
    echo '{
        "body": ${groovy.json.JsonOutput.toJson(message)}
    }' > ${payloadPath}
    }
    """

    withCredentials([string(credentialsId: token, variable: 'GH_TOKEN')]) {
        def url = "https://api.github.com/repos/${repo}/issues/${pr}/comments"
        def authHeader = "Authorization: token ${GH_TOKEN}"
        try {
            def resp = sh(script: "curl -s -X POST -H \"${authHeader}\" -H \"Content-Type: application/json\" --data-binary @${payloadPath} '${url}'", returnStdout: true).trim()
            echo "PR comment response: ${resp}"
            return resp
        } catch (err) {
            error "Failed to post PR comment: ${err}"
        } finally {
            // best-effort cleanup of the temporary file
            try {
                sh(script: "rm -f ${payloadPath}", returnStdout: false)
            } catch (_e) {
                echo "Warning: failed to remove temporary payload file ${payloadPath}: ${_e}"
            }
        }
    }
}
