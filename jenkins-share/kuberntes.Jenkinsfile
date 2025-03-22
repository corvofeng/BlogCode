
podTemplate(
  agentContainer: 'agent',
  agentInjection: true,
  envVars: [
    envVar(key: 'DOCKER_HOST', value: 'tcp://tcp-gitea-docker-builder:2376'),
    envVar(key: 'DOCKER_TLS_VERIFY', value: ''),
  ],
  containers: [
    // containerTemplate(args: '9999999', command: 'sleep', image: 'jenkins/inbound-agent:3301.v4363ddcca_4e7-1-alpine3.21', name: 'agent', workingDir: '/home/jenkins/agent'),
    containerTemplate(args: '9999999', command: 'sleep', image: 'jenkins/jnlp-agent-docker:latest', name: 'agent', workingDir: '/home/jenkins/agent'),
    containerTemplate(name: 'golang', image: 'golang:1.24.1', command: 'sleep', args: '99d')
  ]) {
    node(POD_LABEL) {
        stage('Get a docker project') {
            // git url: 'https://github.com/hashicorp/terraform.git', branch: 'main'
            container('agent') {
                stage('Build docker project') {
                    sh '''
                    docker version
                    docker images
                    '''
                }
            }
        }

        stage('Get a Golang project') {
            container('golang') {
                stage('Build a Go project') {
                    sh '''
                    go version
                    # mkdir -p /go/src/github.com/hashicorp
                    # ln -s `pwd` /go/src/github.com/hashicorp/terraform
                    # cd /go/src/github.com/hashicorp/terraform && make
                    '''
                }
            }
        }
    }
  }
