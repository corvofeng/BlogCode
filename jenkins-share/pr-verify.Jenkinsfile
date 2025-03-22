
node {
    // setGitHubPullRequestStatus context: 'init', message: 'jenkins start', state: 'PENDING'

    stage('run-parallel-branches') {
        // echo "in pr branch ${GITHUB_PR_SOURCE_BRANCH}"
        checkout scm

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
