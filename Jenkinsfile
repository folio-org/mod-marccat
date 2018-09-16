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
          when {
              expression { BRANCH_NAME ==~ /(master|develop)/ }
            }
            steps {
             script {
               echo 'Pulling...' + env.BRANCH_NAME
               def mvnHome = tool 'mvn'
               sh "mvn -Dintegration-tests.skip=true clean compile"
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
