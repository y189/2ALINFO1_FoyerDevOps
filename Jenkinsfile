pipeline {
    agent any

    environment {
        IMAGE_NAME = "yosrahb/backend-foyer"
        IMAGE_VERSION = "1.4.0"
        SONAR_HOST_URL = "http://localhost:9000"
        SONAR_PROJECT_KEY = "foyer-projet"
        NEXUS_URL = "localhost:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIALS_ID = "nexus-credentials"
        DOCKER_HUB_CREDENTIALS_ID = "docker-hub-credentials"
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

        stage('Clean') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean"
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
                    groupId: 'tn.esprit.spring',
                    version: "${IMAGE_VERSION}",
                    repository: "${NEXUS_REPOSITORY}",
                    credentialsId: "${NEXUS_CREDENTIALS_ID}",
                    artifacts: [
                        [
                            artifactId: 'Foyer',
                            type: 'jar',
                            file: "target/Foyer-${IMAGE_VERSION}.jar"
                        ]
                    ]
                )
            }
        }

    stage('Docker Build from Nexus') {
    steps {
        withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIALS_ID}")]) {
            script {
                sh """
                    docker build \
                        --build-arg NEXUS_URL=http://${NEXUS_URL} \
                        --build-arg REPOSITORY=${NEXUS_REPOSITORY} \
                        --build-arg GROUP_ID=tn.esprit.spring \
                        --build-arg ARTIFACT_ID=Foyer \
                        --build-arg VERSION=${IMAGE_VERSION} \
                        --build-arg NEXUS_USER=$NEXUS_USER \
                        --build-arg NEXUS_PASS=$NEXUS_PASS \
                        -t ${IMAGE_NAME}:${IMAGE_VERSION} .
                """
            }
        }
    }
}


        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_HUB_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        sh """
                            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                            docker tag ${IMAGE_NAME}:${IMAGE_VERSION} ${IMAGE_NAME}:${IMAGE_VERSION}
                            docker push ${IMAGE_NAME}:${IMAGE_VERSION}
                            docker logout
                        """
                    }
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
