
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
      agent {
        docker {
          image 'maven:3.5.0'
        }
      }
    }
}
