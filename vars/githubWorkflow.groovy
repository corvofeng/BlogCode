def call(String tokenID, String owner, String repo, String workflow, String ref, String app, String image) {
    def apiUrl = "https://api.github.com/repos/${owner}/${repo}/actions/workflows/${workflow}/dispatches"
    withCredentials([string(credentialsId: tokenID, variable: 'GH_TOKEN')]) {
        sh """
          curl --location ${apiUrl} \
          --header 'Content-Type: application/json' \
          --header 'Authorization: Bearer ${GH_TOKEN}' \
          --data '{
              "ref": "${ref}",
              "inputs": {
                  "app": "${app}",
                  "image": "${image}"
              }
          }'
          """
    }
}
