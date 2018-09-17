pipeline {
    agent any
    environment { 
        DEPLOY_PORT = 8888
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
   post {
    failure {
      // notify users when the Pipeline fails
      mail to: 'c.chiama@icloud.com',
          subject: "Failed Pipeline: test",
          body: "Something is wrong"
    }
  }
}
