
node {
    stage('run-parallel-branches') {
        parallel(
          a: {
            echo 'This is branch a'
          },
          b: {
            echo 'This is branch b'
          }
        )
    }

    stage('Maven Install') {
      sh '''
      docker pull nginx
      '''
    }

    // set github status success
    setGitHubPullRequestStatus context: '', message: '', state: 'SUCCESS'
}
