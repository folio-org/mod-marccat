pipeline {
    agent any
    stages {
      stage('Clean') {
            steps {
                sh('./script/clean.sh')
            }
            post {
                success {
                    echo 'cleaning succesfully...'
                }
            }
        }
        stage('Build') {
            steps {
                script {
                   echo 'Pulling...' + env.BRANCH_NAME
                   def mvnHome = tool 'mvn'
                   sh "'${mvnHome}/bin/mvn' clean compile package -DskipTests=true"
                   archiveArtifacts 'target*//*.jar'
               }
            }
            post {
                success {
                    echo 'cleaning succesfully...'
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy'){
                steps{
                    script{
                        withEnv(['JENKINS_NODE_COOKIE=dontkill']) {
                            sh "nohup java -Dserver.port=8888 -jar ./target/mod-cataloging-1.0.jar &"
                        }
                    }
                }
            post {
                success {
                    echo 'deploy succesfully on port 8888'
                }
            }
        }
         stage('Npm') {
             steps {
                echo 'Publishing on Npm....'
             }
         }
    }
}
