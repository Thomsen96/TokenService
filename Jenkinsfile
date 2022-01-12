pipeline {
    agent any

    stages {
        stage('Setup server') {
            steps {
                sh 'docker rm -f tokenservice_server'
                sh 'docker rm -f tokenservice_client'
            }
        }
        stage('Build server') {
            steps {
                sh 'build_and_run.sh'
                # sh 'docker build --tag tokenservice_server ./Server'
            }
        }
        stage('Build client') {
            steps {
                sh 'docker build --tag tokenservice_client ./Client'
            }
        }
        stage('Start Server') {
            steps {
                echo 'starting up tokenservice docker'
                sh'docker run --name tokenservice_server --network=deploy_app_network -d tokenservice_server:latest'
                //sleep 2
            }
        }
        stage('Test') {
            steps {
                echo 'starting up test'
                sh 'docker run --name tokenservice_client --network=deploy_app_network --entrypoint /home/app/run.sh tokenservice_client:latest'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
        stage('Clean up') {
            steps {
                sh 'docker rm -f tokenservice_server'
                sh 'docker rm -f tokenservice_client'
            }
        }
    }
}
