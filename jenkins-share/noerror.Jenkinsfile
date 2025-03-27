def testWrapper(Closure job) {
    def varFunc = { func, Object... args ->
        args.each { println it }
        println "args ${args}, ${args.getClass()}, ${args.size()}"
        func.call(*args)
    }
    return varFunc.curry(job)
}

def retryWrapper(Closure job) {
    def func = { Object[] args ->
        waitUntil {
            try {
                println "args ${args}, ${args.getClass()}, ${args.size()}"
                job.call(*args)
                true
            } catch (error) {
                println error
                input 'Retry the job ?'
                false
            }
        }
    }
    return func
}

node {
    // gitHubPRStatus githubPRMessage('${GITHUB_PR_COND_REF} run started')
    // setGitHubPullRequestStatus context: 'init', message: '', state: 'PENDING'
    stage('run-parallel-branches') {
        checkout scm
        parallel(
          a: {
            echo "This is branch a"
            parallel {
                c: {
                    echo "This is branch c"
                },
                d: {
                    echo "This is branch d"
                },
            },
          },
          b: {
            echo "This is branch b"
          }
        )
    }

    stage('looper-parallel-branches') {
        def looper = [:]
        for (int i = 0; i < 5; i++) {
            looper["${i}"] = {
                echo "This is branch ${i}"
            }
        }
        parallel looper
    }

    stage('retry') {
        testJob = { ->
            // generate number between 40 and 99
            def num = Math.abs( new Random().nextInt() % (99 - 40) ) + 40
            if(num % 2 == 0) {
                throw new Exception("Even number")
            }
        }

        retryFunc = { job ->
            waitUntil {
                try {
                    job()
                    true
                } catch (error) {
                    println error
                    input 'Retry the job ?'
                    false
                }
            }
        }

        def looper = [:]
            for (int i = 0; i < 5; i++) {
            looper["${i}"] = {
                retryFunc(testJob)
            }
        }
        // parallel looper
    }

    // setGitHubPullRequestStatus context: '', message: '', state: 'SUCCESS'


    def workNodes = ['192.168.1.1', '192.168.1.2', '192.168.1.3']
    stage('Action') {
        stage('action 1') {
            def looper = [:]
            workNodes.each { node ->
                looper["Action1 for ${node}"] = {
                    println "action1 on $node"
                }
            }
            parallel looper
        }
        
        stage('action 2') {
            def looper = [:]
            workNodes.each { node ->
                looper["Action2 for ${node}"] = {
                    println "action2 on $node"
                }
            }
            parallel looper
        }
    }
    stage("Action Runner") {
        def actionRunner = { msg, nodes, action ->
            def looper = [:]
            nodes.each { node ->
                looper["${msg} -- ${node}"] = {
                    stage("${msg} -- ${node}") {
                        action(node)
                    }
                }
            }
            parallel looper
        }

        actionRunner("action1", workNodes, { node ->
            println "action1 on $node"
        })

        actionRunner("action2", workNodes, { node ->
            println "action2 on $node"
        })

    }

    /*
    stage('Retry Wrapper') {
        testJob = retryWrapper({ arg1, arg2 ->
            // generate number between 40 and 99
            println("${arg1}, ${arg2}")
            def num = Math.abs( new Random().nextInt() % (99 - 40) ) + 40
            if(num % 2 == 0) {
                throw new Exception("Even number")
            }
        })

        def looper = [:]
        for (int i = 0; i < 5; i++) {
            looper["${i}"] = {
                testJob(["arg1", i])
            }
        }
        // parallel looper
    }
    */
}
