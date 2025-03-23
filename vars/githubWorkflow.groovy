def call(String token, String owner, String repo, String workflow, String ref, String app, String image) {
    def apiUrl = "https://api.github.com/repos/${owner}/${repo}/actions/workflows/${workflow}/dispatches"
    
    def payload = [
        ref   : ref,
        inputs: [
            app  : app,
            image: image
        ]
    ]

    def connection = new URL(apiUrl).openConnection()
    connection.setRequestMethod('POST')
    connection.setRequestProperty('Content-Type', 'application/json')
    connection.setRequestProperty('Authorization', "Bearer ${token}")
    connection.doOutput = true

    connection.outputStream.withWriter('UTF-8') { writer ->
        writer << new groovy.json.JsonBuilder(payload).toString()
    }

    def responseCode = connection.responseCode
    def responseMessage = connection.inputStream.text

    println "Response Code: ${responseCode}"
    println "Response Message: ${responseMessage}"
}