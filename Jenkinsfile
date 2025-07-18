pipeline {
    agent any

    environment {
        IMAGE_NAME = "yosrahb/backend-foyer"
        SONAR_HOST_URL = "http://localhost:9000"
        SONAR_PROJECT_KEY = "foyer-projet"
    }

    tools {
        maven 'MAVEN_HOME' // nom défini dans Jenkins > Global Tool Configuration
    }

    stages {
        stage('Clone') {
            steps {
                git branch: 'master', url: 'https://github.com/y189/2ALINFO1_FoyerDevOps.git'
            }
        }

        stage('Build (skip tests)') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarServer') { // nom du serveur défini dans Jenkins
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                        -Dsonar.host.url=$SONAR_HOST_URL
                    """
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
