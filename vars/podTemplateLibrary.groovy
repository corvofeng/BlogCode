/* groovylint-disable MethodReturnTypeRequired */
/* groovylint-disable MethodParameterTypeRequired */
/* groovylint-disable-next-line NoDef */
def call(image='jenkins/jnlp-agent-docker:latest', body) {
    podTemplate(
        agentContainer: 'agent',
        agentInjection: true,
        envVars: [
            envVar(key: 'DOCKER_HOST', value: 'tcp://tcp-gitea-docker-builder:2376'),
            envVar(key: 'DOCKER_TLS_VERIFY', value: ''),
        ],
        containers: [
            containerTemplate(args: '9999999', command: 'sleep', image: image, name: 'agent', workingDir: '/home/jenkins/agent'),
            containerTemplate(name: 'golang', image: 'golang:1.24.1', command: 'sleep', args: '99d')
        ]) {
        node(POD_LABEL) {
            container('agent') {
                body()
            }
        }
    }
}
