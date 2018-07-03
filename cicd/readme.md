
# Create a simple Continuous Integration

What we need ?

* Github - Source code uner version control
* Jenkins
* Java
* Maven
* Artifactory

## Define localhost domain map

Using public IP assignment

    172.31.16.33    jenkins.aws
    172.31.18.48    docker.aws artifactory.aws
    172.31.31.222   web.aws app.aws

Using private network

    34.216.233.212  jenkins.aws
    34.219.51.87    web.aws app.aws
    34.217.30.55    docker.aws artifactory.aws

## Running our web app first time

Using instruction from https://spring.io/guides/gs/serving-web-content/

Adding Unit test sample based on https://raw.githubusercontent.com/junit-team/junit5-samples/r5.2.0/junit5-jupiter-starter-maven/pom.xml

* https://github.com/jpeerz/backtojava.git

## Build the Orchestrator Machine

Created credentials with id ff99f001-bd2c-4d69-8d48-c3bbab3a3bc8 (can use it for GitHub Server Config)


## Build the Artifact Box

https://www.jfrog.com/confluence/display/RTF/Installing+with+Docker

    docker pull docker.bintray.io/jfrog/artifactory-oss:latest

Start artifactory service

    docker run --name artifactory -d -p 8081:8081 -e HA_DATA_DIR=/var/opt/jfrog/artifactory/data docker.bintray.io/jfrog/artifactory-oss:latest

Or with persistence volume

    docker volume create jfrog_data
    docker run -d -p 8081:8081 --name artifactory --rm -v jfrog_data:/var/opt/jfrog/artifactory/data docker.bintray.io/jfrog/artifactory-oss:latest

## Integrate with Jenkins

https://www.jfrog.com/confluence/display/RTF/Jenkins+Artifactory+Plug-in
https://github.com/jenkinsci/artifactory-plugin


## download artifact and run from Sandbox machine

~/workspace/build$ java -jar ./web/target/gs-serving-web-content-0.1.0.jar


