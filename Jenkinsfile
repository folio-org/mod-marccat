pipeline {
    agent any
    stages {
      stage('Checkout And Clean') {
            steps {
              script {
                    echo 'Pulling...' + env.BRANCH_NAME
                    sh('./script/clean.sh')
                    def mvnHome = tool 'mvn'
                    sh "'${mvnHome}/bin/mvn' clean -DskipTests=true"
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
            when {
                 expression { BRANCH_NAME ==~ /(master|develop|release)/ }
                 }
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
                failure {
                     emailext body: "${currentBuild.currentResult}: Job [${env.JOB_NAME}] build #${env.BUILD_NUMBER}\n \nMore info at: ${env.BUILD_URL}\n",
                     recipientProviders: [upstreamDevelopers(), developers(), brokenBuildSuspects()],
                     subject: 'FAILURE Jenkins Pipeline mod-cataloging', to: 'christian.chiama@atcult.it',
                     attachLog: true,
                     compressLog: true
               }
            }
        }
        stage('Deploy ITNET'){
               when {
                  expression { BRANCH_NAME ==~ /(master|release)/ }
              }
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
                   failure {
                         emailext body: "${currentBuild.currentResult}: Job [${env.JOB_NAME}] build #${env.BUILD_NUMBER}\n \nMore info at: ${env.BUILD_URL}\n",
                         recipientProviders: [upstreamDevelopers(), developers(), brokenBuildSuspects()],
                         subject: 'FAILURE Jenkins Pipeline mod-cataloging', to: 'christian.chiama@atcult.it,mirko.fronzo@atcult.it',
                         attachLog: true,
                         compressLog: true
                   }
              }
         }
         stage('Publish API Docs') {
             when {
               expression { BRANCH_NAME ==~ /(master|develop|release)/ }
             }
             steps {
                echo 'Publishing API Docs....'
             }
         }
          stage('Publish Npm') {
                   when {
                      expression { BRANCH_NAME ==~ /(master|develop|release)/ }
                 }
                   steps {
                     echo 'Publishing on Npm....'
              }
         }
    }

     post {
          always {
              echo 'One way or another, I have finished'
              deleteDir() /* clean up our workspace */
          }
          success {
              echo 'I succeeeded!'
          }
          unstable {
              echo 'I am unstable :/'
          }
          failure {
              echo 'I failed :('
              emailext body: "${currentBuild.currentResult}: Job [${env.JOB_NAME}] build #${env.BUILD_NUMBER}\n \nMore info at: ${env.BUILD_URL}\n",
              recipientProviders: [upstreamDevelopers(), developers(), brokenBuildSuspects()],
              subject: 'FAILURE Jenkins Pipeline mod-cataloging', to: 'christian.chiama@atcult.it',
              attachLog: true,
              compressLog: true
          }
          changed {
              echo 'Things were different before...'
          }
      }
}
