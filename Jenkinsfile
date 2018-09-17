pipeline {
    agent any
    stages {
      stage('Clean') {
            steps {
                sh "chmod -R 777 script/"
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
                   archive 'target*//*.jar'
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
                            sh 'nohup java -Dserver.port=8888 -jar ./target/mod-cataloging-1.0.jar &'
                    }
                }
            post {
                success {
                    echo 'deploy succesfully mod-cataloging up and running on port 8889'
                }
            }
        }
    }
}
