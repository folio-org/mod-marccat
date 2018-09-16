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
              expression { BRANCH_NAME ==~ /(master|develop|ci-test)/ }
            }
            steps {
             script {
               echo 'Pulling...' + env.BRANCH_NAME
               def mvnHome = tool 'mvn'
               sh "'${mvnHome}/bin/mvn' -Dintegration-tests.skip=true clean compile"
               }
            }
        }
        stage('Test') {
            steps {
                echo 'test in progress.....'
                def mvnHome = tool 'mvn'
                sh "'${mvnHome}/bin/mvn' clean package"
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
def version() {
     def matcher = readFile('pom.xml') =~ '<version>(.+?)</version>'
     matcher ? matcher[0][1] : null
 }
def getDevVersion() {
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    def versionNumber;
    if (gitCommit == null) {
        versionNumber = env.BUILD_NUMBER;
    } else {
        versionNumber = gitCommit.take(8);
    }
    print 'build versions...'
    print versionNumber
    return versionNumber
}
def getReleaseVersion() {
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    def versionNumber;
    if (gitCommit == null) {
        versionNumber = env.BUILD_NUMBER;
    } else {
        versionNumber = gitCommit.take(8);
    }
    return version()
}
