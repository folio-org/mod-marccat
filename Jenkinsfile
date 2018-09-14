pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
          echo 'Pulling from Atcult mod-cataloging...'
          def mvnHome = tool 'Maven 3.3.9'
          def targetVersion = getDevVersion()
          print 'target build version...'
          print targetVersion
          sh "'${mvnHome}/bin/mvn' -Dintegration-tests.skip=true -Dbuild.number=${targetVersion} clean compile"
          def pom_version = version()
          // get the current development version
          developmentArtifactVersion = "${pom_version}-${targetVersion}"
          print pom_version
          archiveArtifacts 'target*//*.jar'
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
