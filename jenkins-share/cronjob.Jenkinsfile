
/* groovylint-disable-next-line */
@Library('jenkins-pod') _

// https://plugins.jenkins.io/parameterized-scheduler/
// Example: https://jenkins.in.corvo.fun/job/BlogCodeSharing/job/CronJobWork/
node {
    properties([
      parameters([
        string(name: 'PLANET', defaultValue: 'Earth', description: 'Which planet are we on?'),
        string(name: 'GREETING', defaultValue: 'Hello', description: 'How shall we greet?')
      ]),

      pipelineTriggers([
        parameterizedCron('''
            0 0 1 * * %task=refresh
            */2 * * * * %GREETING=Hola;PLANET=Pluto
            */3 * * * * %PLANET=Mars
        ''')
      ])
    ])

    stage('Planet') {
        echo "We are on planet ${params.PLANET}"
    }
    stage('Greeting') {
        echo "${params.GREETING} ${params.PLANET}"
    }
}
