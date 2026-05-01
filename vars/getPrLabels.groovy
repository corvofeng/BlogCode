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
        String labelsRaw = sh(
            returnStdout: true,
            script: """
                set -euo pipefail

                api_url="https://api.github.com/repos/${repo}/issues/${changeId}/labels"
                gh_labels_api() {
                    body_file="`mktemp`"
                    debug_curl="curl -sSL -H 'Accept: application/vnd.github+json' -H 'Authorization: Bearer ***' '\$api_url'"
                    echo "[getPrLabels] request: \$debug_curl" >&2
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
                    gh_labels_api | sed -nE 's/.*"name"[[:space:]]*:[[:space:]]*"([^\"]*)".*/\1/p' || true
                fi
            """.stripIndent().trim()
        )

        labels = labelsRaw ? labelsRaw.readLines() : []
        source = 'github-api'
    }

    return [
        labels: labels,
        source: source,
    ]
}
