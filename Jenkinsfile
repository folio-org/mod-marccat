pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''PID=$(lsof -t -i:$PORT)
'''
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