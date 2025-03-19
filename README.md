# Gestion de Comptes Bancaires - Architecture Hexagonale et DDD

## Description
Ce projet implémente une application de gestion de comptes bancaires en suivant l'architecture hexagonale, Domain-Driven Design (DDD) et Test driven development (TDD). Il prend en charge les fonctionnalités suivantes :

- Création et gestion de comptes
- Dépôt et retrait d'argent
- Historique des transactions
- Génération de relevés bancaires
- API REST pour interagir avec le système
- Documentation Swagger pour tester l'API
- Interface en ligne de commande (CLI) pour exécuter des opérations bancaires

## Technologies Utilisées
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (en développement)
- JUnit 5 & Mockito (tests)
- Maven
- Swagger (Documentation API)
- CLI avec `CommandLineRunner`

## Architecture

### Couches du projet :
1. **Domain (Cœur Métier)**
    - Contient les entités métier (`Account`, `Operation`, `OperationHistory`).
    - Définit les interfaces de Repository (`AccountRepository`, `OperationRepository`, `OperationHistoryRepository`).
    - Implémente les services métier (`AccountService`, `StatementService`).

2. **Infrastructure (Adaptateurs & Persistance)**
    - Contient les implémentations des repositories (`AccountRepositoryImpl`, `OperationRepositoryImpl`, `OperationHistoryRepositoryImpl`).
    - Stocke les entités JPA (`AccountEntity`, `OperationEntity`, `OperationHistoryEntity`).
    - Convertit les entités via des Mappers.

3. **Application (Interfaces Utilisateurs)**
    - Fournit une API REST (`AccountController`, `StatementController`, `CliController`).
    - Gère les requêtes utilisateur et les réponses DTO.
    - Fournit une interface en ligne de commande (`CliOperationController`) pour effectuer des transactions.

## Installation et Exécution

### 1. Cloner le projet
```sh
git clone https://github.com/votre-repo/bank-account.git
cd bank-account/backend
```

### 2. Construire et exécuter l'application
```sh
mvn clean install
mvn spring-boot:run
```

### 3. Accéder à l'API REST
L'application expose des endpoints REST :

Récupérer un compte :
```sh
GET /api/account/{accountId}
```

Déposer de l'argent :
```sh
POST /api/account/{accountId}/deposit?amount=100.00
```

Retirer de l'argent :
```sh
POST /api/account/{accountId}/withdraw?amount=50.00
```

Récupérer le relevé bancaire :
```sh
GET /api/statement/{accountId}
```

## Documentation API avec Swagger

Swagger est intégré dans l'application pour faciliter la documentation et le test des endpoints REST.
### Accéder à Swagger UI :
```sh
http://localhost:8080/swagger-ui/index.html
```

## Utilisation de l'Interface en Ligne de Commande (CLI)

### Comment fonctionne la CLI
L'application inclut une CLI interactive permettant aux utilisateurs de :
- Déposer de l'argent sur un compte
- Retirer de l'argent d'un compte
- Consulter les relevés de compte
- Quitter la CLI

La CLI démarre automatiquement au lancement de l'application. Elle peut également être déclenchée manuellement via une API REST.

### Lancer la CLI manuellement
Par défaut, la CLI démarre automatiquement lorsque l'application est lancée. Cependant, il est possible de la démarrer manuellement.

#### 1. Exécution via le terminal
```sh
mvn spring-boot:run
```
Cela démarre la CLI en mode interactif.

#### 1.  Exécution via l'API REST
Pour démarrer la CLI depuis l'API REST, envoyez une requête POST :
```sh
curl -X POST http://localhost:8080/api/cli/start
```
### Lancer la CLI manuellement

Lorsque la CLI démarre, le menu suivant apparaît :
```sh
===== Menu Banque CLI =====
Comptes de test disponibles :
🔹 ID du compte : 1001 | Solde : 500.00
🔹 ID du compte : 2002 | Solde : 800.00

1. Déposer de l'argent
2. Retirer de l'argent
3. Afficher le relevé de compte
4. Quitter
Votre choix :
```

