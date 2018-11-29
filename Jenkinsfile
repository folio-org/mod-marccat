pipeline {
    agent any
     environment {
         doError = '1'
     }
    stages {
      stage('SCM Checkout') {
            steps {
              script {
                    echo 'Pulling...' + env.BRANCH_NAME
                    sh('./script/deploy/clean.sh')
                }
             }
        }
        stage('Build') {
            steps {
                script {
                   def mvnHome = tool 'mvn'
                   sh "'${mvnHome}/bin/mvn' clean compile package -DskipTests=true"
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
                            sh('./script/deploy/deploy.sh')
                        }
                    }
                }
        }
        stage('Deploy ITNET'){
               steps{
                    script{
                         withEnv(['JENKINS_NODE_COOKIE=dontkill']) {
                                  sh('./script/deploy/deploy_itnet.sh')
                                }
                            }
                        }
         }
         stage('Publish API Docs') {
             steps {
                echo 'Publishing API Docs....'
             }
         }
          stage('Publish Npm') {
                   steps {
                     echo 'Publishing on Npm....'
              }
         }
    }

     post {
          always {
              echo 'pipeline finished!'
          }
          success {
              echo 'mod-catalogin deployed succesfully on Zeta and ITNET and up and running on port 8080'
          }
          failure {
              echo 'Pipeline failed!!!!'
              emailext body: "${currentBuild.currentResult}: Job [${env.JOB_NAME}] build #${env.BUILD_NUMBER}\n \nMore info at: ${env.BUILD_URL}\n",
              recipientProviders: [upstreamDevelopers(), developers(), brokenBuildSuspects()],
              subject: 'FAILURE Jenkins Pipeline mod-catamarccat', to: 'christian.chiama@atcult.it,mirko.fonzo@atcult.it',
              attachLog: true,
              compressLog: true
          }
      }
}
