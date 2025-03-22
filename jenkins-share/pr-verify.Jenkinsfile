
node {
    // setGitHubPullRequestStatus context: 'init', message: 'jenkins start', state: 'PENDING'

    stage('run-parallel-branches') {
        echo "in pr branch ${params.GITHUB_PR_SOURCE_BRANCH}"
        // checkout scmGit(branches: [[name: "*/${params.GITHUB_PR_SOURCE_BRANCH}"]])

        parallel(
          a: {
            echo 'This is branch a'
          },
          b: {
            echo 'This is branch b'
          }
        )
    }

    stage('docker status') {
      sh '''
      docker pull nginx
      '''
    }

    // set github status success
    setGitHubPullRequestStatus context: '', message: 'jenkins finish', state: 'SUCCESS'
}