## DDD et Bonnes Pratiques Respectées
-  Séparation des responsabilités : Un service ne gère qu'une seule responsabilité.
-  Hexagonal Architecture : La couche métier est indépendante des technologies utilisées.
-  Test-Driven Development (TDD) : Implémentation avec des tests unitaires en amont.
-  SOLID Principles : Code propre, évolutif et maintenable.

## Idées d'améliorations futures du projet

### 1. Amélioration de l'interface CLI
- **Ajout de couleurs et d'un affichage amélioré** pour une meilleure lisibilité.
- **Ajout d'une confirmation avant exécution** des transactions (ex : "Êtes-vous sûr ? (O/N)").
- **Possibilité de naviguer dans l'historique des opérations** avec des commandes supplémentaires.
- **Support des alias de commandes** (ex : `d` pour dépôt, `r` pour retrait, `s` pour relevé).
- **Gestion des erreurs avancée** avec affichage des solutions possibles.

### 2. Sécurité et authentification
- **Ajout d'une authentification utilisateur** via un login/mot de passe pour accéder aux comptes.
- **Mise en place d'un chiffrement des données sensibles** (ex : mot de passe hashé en base de données).
- **Intégration d'une authentification via JWT** pour sécuriser les API REST.
- **Journalisation des transactions et tentatives d'accès non autorisées**.

### 3. Optimisation de la persistance des données
- **Passage à une base de données SQL robuste** (PostgreSQL, MySQL) au lieu de H2.
- **Mise en place de la pagination pour l'historique des transactions**.
- **Ajout d'un cache avec Redis** pour améliorer la performance des requêtes.
- **Gestion des transactions bancaires avec ACID** pour garantir la cohérence des opérations.

### 4. Développement d'une API REST complète
- **Ajout d'un endpoint pour créer des comptes** et non seulement les tester en base.
- **Mise en place de contrôles d'accès avec des rôles utilisateurs** (ex : client, admin).
- **Documentation Swagger améliorée** avec des exemples de requêtes.
- **Ajout d'un webhook pour notifier les utilisateurs** en cas d'opérations importantes.

### 5. Ajout d'une interface web et mobile
- **Développement d'un frontend Angular ou React** pour interagir avec l'API REST.
- **Création d'une application mobile Flutter ou React Native** pour gérer les comptes en mobilité.
- **Intégration de notifications push** pour informer les utilisateurs des mouvements bancaires.

### 6. Gestion avancée des transactions
- **Ajout de virements entre comptes** avec validation sécurisée.
- **Ajout d'un système de limites quotidiennes et alertes en cas de dépassement**.
- **Mise en place d'un mécanisme d'annulation de transaction en cas d'erreur**.

### 7. Intégration de paiements et services tiers
- **Ajout du support des paiements en ligne** (Stripe, PayPal, API bancaire).
- **Connexion avec un service externe d’analyse financière** pour générer des rapports.
- **Ajout d'un système de catégorisation des transactions** pour mieux gérer les finances.

### 8. Tests et robustesse du système
- **Augmentation de la couverture des tests à 90%** avec plus de tests d'intégration.
- **Ajout de tests de charge pour évaluer la performance sous forte utilisation**.
- **Mise en place d'un environnement CI/CD** avec des pipelines automatisés (GitHub Actions, GitLab CI).

### 9. Déploiement et scalabilité
- **Déploiement sur le cloud (AWS, GCP, Azure)** avec une architecture scalable.
- **Utilisation de Kubernetes pour gérer les containers Docker**.
- **Surveillance et alertes via Prometheus et Grafana** pour le monitoring en production.

### 10. Expérience utilisateur et personnalisation
- **Ajout d'un chatbot pour aider les utilisateurs via la CLI**.
- **Personnalisation des comptes avec des préférences utilisateur** (ex : notifications, langue).
- **Export des relevés bancaires au format PDF ou CSV**.
- **Ajout d’un mode hors ligne** pour certaines fonctionnalités.

## Auteurs
-  Youssef MASSAOUDI - Développeur principal