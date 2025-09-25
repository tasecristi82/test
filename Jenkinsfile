pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'ghcr.io/mihai-satmarean'
        IMAGE_NAME = 'simple-spring-app'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }
        
        stage('Test Docker Image') {
            steps {
                script {
                    sh """
                        docker run -d --name test-app-${BUILD_NUMBER} -p 8082:8080 ${IMAGE_NAME}:${IMAGE_TAG}
                        sleep 30
                        curl -f http://localhost:8082/actuator/health
                        curl -f http://localhost:8082/
                        curl -f http://localhost:8082/hello?name=Jenkins
                    """
                }
            }
        }
        
        stage('Push to Registry') {
            steps {
                script {
                    docker.image("${IMAGE_NAME}:${IMAGE_TAG}").push()
                }
            }
        }
    }
    
    post {
        always {
            script {
                sh "docker stop test-app-${BUILD_NUMBER} || true"
                sh "docker rm test-app-${BUILD_NUMBER} || true"
            }
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
