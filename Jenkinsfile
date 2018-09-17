pipeline {
    agent any
    environment { 
        DEPLOY_TO = 'development'
        DEPLOY_PORT = 8888
        EMAIL_REPORT = 'christian.chiama@atcult.it'
    }
  
     parameters {
           text(name: 'CCHIAMA', defaultValue: ${EMAIL_REPORT}, description: 'Enter some information about the person')
           choice(name: 'EMAIL', choices: ['christian.chiama@atcult.it', 'natascia.bianchini@atcult.it', 'alice-guercio@atcult.it'], description: 'Notification email')
        }
    stages {
      stage('Clean') {
           options {
                timeout(time: 3, unit: 'MINUTES') 
            }
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
            options {
                timeout(time: 5, unit: 'MINUTES') 
            }
          when {
              expression { BRANCH_NAME ==~ /(master|develop|ci-test)/ }
              anyOf {
                    environment name: 'DEPLOY_TO', value: 'production'
                    environment name: 'DEPLOY_TO', value: 'staging'
                }
            }
            steps {
                script {
                   echo 'Pulling...' + env.BRANCH_NAME
                   def mvnHome = tool 'mvn'
                   sh "'${mvnHome}/bin/mvn' clean compile package -DskipTests"
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
           options {
                timeout(time: 5, unit: 'MINUTES') 
            }
                steps{
                    script{
                        withEnv(['JENKINS_NODE_COOKIE=dontkill']) {
                          sh "nohup java -Dserver.port=${DEPLOY_PORT} -jar ./target/mod-cataloging-1.0.jar &"
                        }
                    }
                }
            post {
                success {
                    echo 'deploy succesfully on port ${DEPLOY_PORT}'
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
