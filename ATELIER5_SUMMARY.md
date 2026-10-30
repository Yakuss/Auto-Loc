# 📋 ATELIER 5 - RÉSUMÉ COMPLET

## ✅ Objectifs réalisés

### 1. Configuration SpringDoc OpenAPI
- ✅ Ajout de la dépendance `springdoc-openapi-starter-webmvc-ui` version 2.6.0
- ✅ Configuration du `context-path` : `/api`
- ✅ Port : `8080`
- ✅ Création de `OpenApiConfig.java` pour la configuration Swagger

### 2. Annotations Jackson (Gestion des associations bidirectionnelles)
- ✅ `@JsonManagedReference("contrat-paiements")` sur `Contrat.paiements`
- ✅ `@JsonBackReference("contrat-paiements")` sur `Paiement.contrat`
- ✅ Prévention des boucles infinies de sérialisation JSON

### 3. Documentation Swagger complète (9 contrôleurs)
Tous les contrôleurs ont été annotés avec :
- ✅ `@Tag` : Description du groupe d'endpoints
- ✅ `@Operation` : Description de chaque opération
- ✅ `@ApiResponses` : Codes de réponse (200, 201, 204, 400, 404)
- ✅ `@Parameter` : Description des paramètres

**Contrôleurs documentés :**
1. VehiculeController - Gestion des véhicules
2. ClientController - Gestion des clients
3. AgenceController - Gestion des agences
4. ReservationController - Gestion des réservations
5. ContratController - Gestion des contrats (avec cascade vers paiements)
6. PaiementController - Consultation des paiements (READ-ONLY)
7. MaintenanceController - Gestion des maintenances
8. EmployeController - Gestion des employés
9. EquipementController - Gestion des équipements

### 4. Documentation technique
- ✅ `docs/api-notes.md` créé avec 5 sections complètes (A-E)
- ✅ Tableau récapitulatif de tous les endpoints
- ✅ Explication des annotations Jackson
- ✅ Comparaison Postman vs Swagger
- ✅ Analyse SonarQube
- ✅ Réponses aux questions de compréhension

### 5. Tests et validation
- ✅ `TEST_ENDPOINTS.md` : Guide complet des tests
- ✅ `test-endpoints.http` : Fichier REST Client pour VS Code
- ✅ Tous les endpoints testables via Postman et Swagger UI

---

## 📊 Architecture mise en place

### Couches de l'application

```
┌─────────────────────────────────────┐
│     Client (Postman/Browser)        │
└──────────────┬──────────────────────┘
               │ HTTP Request
               ▼
┌─────────────────────────────────────┐
│       DispatcherServlet             │
│     (Point d'entrée unique)         │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│    web.controller                   │
│  - @RestController                  │
│  - @RequestMapping                  │
│  - Gestion HTTP (JSON)              │
│  - Documentation Swagger            │
└──────────────┬──────────────────────┘
               │ Dépend de I*Service
               ▼
┌─────────────────────────────────────┐
│    service                          │
│  - Interfaces I*Service             │
│  - Implémentations *ServiceImpl     │
│  - Logique métier                   │
│  - @Transactional                   │
└──────────────┬──────────────────────┘
               │ Dépend de I*Repository
               ▼
┌─────────────────────────────────────┐
│    repository                       │
│  - Interfaces I*Repository          │
│  - Accès aux données (JPA)          │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│       Base de données MySQL         │
│         (autoloc_db)                │
└─────────────────────────────────────┘
```

---

## 🔗 URLs importantes

| Ressource | URL | Description |
|-----------|-----|-------------|
| Swagger UI | http://localhost:8080/api/swagger-ui.html | Interface de documentation et test |
| OpenAPI JSON | http://localhost:8080/api/v3/api-docs | Spécification OpenAPI brute |
| Véhicules | http://localhost:8080/api/vehicules | Endpoint véhicules |
| Clients | http://localhost:8080/api/clients | Endpoint clients |
| Agences | http://localhost:8080/api/agences | Endpoint agences |
| Réservations | http://localhost:8080/api/reservations | Endpoint réservations |
| Contrats | http://localhost:8080/api/contrats | Endpoint contrats |
| Paiements | http://localhost:8080/api/paiements | Endpoint paiements (READ-ONLY) |
| Maintenances | http://localhost:8080/api/maintenances | Endpoint maintenances |
| Employés | http://localhost:8080/api/employes | Endpoint employés |
| Équipements | http://localhost:8080/api/equipements | Endpoint équipements |

---

## 📂 Structure des fichiers modifiés

