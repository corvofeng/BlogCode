
node {
    stage('run-parallel-branches') {
        echo "in pr branch ${GITHUB_PR_SOURCE_BRANCH}"
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
    setGitHubPullRequestStatus context: '', message: '', state: 'SUCCESS'
}
