pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                  script {
                    echo 'Pulling...'
                    def mvnHome = tool 'Maven 3.3.9'
                    def targetVersion = getDevVersion()
                       print 'target build version...'
                       print targetVersion
                       sh "'${mvnHome}/bin/mvn' -Dintegration-tests.skip=true -Dbuild.number=${targetVersion} clean package"
                       def pom = readMavenPom file: 'pom.xml'
                       // get the current development version 
                       developmentArtifactVersion = "${pom.version}-${targetVersion}"
                       print pom.version
                       archive 'target*//*.jar'
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
def getDevVersion() {
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    def versionNumber;
    if (gitCommit == null) {
        versionNumber = env.BUILD_NUMBER;
    } else {
        versionNumber = gitCommit.take(8);
    }
    print 'build  versions...'
    print versionNumber
    return versionNumber
}

def getReleaseVersion() {
    def pom = readMavenPom file: 'pom.xml'
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    def versionNumber;
    if (gitCommit == null) {
        versionNumber = env.BUILD_NUMBER;
    } else {
        versionNumber = gitCommit.take(8);
    }
    return pom.version.replace("-SNAPSHOT", ".${versionNumber}")
}