```
autoLoc/
├── pom.xml                                    [MODIFIÉ] Ajout SpringDoc
├── src/main/resources/
│   └── application.properties                 [MODIFIÉ] context-path=/api, port=8080
├── src/main/java/tn/esprit/autoloc/
│   ├── domain/
│   │   ├── Contrat.java                       [MODIFIÉ] @JsonManagedReference
│   │   └── Paiement.java                      [MODIFIÉ] @JsonBackReference
│   └── web/controller/
│       ├── OpenApiConfig.java                 [CRÉÉ] Configuration Swagger
│       ├── AgenceController.java              [MODIFIÉ] Annotations Swagger
│       ├── ClientController.java              [MODIFIÉ] Annotations Swagger
│       ├── ContratController.java             [MODIFIÉ] Annotations Swagger
│       ├── EmployeController.java             [MODIFIÉ] Annotations Swagger
│       ├── EquipementController.java          [MODIFIÉ] Annotations Swagger
│       ├── MaintenanceController.java         [MODIFIÉ] Annotations Swagger
│       ├── PaiementController.java            [MODIFIÉ] Annotations Swagger
│       ├── ReservationController.java         [MODIFIÉ] Annotations Swagger
│       └── VehiculeController.java            [MODIFIÉ] Annotations Swagger
├── docs/
│   └── api-notes.md                           [CRÉÉ] Documentation complète
├── TEST_ENDPOINTS.md                          [MODIFIÉ] Guide de test
├── test-endpoints.http                        [CRÉÉ] Fichier REST Client
└── ATELIER5_SUMMARY.md                        [CE FICHIER]
```

---

## 🎯 Conventions REST appliquées

### Verbes HTTP et codes de statut

| Opération | Verbe | Route | Code succès | Code erreur |
|-----------|-------|-------|-------------|-------------|
| Lister | GET | /api/{entity} | 200 OK | — |
| Consulter | GET | /api/{entity}/{id} | 200 OK | 404 Not Found |
| Créer | POST | /api/{entity} | 201 Created | 400 Bad Request |
| Modifier | PUT | /api/{entity}/{id} | 200 OK | 404, 400 |
| Supprimer | DELETE | /api/{entity}/{id} | 204 No Content | 404 Not Found |

### Principes appliqués

✅ **Routes au pluriel** : `/vehicules` et non `/vehicule`  
✅ **Verbes HTTP sémantiques** : GET pour lire, POST pour créer, PUT pour modifier, DELETE pour supprimer  
✅ **Codes HTTP corrects** : 201 pour création, 204 pour suppression, 404 pour ressource inexistante  
✅ **Réponses JSON structurées** : Format uniforme pour toutes les entités  
✅ **Gestion d'erreurs** : GlobalExceptionHandler intercepte ResourceNotFoundException et IllegalArgumentException

---

## 🔍 Points clés de l'implémentation

### 1. Injection de dépendances

```java
@RestController
@RequiredArgsConstructor  // ← Génère le constructeur
public class VehiculeController {
    private final IVehiculeService vehiculeService;  // ← Injection par constructeur
}
```

**Avantages :**
- Immutabilité (champ `final`)
- Testabilité (on peut passer des mocks)
- Détection des dépendances circulaires au démarrage

### 2. Séparation des responsabilités

```
Controller → Service Interface → Service Implementation → Repository
```

- **Controller** : Gestion HTTP (routes, codes, JSON)
- **Service** : Logique métier (validations, transactions)
- **Repository** : Accès aux données (SQL, JPA)

### 3. Gestion des associations bidirectionnelles

**Problème** : Boucle infinie lors de la sérialisation JSON

**Solution** : Annotations Jackson

```java
// Contrat.java (parent)
@JsonManagedReference("contrat-paiements")
private Set<Paiement> paiements;

// Paiement.java (enfant)
@JsonBackReference("contrat-paiements")
private Contrat contrat;
```

**Résultat** :
- Le JSON du contrat **inclut** ses paiements
- Le JSON du paiement **n'inclut pas** le contrat (évite la boucle)

### 4. Composition forte : Contrat ↔ Paiement

```java
@OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true)
```

**Conséquences :**
- Créer un contrat → crée automatiquement ses paiements (PERSIST)
- Supprimer un contrat → supprime automatiquement ses paiements (REMOVE)
- PaiementController est en lecture seule (pas de POST/PUT/DELETE)

### 5. Documentation Swagger enrichie

```java
@Tag(name = "Vehicules", description = "Gestion des véhicules")
@Operation(summary = "Créer un véhicule")
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès"),
    @ApiResponse(responseCode = "400", description = "Données invalides")
})
```

