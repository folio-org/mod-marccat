pipeline {
    agent any
    triggers {
        pollSCM 'H/2 * * * *'
    }
     tools {
       maven 'apache-maven-3.5.4'
     }
    stages {
      stage('Clean') {
                steps {
                   sh('./script/clean.sh')
                }
                post {
                     success {
                         sh 'mvn --version'
                    }
                 }
            }
        stage('Build') {
          when {
              expression { BRANCH_NAME ==~ /(master|develop)/ }
            }
            steps {
               echo 'Build...'
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
