pipeline {
    agent any
    triggers {
        pollSCM 'H/2 * * * *'
    }
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
              expression { BRANCH_NAME ==~ /(master|develop|ci-test)/ }
            }
            steps {
             script {
               echo 'Pulling...' + env.BRANCH_NAME
               def mvnHome = tool 'mvn'
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
