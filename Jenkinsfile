pipeline {
    agent any
     environment {
         DISABLE_AUTH = 'true'
     }
    stages {
      stage('Checkout And Clean') {
            steps {
              script {
                    echo 'Pulling...' + env.BRANCH_NAME
                    def mvnHome = tool 'mvn'
                    sh "'${mvnHome}/bin/mvn' clean -DskipTests=true"
                    echo 'cleaning...'
                }
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
                   def mvnHome = tool 'mvn'
                   sh "'${mvnHome}/bin/mvn' compile package -DskipTests=true"
                   archiveArtifacts 'target*//*.jar'
               }
            }
        }
        stage('Test') {
            steps {
                echo 'Executing test.....'
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
                    echo 'mod-catalogin up and running on port 8080'
                }
            }
        }
        stage('Deploy ITNET'){
               steps{
                    script{
                         withEnv(['JENKINS_NODE_COOKIE=dontkill']) {
                                  sh('./script/deploy_itnet.sh')
                                }
                            }
                        }
              post {
                   success {
                         echo 'mod-catalogin up and running on port 8080 on ITNET'
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
