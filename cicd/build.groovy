node ("build"){
   
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
                name: 'NAME_OF_BRANCH',
                defaultValue: 'master',
                description: 'Trigger CI with different branch.'
            ),
            string(
                name: 'REPO_URI',
                defaultValue: 'https://github.com/jpeerz/backtojava.git',
                description: 'SCM uri'
            ),
            string(
                name: 'DOCKER_REGISTRY',
                defaultValue: '34.215.221.237:5000',
                description: 'Server waiting docker push'
            )
        ])
    ])
    
    git url: params.REPO_URI, branch: params.NAME_OF_BRANCH
    
    stage('Checkout Lastest'){
        try {
            do_maven("-f web/pom.xml clean install package -Dmaven.test.skip=true -Dlicense.skip=true")
        } catch(Exception err) {
            echo "Building hygieia container failed: $err"
            throw err
        }
    }
    
    stage('Run Tests'){
        try {
            do_maven("-f web/pom.xml test")
            junit '**/target/surefire-reports/*.xml'
        } catch(Exception err) {
            echo "Some tests failed: $err"
            throw err
        }
    }
    
    stage("Save in Artifactory"){
    }
    
    /*stage('Rebuild Containers') {
        try {
            do_maven("-f web/pom.xml docker:build --fail-never -Dmaven.test.skip=true -Dlicense.skip=true")
        } catch(Exception err) {
            echo "Building hygieia container failed: $err"
            throw err
        }
    }
    
    stage('Publish New Version') {
        try {
            docker.withRegistry("http://${DOCKER_REGISTRY}"){
                api = docker.image('hygieia-api')
                api.push("${NAME_OF_BRANCH}")
                ui = docker.image('hygieia-ui')
                ui.push("${NAME_OF_BRANCH}")
            }
        } catch(Exception err) {
            echo "Uploading container failed: $err"
            throw err
        }
    }*/
}

def do_maven(mvn_task){
    def MAVEN = tool 'MAVEN3'
    try{
        sh "export MAVEN_OPTS='-Xms128m -Xmx128m -XX:+HeapDumpOnOutOfMemoryError'"
        sh "$MAVEN/bin/mvn $mvn_task"
    } catch(Exception err) {
        throw err
    }
}
