/*
Usage examples (place in a Jenkinsfile or shared-library step):

// 1) Fetch labels directly from GitHub API:
//    def info = getPrLabels(
//      repo: 'owner/repo',
//      changeId: env.CHANGE_ID,
//      tokenCredentialId: 'github-token-id',
//    )
//    echo "labels=${info.labels}, source=${info.source}"
//
// Returns:
// - labels: List<String>
// - source: 'github-api' | ''
*/

/* groovylint-disable MethodReturnTypeRequired */
/* groovylint-disable MethodParameterTypeRequired */
/* groovylint-disable-next-line NoDef */
def call(Map config = [:]) {
    def repo = (config.repo ?: env.GIT_REPO ?: '').toString().trim()
    def changeId = (config.changeId ?: env.CHANGE_ID ?: '').toString().trim()
    def tokenCredentialId = (config.tokenCredentialId ?: '').toString().trim()

    if (!repo || !changeId || !tokenCredentialId) {
        return [
            labels: [],
            source: '',
        ]
    }

    List<String> labels = []
    String source = ''

    withCredentials([string(credentialsId: tokenCredentialId, variable: 'GH_TOKEN')]) {
        def url = "https://api.github.com/repos/${repo}/issues/${changeId}/labels"
        def authHeader = 'Authorization: Bearer $GH_TOKEN'
        def acceptHeader = 'Accept: application/vnd.github+json'

        echo "[getPrLabels] request: curl -sSL -H '${acceptHeader}' -H 'Authorization: Bearer ***' '${url}'"

        String curlOutput = sh(
            returnStdout: true,
            script: """
                set -euo pipefail
                curl -sSL -w '\n%{http_code}' -H "${acceptHeader}" -H "${authHeader}" '${url}'
            """.stripIndent().trim()
        ).trim()

        List<String> outputLines = curlOutput.readLines()
        String httpCode = outputLines ? outputLines[-1] : ''
        String labelsJson = outputLines ? outputLines.dropRight(1).join('\n') : '[]'

        echo "[getPrLabels] GET ${url} -> HTTP ${httpCode}"
        echo "[getPrLabels] response: ${labelsJson}"
        if (httpCode != '200') {
            echo '[getPrLabels] GitHub API returned non-200 response'
            error "Failed to fetch labels from GitHub API. HTTP ${httpCode}"
        }

        List parsedList = (new groovy.json.JsonSlurperClassic().parseText(labelsJson ?: '[]') ?: []) as List
        labels = parsedList
            .collect { item -> (item?.name ?: '').toString().trim() }
            .findAll { name -> name }
        source = 'github-api'
    }

    return [
        labels: labels,
        source: source,
    ]
}
