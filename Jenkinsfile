pipeline {
    agent any
    environment { 
        DEPLOY_TO = 'development'
        DEPLOY_PORT = 8888
        EMAIL_REPORT = 'christian.chiama@atcult.it'
    }
  
     parameters {
           text(name: 'CCHIAMA', defaultValue: 'christian.chiama@atcult.it', description: 'email for jenkins report')
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
        parallel {
                stage('Branch A') {
                    agent {
                        label "for-branch-a"
                    }
                    steps {
                        echo "On Branch A"
                    }
                }
                stage('Branch B') {
                    agent {
                        label "for-branch-b"
                    }
                    steps {
                        echo "On Branch B"
                    }
                }
           parallel {
                stage('Branch A') {
                    agent {
                        label "for-branch-a"
                    }
                    steps {
                        echo "On Branch A"
                    }
                }
                stage('Branch B') {
                    agent {
                        label "for-branch-b"
                    }
                    steps {
                        echo "On Branch B"
                    }
                }
          stage('Branch C') {
                    agent {
                        label "for-branch-c"
                    }
                    stages {
                        stage('Nested 1') {
                            steps {
                                echo "In stage Nested 1 within Branch C"
                            }
                        }
                        stage('Nested 2') {
                            steps {
                                echo "In stage Nested 2 within Branch C"
                            }
                        }
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
