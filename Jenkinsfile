pipeline {
    agent any

    environment {
        IMAGE_NAME = "yosrahb/backend-foyer"
        SONAR_HOST_URL = "http://sonarqube:9000"
        SONAR_PROJECT_KEY = "foyer-projet"
    }

    stages {
        stage('Clone') {
            steps {
                git branch: 'master', url: 'https://github.com/y189/2ALINFO1_FoyerDevOps.git'
            }
        }

        stage('Build & Test (via Dockerfile)') {
            steps {
                script {
                    // Cette étape lance le build Maven et les tests via Dockerfile multi-stage
                    sh "docker build --progress=plain -t $IMAGE_NAME:latest ."

                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_LOGIN')]) {
                    withSonarQubeEnv('MySonarQube') {
                        // Utilise l'image maven temporairement uniquement pour Sonar
                        sh """
                        docker run --rm -v \$PWD:/app -w /app maven:3.9.6-eclipse-temurin-17 \
                        mvn sonar:sonar -DskipTests -Dsonar.login=$SONAR_LOGIN
                        """
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                        sh "docker push $IMAGE_NAME:latest"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh "docker pull $IMAGE_NAME:latest"
                    sh "docker stop app || true"
                    sh "docker rm app || true"
                    sh "docker run -d --name app -p 8080:8080 $IMAGE_NAME:latest"
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé (succès ou échec).'
        }
        success {
            echo 'Déploiement réussi'
        }
        failure {
            echo 'Le pipeline a échoué'
        }
    }
}
