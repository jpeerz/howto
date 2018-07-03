node ("deploy"){
   
    properties([
        buildDiscarder(
            logRotator(
                artifactDaysToKeepStr: '',
                artifactNumToKeepStr: '',
                daysToKeepStr: '3',
                numToKeepStr: '3'
            )
        ), 
        disableConcurrentBuilds(), 
        pipelineTriggers([]),
        parameters([
            string(
                name: 'RELEASE_NAME',
                defaultValue: 'latest',
                description: 'Release tag to dockerize and save in registry'
            ),
            string(
                name: 'DOCKER_REGISTRY',
                defaultValue: '34.215.221.237:5000',
                description: 'Server waiting docker push'
            ),
            string(
                name: 'DOCKER_WEB_IP',
                defaultValue: '172.31.31.222',
                description: 'Client Server waiting docker pull'
            )
        ])
    ])
    
    stage('Fetch New Containers') {
        try {
            echo 'Downloading new package'
            /*docker.withRegistry("http://${DOCKER_REGISTRY}"){
                myapp = docker.image("app:${RELEASE_NAME}")
                myapp.pull()
            }*/
        } catch(Exception err) {
            echo "Building container failed: $err"
            throw err
        }
    }
    
    stage('Startup Database') {
        try {
            //sh "docker run -d --name mongodb "
            echo 'Configuring DB'
        } catch(Exception err) {
            echo "Deploying database container failed: $err"
            throw err
        }
    }
    
    stage('Run App') {
        try {
            //sh "docker run -d --name "
            echo 'Starting Docker app'
        } catch(Exception err) {
            echo "Deploying container failed: $err"
            throw err
        }
    }
}
