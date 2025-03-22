
// podTemplate(
//     agentContainer: 'jenkins-agent-sidecar', agentInjection: true, containers:
//      [containerTemplate(args: '9999999', command: 'sleep', image: 'jenkins/inbound-agent:3301.v4363ddcca_4e7-1-alpine3.21', livenessProbe: containerLivenessProbe(execArgs: '', failureThreshold: 0, initialDelaySeconds: 0, periodSeconds: 0, successThreshold: 0, timeoutSeconds: 0), name: 'agent', resourceLimitCpu: '', resourceLimitEphemeralStorage: '', resourceLimitMemory: '', resourceRequestCpu: '', resourceRequestEphemeralStorage: '', resourceRequestMemory: '', workingDir: '/home/jenkins/agent')]) {
// // some block
// }


podTemplate(
  agentContainer: 'maven',
  agentInjection: true,
  containers: [
    containerTemplate(name: 'maven', image: 'maven:3.9.9-eclipse-temurin-17'),
    containerTemplate(name: 'golang', image: 'golang:1.24.1', command: 'sleep', args: '99d')
  ]) {

    node(POD_LABEL) {
        stage('Get a Maven project') {
            git 'https://github.com/jenkinsci/kubernetes-plugin.git'
            container('maven') {
                stage('Build a Maven project') {
                    sh 'mvn -B -ntp clean install'
                }
            }
        }

        stage('Get a Golang project') {
            git url: 'https://github.com/hashicorp/terraform.git', branch: 'main'
            container('golang') {
                stage('Build a Go project') {
                    sh '''
                    mkdir -p /go/src/github.com/hashicorp
                    ln -s `pwd` /go/src/github.com/hashicorp/terraform
                    cd /go/src/github.com/hashicorp/terraform && make
                    '''
                }
            }
        }

    }
}



/*
pipeline {
    agent {
        kubernetes {
            yaml '''
        apiVersion: v1
        kind: Pod
        metadata:
          labels:
            some-label: some-label-value
        spec:
          containers:
          - name: maven
            image: maven:3.9.9-eclipse-temurin-17
            command:
            - cat
            tty: true
          - name: busybox
            image: busybox
            command:
            - cat
            tty: true
        '''
            retries 2
        }
    }

    stages {
        stage('Run maven') {
            steps {
                container('maven') {
                    sh 'mvn -version'
                }
                container('busybox') {
                    sh '/bin/busybox'
                }
            }
        }
    }
}
*/
