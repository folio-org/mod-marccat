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
                    sh('./script/clean.sh')
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
              echo 'delete workspace....'
              deleteDir()
          }
          success  {
              echo 'Pipeline failed!!!!'
              emailext body: "${currentBuild.currentResult}: Job [${env.JOB_NAME}] build #${env.BUILD_NUMBER}\n \nMore info at: ${env.BUILD_URL}\n",
              recipientProviders: [upstreamDevelopers(), developers(), brokenBuildSuspects()],
              subject: 'FAILURE Jenkins Pipeline mod-cataloging', to: 'christian.chiama@atcult.it,mirko.fonzo@atcult.it',
              attachLog: true,
              compressLog: true
          }
      }
}
