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
                    // Construction Maven en ignorant les tests
                    sh 'mvn clean package -DskipTests=true'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    echo "Lancement des tests Maven"
                    // Lancer les tests unitaires Maven
                    sh 'mvn test'
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
