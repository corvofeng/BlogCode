
node {
    // setGitHubPullRequestStatus context: 'init', message: 'jenkins start', state: 'PENDING'

    stage('checkout branches') {
        echo "in pr branch ${params.GITHUB_PR_SOURCE_BRANCH}"
        // checkout scmGit(branches: [[name: "*/${params.GITHUB_PR_SOURCE_BRANCH}"]])
    }

    stage('docker status') {
      sh '''
      docker pull nginx
      '''
    }

    // set github status success
    setGitHubPullRequestStatus context: '', message: 'jenkins finish', state: 'SUCCESS'
}
