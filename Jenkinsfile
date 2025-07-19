pipeline {
    agent any

    environment {
        IMAGE_NAME = "yosrahb/backend-foyer"
        IMAGE_VERSION = "1.5.0"
        SONAR_HOST_URL = "http://localhost:9000"
        SONAR_PROJECT_KEY = "foyer-projet"
        NEXUS_URL = "localhost:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIALS_ID = "nexus-credentials"           // doit exister dans Jenkins
        DOCKER_HUB_CREDENTIALS_ID = "docker-hub-credentials" // doit exister dans Jenkins
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

        stage('Build JAR') {
            steps {
                sh "mvn clean package -DskipTests=true"
            }
        }

        stage('Test') {
            steps {
                sh "mvn test"
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

        stage('Upload JAR to Nexus') {
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

        stage('Build Docker Image from Nexus') {
            steps {
             withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
    sh """
        set -ex
        docker build \\
            --build-arg NEXUS_URL=http://${NEXUS_URL} \\
            --build-arg REPOSITORY=${NEXUS_REPOSITORY} \\
            --build-arg GROUP_ID=tn.esprit.spring \\
            --build-arg ARTIFACT_ID=Foyer \\
            --build-arg VERSION=${IMAGE_VERSION} \\
            --build-arg NEXUS_USER=\$NEXUS_USER \\
            --build-arg NEXUS_PASS=\$NEXUS_PASS \\
            -t ${IMAGE_NAME}:${IMAGE_VERSION} .
    """
}

            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKER_HUB_CREDENTIALS_ID}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_VERSION}
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé (succès ou échec).'
        }
        success {
            echo '✅ Déploiement réussi'
        }
        failure {
            echo '❌ Le pipeline a échoué'
        }
    }
}
