podTemplate(yaml: '''
    apiVersion: v1
    kind: Pod
    metadata:
      labels: 
        some-label: some-label-value
    spec:
      containers:
      - name: busybox
        image: busybox
        command:
        - sleep
        args:
        - 99d
    ''') {
    node(POD_LABEL) {
      container('busybox') {
        echo POD_CONTAINER // displays 'busybox'
        sh 'hostname'
      }
    }
}
