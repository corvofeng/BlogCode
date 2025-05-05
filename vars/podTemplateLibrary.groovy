/* groovylint-disable MethodReturnTypeRequired */
/* groovylint-disable MethodParameterTypeRequired */
/* groovylint-disable-next-line NoDef */
def call(image='golang:1.24.1', body) {
    podTemplate(
        agentContainer: 'agent',
        agentInjection: true,
        envVars: [
            envVar(key: 'DOCKER_HOST', value: 'tcp://tcp-gitea-docker-builder:2376'),
            envVar(key: 'DOCKER_TLS_VERIFY', value: ''),
        ],
        containers: [
            containerTemplate(args: '9999999', command: 'sleep', image: 'jenkins/jnlp-agent-docker:latest', name: 'agent', workingDir: '/home/jenkins/agent'),
            containerTemplate(name: 'runner', image: image, command: 'sleep', args: '99d')
        ]) {
        node(POD_LABEL) {
            container('agent') {
                body()
            }
        }
    }
}
