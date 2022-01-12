pipeline {
    agent any

    stages {
        stage('Setup server') {
            steps {
                sh 'docker rm -f token-service-server'
            }
        }
        stage('Build') {
            steps {
                sh './build.sh'
            }
        }
        stage('Deploy') {
            steps {
                sh './deploy.sh'
            }
        }
        stage('Test') {
            steps {
                
                sh'./test.sh'
                //sleep 2
            }
        }
    }
}