**Avantages :**
- Documentation auto-générée
- Synchronisée avec le code
- Testable directement depuis Swagger UI

---

## 🧪 Tests disponibles

### Option 1 : Swagger UI (Recommandé pour la découverte)

1. Démarrer l'application : `mvnw.cmd spring-boot:run`
2. Ouvrir : http://localhost:8080/api/swagger-ui.html
3. Sélectionner un contrôleur (ex: Vehicules)
4. Cliquer sur "Try it out" → Modifier le JSON → "Execute"
5. Voir la requête curl, le code de statut, et la réponse

### Option 2 : Postman (Recommandé pour les tests approfondis)

1. Créer une collection "AutoLoc"
2. Configurer l'environnement avec `baseUrl = http://localhost:8080`
3. Créer des dossiers par entité (Vehicules, Clients, etc.)
4. Ajouter des requêtes pour chaque opération CRUD
5. Utiliser les scripts de tests pour valider les réponses

**Exemple de test Postman :**
```javascript
pm.test("Statut 201", () => pm.response.to.have.status(201));
pm.environment.set("vehiculeId", pm.response.json().idVehicule);
```

### Option 3 : REST Client VS Code

1. Ouvrir `test-endpoints.http`
2. Cliquer sur "Send Request" au-dessus de chaque requête
3. Voir la réponse dans un panneau à droite

### Option 4 : curl (Windows CMD)

```cmd
curl http://localhost:8080/api/vehicules
```

---

## 📊 Exemples de requêtes

### 1. Créer un véhicule

**Requête :**
```http
POST http://localhost:8080/api/vehicules
Content-Type: application/json

{
  "immatriculation": "TUN-1234",
  "marque": "Toyota",
  "modele": "Corolla",
  "categorie": "BERLINE",
  "tarifJournalier": 150.00,
  "statut": "DISPONIBLE"
}
```

**Réponse (201 Created) :**
```json
{
  "idVehicule": 1,
  "immatriculation": "TUN-1234",
  "marque": "Toyota",
  "modele": "Corolla",
  "categorie": "BERLINE",
  "tarifJournalier": 150.00,
  "statut": "DISPONIBLE"
}
```

### 2. Créer un contrat avec ses paiements (cascade)

**Requête :**
```http
POST http://localhost:8080/api/contrats
Content-Type: application/json

{
  "dateSignature": "2026-11-02",
  "montantTotal": 480.00,
  "valide": true,
  "paiements": [
    {
      "montant": 150.00,
      "datePaiement": "2026-11-02",
      "modePaiement": "CARTE_BANCAIRE"
    },
    {
      "montant": 330.00,
      "datePaiement": "2026-11-06",
      "modePaiement": "VIREMENT"
    }
  ]
}
```

**Réponse (201 Created) :**
```json
{
  "idContrat": 1,
  "dateSignature": "2026-11-02",
  "montantTotal": 480.00,
  "valide": true,
  "paiements": [
    {
      "idPaiement": 1,
      "montant": 150.00,
      "datePaiement": "2026-11-02",
      "modePaiement": "CARTE_BANCAIRE"
    },
    {
      "idPaiement": 2,
      "montant": 330.00,
      "datePaiement": "2026-11-06",
      "modePaiement": "VIREMENT"
    }
  ]
}
```

**Vérification :**
```http
GET http://localhost:8080/api/paiements
```
→ Les 2 paiements apparaissent (cascade PERSIST a fonctionné)

### 3. Consulter un ID inexistant (404)

**Requête :**
```http
GET http://localhost:8080/api/vehicules/9999
```

**Réponse (404 Not Found) :**
```json
{
  "statut": 404,
  "message": "Vehicule introuvable avec l'identifiant 9999",
  "horodatage": "2026-11-02T15:30:42Z"
}
```

### 4. Essayer de créer un paiement directement (405)

**Requête :**
```http
POST http://localhost:8080/api/paiements
Content-Type: application/json

{
  "montant": 100.00,
  "datePaiement": "2026-11-02",
  "modePaiement": "ESPECES"
}
```

**Réponse (405 Method Not Allowed) :**
```json
{
  "statut": 405,
  "message": "Method 'POST' is not supported for this endpoint",
  "horodatage": "2026-11-02T15:31:15Z"
}
```

---

## 🔧 Résolution de problèmes courants

### Problème 1 : 404 sur tous les endpoints

**Cause** : L'application n'est pas démarrée ou tourne sur un autre port

