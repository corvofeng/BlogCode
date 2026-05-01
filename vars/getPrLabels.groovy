/*
Usage examples (place in a Jenkinsfile or shared-library step):

// 1) Use webhook env labels first, then fallback to GitHub API:
//    def info = getPrLabels(
//      repo: 'owner/repo',
//      changeId: env.CHANGE_ID,
//      tokenCredentialId: 'github-token-id',
//    )
//    echo "labels=${info.labels}, source=${info.source}"
//
// 2) Use only webhook env labels (disable API fallback):
//    def info = getPrLabels(fetchFromApi: false)
//
// Returns:
// - labels: List<String>
// - source: 'env:<ENV_KEY>' | 'github-api' | ''
*/

/* groovylint-disable MethodReturnTypeRequired */
/* groovylint-disable MethodParameterTypeRequired */
/* groovylint-disable-next-line NoDef */
def call(Map config = [:]) {
    def repo = (config.repo ?: env.GIT_REPO ?: '').toString().trim()
    def changeId = (config.changeId ?: env.CHANGE_ID ?: '').toString().trim()
    def tokenCredentialId = (config.tokenCredentialId ?: '').toString().trim()
    def fetchFromApi = (config.fetchFromApi == null) ? true : config.fetchFromApi

    List<String> labelEnvCandidates = (config.labelEnvCandidates ?: [
        'CHANGE_LABELS',
        'ghprbPullLabels',
        'GITHUB_PR_LABELS',
        'PR_LABELS',
        'pull_request_labels',
    ]) as List<String>

    List<String> labels = []
    String source = ''

    def normalizeLabels = { String raw ->
        raw
            .replace('[', '')
            .replace(']', '')
            .replace('"', '')
            .replace("'", '')
            .split(/[\s,]+/)
            .collect { it.trim() }
            .findAll { it }
    }

    for (String envKey : labelEnvCandidates) {
        String raw = env."${envKey}"?.trim()
        if (!raw) {
            continue
        }

        labels = normalizeLabels(raw)
        source = "env:${envKey}"
        break
    }

    if (!labels && fetchFromApi && repo && changeId && tokenCredentialId) {
        withCredentials([string(credentialsId: tokenCredentialId, variable: 'GH_TOKEN')]) {
            String labelsRaw = sh(
                returnStdout: true,
                script: """
                    set -euo pipefail

                    api_url="https://api.github.com/repos/${repo}/issues/${changeId}/labels"
                    gh_labels_api() {
                        body_file="`mktemp`"
                        http_code="`curl -sSL -o "\$body_file" -w '%{http_code}' -H 'Accept: application/vnd.github+json' -H "Authorization: Bearer \$GH_TOKEN" "\$api_url"`"

                        echo "[getPrLabels] GET \$api_url -> HTTP \$http_code" >&2
                        if [ "\$http_code" != "200" ]; then
                            echo "[getPrLabels] GitHub API returned non-200 response; response preview (first 40 lines):" >&2
                            sed -n '1,40p' "\$body_file" >&2 || true
                            rm -f "\$body_file"
                            return 1
                        fi

                        cat "\$body_file"
                        rm -f "\$body_file"
                    }

                    if command -v jq >/dev/null 2>&1; then
                        gh_labels_api | jq -r '.[].name // empty'
                    else
                        gh_labels_api | grep -o '"name":"[^"]*"' | cut -d '"' -f 4 || true
                    fi
                """.stripIndent().trim()
            )

            labels = labelsRaw ? labelsRaw.readLines() : []
            source = 'github-api'
        }
    }

    return [
        labels: labels,
        source: source,
    ]
}
