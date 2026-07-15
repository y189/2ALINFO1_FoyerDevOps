# Foyer DevOps – Gestion de Foyer Universitaire

Application backend **Spring Boot** pour la gestion d'un foyer universitaire (blocs, chambres, étudiants, réservations, universités), packagée dans une pipeline **CI/CD Jenkins** complète (build, tests, analyse qualité, artefact Nexus, image Docker, Docker Hub).

## 🧰 Stack technique

- **Java 17**
- **Spring Boot 3.1.5** (Web, Data JPA, Mail)
- **MySQL** (base de données)
- **H2** (base en mémoire pour les tests)
- **Lombok**
- **Springdoc OpenAPI** (documentation Swagger)
- **Maven** (build)
- **Docker**
- **Jenkins** (pipeline CI/CD)
- **SonarQube** (analyse de qualité de code)
- **Nexus** (dépôt d'artefacts)

## 📦 Fonctionnalités / Domaine métier

Le projet expose des API REST pour gérer :

- **Foyer** – le foyer universitaire lui-même
- **Bloc** – les blocs/bâtiments d'un foyer
- **Chambre** – les chambres d'un bloc (avec un `TypeChambre`)
- **Étudiant** – les étudiants résidents
- **Université** – les universités rattachées
- **Réservation** – les réservations de chambres par les étudiants

Le code est organisé en couches classiques :

```
src/main/java/tn/esprit/spring/
├── DAO/
│   ├── Entities/        # Entités JPA (Foyer, Bloc, Chambre, Etudiant, Universite, Reservation, TypeChambre)
│   └── Repositories/    # Repositories Spring Data JPA
├── Services/            # Interfaces + implémentations métier (par entité)
├── RestControllers/     # Contrôleurs REST exposant les API
├── AOP/                 # Aspects (programmation orientée aspect)
├── Schedular/           # Tâches planifiées (scheduling)
└── Config/              # Configuration (ex: SpringDoc/Swagger)
```

## 🚀 Démarrage rapide (local)

### Prérequis

- JDK 17
- Maven
- Une instance MySQL accessible

### Lancer l'application

```bash
git clone https://github.com/y189/pipeline_FoyerDevOps.git
cd pipeline_FoyerDevOps
mvn clean install
mvn spring-boot:run
```

L'application démarre sur le port **8086** avec le contexte `/Foyer` :

```
http://localhost:8086/Foyer
```

### Configuration

Le fichier `src/main/resources/application.properties` contient :

```properties
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=update
server.port=8086
server.servlet.context-path=/Foyer
```

> ⚠️ Les paramètres de connexion à la base de données (URL, utilisateur, mot de passe) sont à ajouter/adapter selon votre environnement (non versionnés dans ce fichier).

## 🐳 Docker

Le `Dockerfile` construit une image en téléchargeant le `.jar` déjà publié sur un dépôt **Nexus** (plutôt que de builder le jar dans l'image) :

```bash
docker build \
  --build-arg NEXUS_URL=<url_nexus> \
  --build-arg REPOSITORY=<repo_nexus> \
  --build-arg GROUP_ID=tn.esprit.spring \
  --build-arg ARTIFACT_ID=Foyer \
  --build-arg VERSION=1.5.1 \
  --build-arg NEXUS_USER=<user> \
  --build-arg NEXUS_PASS=<password> \
  -t yosrahb/backend-foyer:1.5.1 .

docker run -p 8086:8086 yosrahb/backend-foyer:1.5.1
```

## 🔁 Pipeline CI/CD (Jenkins)

Le `Jenkinsfile` définit un pipeline avec les étapes suivantes :

1. **Clone** – récupération du code source depuis GitHub
2. **Build JAR** – `mvn clean package` (tests ignorés à cette étape)
3. **Test** – exécution des tests unitaires (`mvn test`)
4. **SonarQube Analysis** – analyse statique de la qualité du code
5. **Upload JAR to Nexus** – publication de l'artefact `.jar` versionné sur Nexus
6. **Build Docker Image from Nexus** – construction de l'image Docker à partir du jar téléchargé depuis Nexus
7. **Push Docker Image to Docker Hub** – publication de l'image sur Docker Hub

### Prérequis Jenkins

Ce pipeline suppose que les éléments suivants sont configurés côté Jenkins :

- Un outil Maven nommé `MAVEN_HOME`
- Un serveur SonarQube nommé `SonarServer`
- Des identifiants Jenkins :
  - `nexus-credentials` (accès Nexus)
  - `docker-hub-credentials` (accès Docker Hub)

### Variables d'environnement du pipeline

| Variable | Description |
|---|---|
| `IMAGE_NAME` | Nom de l'image Docker (`yosrahb/backend-foyer`) |
| `IMAGE_VERSION` | Version de l'image / du jar (`1.5.1`) |
| `SONAR_HOST_URL` | URL du serveur SonarQube |
| `SONAR_PROJECT_KEY` | Clé du projet SonarQube |
| `NEXUS_URL` | URL du serveur Nexus |
| `NEXUS_REPOSITORY` | Dépôt Maven Nexus (`maven-releases`) |

## 🧪 Tests

```bash
mvn test
```

Les tests utilisent une base **H2** en mémoire.

## 📄 Licence

Projet académique (ESPRIT).
