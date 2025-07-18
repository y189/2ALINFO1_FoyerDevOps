pipeline {
    agent any

    environment {
        IMAGE_NAME = "yosrahb/backend-foyer"
        SONAR_HOST_URL = "http://localhost:9000"
        SONAR_PROJECT_KEY = "foyer-projet"
        NEXUS_URL = "http://localhost:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIALS_ID = "nexus-credentials"
    }

    tools {
        maven 'MAVEN_HOME'
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
                withSonarQubeEnv('SonarServer') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.host.url=${SONAR_HOST_URL}
                    """
                }
            }
        }

        stage('Upload to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: "${NEXUS_URL}",
                    groupId: 'tn.esprit.spring',                 // correspond au <groupId> du pom.xml
                    version: '1.4.0-SNAPSHOT',                   // correspond au <version> du pom.xml
                    repository: "${NEXUS_REPOSITORY}",
                    credentialsId: "${NEXUS_CREDENTIALS_ID}",
                    artifacts: [
                        [
                            artifactId: 'Foyer',                  // correspond au <artifactId> du pom.xml
                            type: 'jar',
                            file: 'target/Foyer-1.4.0-SNAPSHOT.jar'
                        ]
                    ]
                )
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
