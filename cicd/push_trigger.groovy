node {
    properties([
        buildDiscarder(
            logRotator(
                artifactDaysToKeepStr: '', 
                artifactNumToKeepStr: '', 
                daysToKeepStr: '10', 
                numToKeepStr: '20')
        ), 
        disableConcurrentBuilds(), 
        pipelineTriggers([
            [$class: "GitHubPushTrigger"]
        ]),
        parameters([
            string(
                name: 'NAME_OF_BRANCH',
                defaultValue: 'master',
                description: 'Trigger CI with different branch.'
            )
        ])
    ])
    stage('Fetch Code and Build') {
        try {
            build([
                job: 'build', 
                parameters: [
                    string(name: 'NAME_OF_BRANCH', value: params.NAME_OF_BRANCH)
                ], 
                wait: true
            ])
        } catch(Exception err) {
            echo "Deployment Failed: $err"
            throw err
        }
    }
    stage('Containerize in Server') {
        try {
            build([
                job: 'deploy', 
                parameters: [
                    string(name: 'NAME_OF_BRANCH', value: params.NAME_OF_BRANCH)
                ],
                wait: true
            ])
        } catch(Exception err) {
            echo "Deployment Failed: $err"
            throw err
        }
    }
}
