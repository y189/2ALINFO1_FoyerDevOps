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

       stage('Build (skip tests)') {
            steps {
                script {
                    sh "docker build --progress=plain --build-arg SKIP_TESTS=true -t $IMAGE_NAME:latest ."
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Lancer uniquement les tests via un container Maven, monté sur le projet cloné
                    sh """
                    docker run --rm -v \$PWD:/app -w /app maven:3.9.6-eclipse-temurin-17 mvn test
                    """
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
