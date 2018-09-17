pipeline {
    agent any
    stages {
      stage('Clean') {
            steps {
                echo 'cleaning...'
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
                            sh('./script/deploy.sh')
                        }
                    }
                }
            post {
                success {
                    echo 'mod-catlogin up and running on port 8080'
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