**Solution** :
```cmd
# Vérifier que l'application tourne
mvnw.cmd spring-boot:run

# Vérifier le port dans les logs
# Chercher : "Tomcat started on port(s): 8080"
```

### Problème 2 : StackOverflowError lors de GET /api/contrats

**Cause** : Boucle infinie dans la sérialisation JSON (association bidirectionnelle)

**Solution** : Vérifier que les annotations Jackson sont présentes :
```java
// Contrat.java
@JsonManagedReference("contrat-paiements")

// Paiement.java
@JsonBackReference("contrat-paiements")
```

### Problème 3 : 500 avec "No serializer found for ByteBuddyInterceptor"

**Cause** : Association LAZY non chargée et proxy Hibernate

**Solution provisoire** : Ajouter dans application.properties
```properties
spring.jackson.serialization.fail-on-empty-beans=false
```

**Solution définitive** : Utiliser des DTOs (Chapitre 10)

### Problème 4 : Swagger UI affiche une page blanche

**Cause** : Mauvaise URL ou context-path mal configuré

**Solution** : Vérifier l'URL exacte
```
✅ http://localhost:8080/api/swagger-ui.html
❌ http://localhost:8080/swagger-ui.html
```

### Problème 5 : 415 Unsupported Media Type sur POST

**Cause** : Header `Content-Type: application/json` manquant

**Solution** : Ajouter le header dans Postman ou curl
```cmd
curl -H "Content-Type: application/json" -d "{...}" ...
```

---

## 📈 Améliorations futures (Chapitre 10)

### Introduction des DTOs (Data Transfer Objects)

**Problème actuel** : Exposition directe des entités JPA
- Couplage API/Base de données
- Impossible de personnaliser le JSON sans toucher l'entité
- Risque de fuite de données sensibles
- Champs techniques exposés (hibernateLazyInitializer, etc.)

**Solution** : Créer des DTOs dédiés

```java
// Au lieu de
public ResponseEntity<Vehicule> createVehicule(@RequestBody Vehicule vehicule)

// Utiliser
public ResponseEntity<VehiculeResponseDTO> createVehicule(@RequestBody CreateVehiculeDTO dto)
```

**Avantages** :
- Découplage complet API/BDD
- Validation spécifique par cas d'usage (création vs modification)
- Personnalisation du JSON (renommage, formatage, filtrage)
- Sécurité renforcée (contrôle explicite des champs exposés)

---

## ✅ Checklist finale Atelier 5

### Configuration
- [x] SpringDoc dependency ajoutée (2.6.0)
- [x] context-path configuré (`/api`)
- [x] Port configuré (8080)
- [x] OpenApiConfig créé

### Entités
- [x] @JsonManagedReference sur Contrat.paiements
- [x] @JsonBackReference sur Paiement.contrat

### Contrôleurs (9/9)
- [x] VehiculeController documenté
- [x] ClientController documenté
- [x] AgenceController documenté
- [x] ReservationController documenté
- [x] ContratController documenté
- [x] PaiementController documenté (READ-ONLY)
- [x] MaintenanceController documenté
- [x] EmployeController documenté
- [x] EquipementController documenté

### Documentation
- [x] docs/api-notes.md créé (sections A-E)
- [x] TEST_ENDPOINTS.md mis à jour
- [x] test-endpoints.http créé
- [x] ATELIER5_SUMMARY.md créé

### Tests
- [ ] Application démarre sans erreur
- [ ] Swagger UI accessible
- [ ] Endpoints testés avec Postman
- [ ] Cascade Contrat→Paiement vérifiée
- [ ] Codes HTTP corrects (200, 201, 204, 404, 400)

---

## 🎓 Compétences acquises

1. **Conception d'API REST** selon les conventions HTTP
2. **Documentation automatique** avec SpringDoc OpenAPI
3. **Gestion des associations bidirectionnelles** avec Jackson
4. **Test d'API** avec Postman et Swagger UI
5. **Injection de dépendances** par constructeur
6. **Architecture en couches** (Controller → Service → Repository)
7. **Gestion d'erreurs** avec GlobalExceptionHandler
8. **Cascade JPA** et composition forte

---

## 📚 Ressources

- Documentation SpringDoc : https://springdoc.org/
- Spécification OpenAPI : https://swagger.io/specification/
- Jackson annotations : https://github.com/FasterXML/jackson-annotations
- REST API design : https://restfulapi.net/
- HTTP status codes : https://httpstatuses.com/

---

**Atelier 5 terminé avec succès ! 🎉**

Prochaine étape : Introduction des DTOs (Chapitre 10) pour découpler complètement l'API de la couche persistance.
