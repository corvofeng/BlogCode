/* groovylint-disable MethodReturnTypeRequired */
/* groovylint-disable MethodParameterTypeRequired */
/* groovylint-disable DuplicateStringLiteral */
/* groovylint-disable-next-line NoDef */
def call(Closure body) {
    call([:], body)
}

/* groovylint-disable-next-line NoDef */
def call(image, Closure body) {
    call([image: image], body)
}

/* groovylint-disable-next-line NoDef */
def call(image, imageProxyPrefix, Closure body) {
    call([image: image, imageProxyPrefix: imageProxyPrefix], body)
}

/* groovylint-disable-next-line NoDef */
def call(Map config) {
    def body = config.body
    if (!(body instanceof Closure)) {
        throw new IllegalArgumentException('podTemplateLibrary requires a body closure')
    }

    def podConfig = config.findAll { key, _ -> key != 'body' }
    call(podConfig, body)
}

/* groovylint-disable-next-line NoDef */
def call(Map config, Closure body) {
    def imageProxyPrefix = config.imageProxyPrefix ?: config.registryPrefix ?: ''
    def runnerImage = withImageProxyPrefix(config.image ?: 'jenkins/jnlp-agent-docker:latest', imageProxyPrefix)
    def agentImage = withImageProxyPrefix(config.agentImage ?: 'jenkins/inbound-agent:rhel-ubi9-jdk25', imageProxyPrefix)

    podTemplate(
        agentContainer: 'agent',
        agentInjection: true,
        envVars: [
            envVar(key: 'DOCKER_HOST', value: 'tcp://tcp-gitea-docker-builder:2376'),
            envVar(key: 'DOCKER_TLS_VERIFY', value: ''),
        ],
        containers: [
            // Used for user to run their commands
            containerTemplate(name: 'runner', image: runnerImage, command: 'sleep', args: '99d'),

            // Used to connect with jenkins master
            containerTemplate(args: '9999999', command: 'sleep', image: agentImage, name: 'agent', workingDir: '/home/jenkins/agent'),
        ]) {
        node(POD_LABEL) {
            container('runner') {
                body()
            }
        }
    }
}

/* groovylint-disable-next-line NoDef */
def withImageProxyPrefix(image, imageProxyPrefix) {
    if (!imageProxyPrefix) {
        return image
    }

    return "${imageProxyPrefix.toString().replaceAll('/+$', '')}/${image}"
}
