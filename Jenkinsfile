pipeline {
    agent any
    tools {
        maven 'maven'
    }
    environment {
        DB_HOST = '192.168.100.129'
        DB_PASSWORD = 'laucesbkn2001'
    }

    stages {
        stage("Build JAR File") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/agustinsaavedra2/PEP1_Tingeso']])
                dir('PEP1_Tingeso_Backend') { 
                    bat 'mvn clean package'
                }
            }
        }

        stage('Tests') {
            steps {
                dir('PEP1_Tingeso_Backend') { 
                    bat 'mvn test'
                }
            }
        }

        stage('Build docker image') {
            steps {
                dir('PEP1_Tingeso_Backend') {
                    script {
                        bat 'docker build -t agustinsaavedra056/pep1_tingeso_backend:latest .'
                    }
                }
            }
        }

        stage('Push image to Docker Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dhpswid', variable: 'dhpsw')]) {
                        bat 'docker login -u agustinsaavedra056 -p %dhpsw%'
                    }
                    bat 'docker push agustinsaavedra056/pep1_tingeso_backend:latest'
                }
            }
        }
    }
}
