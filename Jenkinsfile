pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                  script {
                    echo 'Pulling...'
                    def mvnHome = tool 'Maven 3.3.9'
                    sh "'${mvnHome}/bin/mvn' -Dintegration-tests.skip=true clean package"
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
         stage('Npm') {
             steps {
                echo 'Publishing on Npm....'
             }
         }
    }
}
