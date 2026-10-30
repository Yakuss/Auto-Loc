# Documentation API - Atelier 5

## A. Endpoints

Tableau récapitulatif de tous les endpoints de l'API REST AutoLoc.

| Contrôleur | Verbe | Route | Service appelé | Succès | Erreurs |
|------------|-------|-------|----------------|--------|---------|
| **VehiculeController** |
| | GET | /api/vehicules | findAll() | 200 | — |
| | GET | /api/vehicules/{id} | findById(id) | 200 | 404 |
| | POST | /api/vehicules | create(vehicule) | 201 | 400 |
| | PUT | /api/vehicules/{id} | update(id, vehicule) | 200 | 404, 400 |
| | DELETE | /api/vehicules/{id} | deleteById(id) | 204 | 404 |
| **ClientController** |
| | GET | /api/clients | findAll() | 200 | — |
| | GET | /api/clients/{id} | findById(id) | 200 | 404 |
| | POST | /api/clients | create(client) | 201 | 400 |
| | PUT | /api/clients/{id} | update(id, client) | 200 | 404, 400 |
| | DELETE | /api/clients/{id} | deleteById(id) | 204 | 404 |
| **AgenceController** |
| | GET | /api/agences | findAll() | 200 | — |
| | GET | /api/agences/{id} | findById(id) | 200 | 404 |
| | POST | /api/agences | create(agence) | 201 | 400 |
| | PUT | /api/agences/{id} | update(id, agence) | 200 | 404, 400 |
| | DELETE | /api/agences/{id} | deleteById(id) | 204 | 404 |
| **ReservationController** |
| | GET | /api/reservations | findAll() | 200 | — |
| | GET | /api/reservations/{id} | findById(id) | 200 | 404 |
| | POST | /api/reservations | create(reservation) | 201 | 400 |
| | PUT | /api/reservations/{id} | update(id, reservation) | 200 | 404, 400 |
| | DELETE | /api/reservations/{id} | deleteById(id) | 204 | 404 |
| **ContratController** |
| | GET | /api/contrats | findAll() | 200 | — |
| | GET | /api/contrats/{id} | findById(id) | 200 | 404 |
| | POST | /api/contrats | create(contrat) | 201 | 400 |
| | PUT | /api/contrats/{id} | update(id, contrat) | 200 | 404, 400 |
| | DELETE | /api/contrats/{id} | deleteById(id) | 204 | 404 |
| **PaiementController** (Lecture seule) |
| | GET | /api/paiements | findAll() | 200 | — |
| | GET | /api/paiements/{id} | findById(id) | 200 | 404 |
| **MaintenanceController** |
| | GET | /api/maintenances | findAll() | 200 | — |
| | GET | /api/maintenances/{id} | findById(id) | 200 | 404 |
| | POST | /api/maintenances | create(maintenance) | 201 | 400 |
| | PUT | /api/maintenances/{id} | update(id, maintenance) | 200 | 404, 400 |
| | DELETE | /api/maintenances/{id} | deleteById(id) | 204 | 404 |
| **EmployeController** |
| | GET | /api/employes | findAll() | 200 | — |
| | GET | /api/employes/{id} | findById(id) | 200 | 404 |
| | POST | /api/employes | create(employe) | 201 | 400 |
| | PUT | /api/employes/{id} | update(id, employe) | 200 | 404, 400 |
| | DELETE | /api/employes/{id} | deleteById(id) | 204 | 404 |
| **EquipementController** |
| | GET | /api/equipements | findAll() | 200 | — |
| | GET | /api/equipements/{id} | findById(id) | 200 | 404 |
| | POST | /api/equipements | create(equipement) | 201 | 400 |
| | PUT | /api/equipements/{id} | update(id, equipement) | 200 | 404, 400 |
| | DELETE | /api/equipements/{id} | deleteById(id) | 204 | 404 |

### Notes sur les codes HTTP

- **200 OK** : Requête réussie (GET, PUT)
- **201 Created** : Ressource créée avec succès (POST)
- **204 No Content** : Suppression réussie (DELETE)
- **400 Bad Request** : Données invalides (validation métier échouée)
- **404 Not Found** : Ressource inexistante (ResourceNotFoundException)

---

## B. Annotations Jackson

### Association bidirectionnelle Contrat ↔ Paiement

**Problème :** Sans annotations, Jackson entre dans une boucle infinie lors de la sérialisation JSON car Contrat contient ses Paiements, et chaque Paiement référence son Contrat.

**Solution adoptée :** Composition forte avec cascade

```java
// Contrat.java (côté parent)
@OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference("contrat-paiements")
private Set<Paiement> paiements = new HashSet<>();

// Paiement.java (côté enfant)
@ManyToOne
@JsonBackReference("contrat-paiements")
private Contrat contrat;
```

**Justification :**
- `@JsonManagedReference` : Côté "gérant" de la relation, les paiements sont inclus dans le JSON du contrat
- `@JsonBackReference` : Côté "dos" de la relation, le contrat n'est PAS inclus dans le JSON du paiement (évite la boucle)
- Même nom de référence `"contrat-paiements"` des deux côtés pour lier les annotations

### Différence entre @JsonBackReference et @JsonIgnore

| Annotation | Comportement | Usage |
|------------|-------------|--------|
| **@JsonBackReference** | - Ignoré en **sérialisation** (GET)<br>- **Pris en compte** en désérialisation (POST/PUT) | Association bidirectionnelle gérée (composition) |
| **@JsonIgnore** | - Ignoré en sérialisation ET désérialisation<br>- Complètement exclu du JSON | Association qu'on ne veut jamais exposer/recevoir |

**Exemple concret :**
- Avec `@JsonBackReference` sur `Paiement.contrat` : On peut créer un contrat avec ses paiements en un seul POST
- Avec `@JsonIgnore` sur `Paiement.contrat` : Impossible de lier les paiements au contrat via JSON

**Autres associations du projet :**
- Pour les associations non-composites (Vehicule-Agence, Client-Reservation, etc.), on utilise `@JsonIgnore` côté inverse pour éviter la verbosité du JSON

---

## C. Postman et Swagger

### Rôles complémentaires

| Outil | Rôle principal | Cas d'usage |
|-------|----------------|-------------|
| **Postman** | **Test et validation** | - Tester l'API endpoint par endpoint<br>- Rejouer des requêtes<br>- Vérifier les codes HTTP et corps de réponse<br>- Scripts de tests automatisés<br>- Collections organisées par fonctionnalité |
| **Swagger UI** | **Documentation et exploration** | - Documentation générée automatiquement<br>- Explorer les endpoints disponibles<br>- Voir les schémas de données<br>- Test rapide "Try it out"<br>- Contrat d'API pour les consommateurs |

### Exemple concret rencontré dans l'atelier

**Postman** : Création d'un contrat avec ses paiements
```http
POST http://localhost:8080/api/contrats
Content-Type: application/json

{
  "dateSignature": "2026-11-02",
  "montantTotal": 480.00,
  "valide": true,
  "paiements": [
    { "montant": 150.00, "datePaiement": "2026-11-02", "modePaiement": "CARTE_BANCAIRE" },
    { "montant": 330.00, "datePaiement": "2026-11-06", "modePaiement": "VIREMENT" }
  ]
}
```
- **Utilisé pour** : Vérifier que la cascade PERSIST fonctionne (paiements créés automatiquement)
- **Vérification** : GET /api/paiements pour confirmer que les 2 paiements existent

**Swagger UI** : 
- **Utilisé pour** : Voir la liste complète des endpoints disponibles
- **Avantage** : Documentation auto-générée avec `@Operation`, `@ApiResponses`
- **Accès** : http://localhost:8080/api/swagger-ui.html

**Complémentarité** : Swagger pour découvrir l'API, Postman pour la tester en profondeur avec des scénarios complets.

---

## D. Anomalies SonarQube

### Anomalie 1 : Exposition directe des entités JPA

**Règle** : "Domain entities should not be used for external APIs"

**Explication** : Les contrôleurs exposent directement les entités JPA (Vehicule, Client, etc.) au lieu de DTOs (Data Transfer Objects).

**Problèmes** :
- Couplage fort entre la base de données et l'API
- Champs internes (relations lazy, métadonnées Hibernate) visibles
- Impossible de personnaliser la structure JSON sans modifier l'entité
- Risque de fuite de données sensibles

**Correction** : Introduire des DTOs (sera fait dans Chapitre 10)
```java
// Au lieu de
public ResponseEntity<Vehicule> createVehicule(@RequestBody Vehicule vehicule)

// Utiliser
public ResponseEntity<VehiculeDTO> createVehicule(@RequestBody CreateVehiculeDTO dto)
```

### Anomalie 2 : Constructeur non utilisé (injection par champ)

**Règle** : S6813 - "Avoid field dependency injection"

**Explication** : Si on avait utilisé `@Autowired` sur un champ au lieu de l'injection par constructeur, SonarQube le signalerait.

```java
// ❌ Mauvaise pratique
@Autowired
private IVehiculeService vehiculeService;

// ✅ Bonne pratique (ce que nous utilisons)
private final IVehiculeService vehiculeService;
// + @RequiredArgsConstructor
```

**Avantages de l'injection par constructeur** :
- Immutabilité (champs `final`)
- Testabilité (on peut passer des mocks)
- Détection des dépendances circulaires au démarrage
- Plus explicite

**Correction appliquée** : Utilisation systématique de `@RequiredArgsConstructor` (Lombok) avec champs `final`.

### Anomalie 3 : Imports inutilisés

**Règle** : "Unused imports should be removed"

**Explication** : Après refactoring, certains imports peuvent rester même s'ils ne sont plus utilisés.

**Exemple rencontré** :
```java
import org.springframework.http.HttpStatus; // ✅ Utilisé pour HttpStatus.CREATED
import org.springframework.web.bind.annotation.ResponseStatus; // ❌ Non utilisé (annotation retirée)
```

**Correction** : Suppression manuelle ou via IDE (IntelliJ : Ctrl+Alt+O).

---

## E. Questions de compréhension

### 1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?

**Composant** : `DispatcherServlet`

**Fonctionnement** :
1. Le DispatcherServlet intercepte toutes les requêtes HTTP
2. Il consulte le `HandlerMapping` pour trouver le contrôleur correspondant
3. Le HandlerMapping compare :
   - Le verbe HTTP (GET, POST, PUT, DELETE)
   - Le chemin de l'URL (/api/vehicules/{id})
   - Les annotations `@RequestMapping`, `@GetMapping`, etc.
4. Il retourne le contrôleur et la méthode à invoquer
5. Le DispatcherServlet exécute la méthode
6. Jackson convertit le résultat en JSON
7. Le DispatcherServlet renvoie la réponse HTTP au client

**Exemple** : 
- Requête : `GET /api/vehicules/1`
- Mapping trouvé : `VehiculeController.getVehiculeById(1)`

---

### 2. Pourquoi ContratController dépend-il de IContratService et non de ContratRepository ni de ContratServiceImpl ?

**Raisons** :

1. **Principe de séparation des responsabilités (SRP)** :
   - Contrôleur : Gestion HTTP (routes, codes de statut, JSON)
   - Service : Logique métier (validations, règles, transactions)
   - Repository : Accès aux données (SQL, JPA)

2. **Dépendance par interface (DIP - Dependency Inversion Principle)** :
   - Le contrôleur ne connaît que l'interface `IContratService`
   - L'implémentation concrète `ContratServiceImpl` peut changer sans toucher au contrôleur
   - Facilite les tests (on peut mocker `IContratService`)

3. **Injection de dépendances (IoC)** :
   - Spring injecte automatiquement `ContratServiceImpl` car c'est la seule implémentation de `IContratService`
   - Si plusieurs implémentations existent, on utilise `@Qualifier`

4. **Éviter le couplage direct au repository** :
   - Si le contrôleur appelait directement `ContratRepository`, il n'y aurait pas de couche pour les validations métier
   - Les transactions (@Transactional) sont gérées au niveau service

**Analogie** : Le contrôleur est comme un serveur dans un restaurant, il prend la commande (requête HTTP) et la transmet à la cuisine (service), il ne va pas directement au frigo (repository).

---

### 3. Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?

**POST → 201 Created** :
- Indique qu'une nouvelle ressource a été créée avec succès
- Par convention REST, le corps de la réponse contient la ressource créée (avec son ID généré)
- Alternative : 200 OK, mais moins précis

**DELETE → 204 No Content** :
- Indique que la suppression a réussi
- Pas de corps de réponse nécessaire (la ressource n'existe plus)
- Alternative : 200 OK avec un message, mais 204 est plus sémantique

**404 Not Found vs 500 Internal Server Error** :

| Code | Signification | Quand l'utiliser |
|------|---------------|------------------|
| 404 | Ressource inexistante | L'ID demandé n'existe pas en base → erreur **cliente** (le client a demandé quelque chose qui n'existe pas) |
| 500 | Erreur serveur | Problème technique (base inaccessible, NullPointerException) → erreur **serveur** (le serveur a un problème) |

**Dans notre API** :
```java
vehiculeService.findById(9999); // Lance ResourceNotFoundException
// GlobalExceptionHandler intercepte et retourne 404
```

C'est une erreur 404 car l'ID 9999 est syntaxiquement correct, mais n'existe pas en base. Ce n'est pas un bug du serveur, c'est une requête valide sur une ressource absente.

---

### 4. Pourquoi PaiementController n'expose-t-il que des GET ? Quelle réponse reçoit un POST /api/paiements ?

**Raison** : **Composition forte avec Contrat**

Les paiements sont gérés via les contrats (cascade = ALL) :
- `CascadeType.PERSIST` : Créer un contrat crée automatiquement ses paiements
- `CascadeType.REMOVE` : Supprimer un contrat supprime automatiquement ses paiements
- `orphanRemoval = true` : Un paiement sans contrat est automatiquement supprimé

**Workflow correct** :
```java
// ✅ Créer un contrat avec ses paiements
POST /api/contrats
{
  "dateSignature": "2026-11-02",
  "montantTotal": 480.00,
  "paiements": [...]
}
```

**Réponse à POST /api/paiements** :
```
405 Method Not Allowed
```

**Explication** : Spring détecte qu'il n'y a pas de méthode annotée `@PostMapping` dans `PaiementController`, donc il renvoie 405 (méthode HTTP non autorisée).

**Pourquoi cette conception ?** :
- Un paiement sans contrat n'a pas de sens métier
- Garantit la cohérence des données (montantTotal du contrat = somme des paiements)
- Simplifie l'API (un seul point d'entrée pour créer contrat + paiements)

---

### 5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi Contrat.paiements utilise-t-il @JsonManagedReference alors que Agence.vehicules utilise @JsonIgnore ?

**Risques de l'exposition directe des entités JPA** :

1. **Fuite de données sensibles** :
   - Tous les champs de l'entité sont exposés (même ceux qui devraient rester internes)
   - Impossible de masquer un champ sans modifier l'entité

2. **Couplage API/Base de données** :
   - Un changement de schéma casse l'API
   - Les relations JPA (lazy, eager) affectent le JSON

3. **Problèmes de sérialisation** :
   - Proxy Hibernate → `No serializer found for ByteBuddyInterceptor`
   - Relations bidirectionnelles → StackOverflowError

4. **Verbosité du JSON** :
   - Inclusion de champs techniques inutiles pour le client
   - Relations imbriquées à l'infini

5. **Sécurité** :
   - Mass assignment : un client peut modifier des champs qu'il ne devrait pas
   - Exemple : `{"id": 1, "isAdmin": true}` dans un POST

**Solution** : DTOs (Data Transfer Objects) - sera implémenté dans Chapitre 10.

---

**Pourquoi Contrat.paiements utilise @JsonManagedReference et Agence.vehicules utilise @JsonIgnore ?**

### Cas 1 : Contrat ↔ Paiement (Composition)

```java
@JsonManagedReference("contrat-paiements") // Côté Contrat
@JsonBackReference("contrat-paiements")     // Côté Paiement
```

**Contexte** :
- Relation de composition forte (un paiement ne peut exister sans contrat)
- Cascade ALL : les paiements sont créés/supprimés avec le contrat
- `orphanRemoval = true`

**Besoin métier** :
- On veut créer un contrat avec ses paiements en un seul POST
- Le JSON du contrat doit inclure ses paiements
- Le JSON du paiement ne doit PAS inclure le contrat (éviter la boucle)

**Résultat** :
```json
// GET /api/contrats/1
{
  "idContrat": 1,
  "montantTotal": 480.00,
  "paiements": [
    {"idPaiement": 1, "montant": 150.00}, // ✅ contrat non inclus
    {"idPaiement": 2, "montant": 330.00}
  ]
}
```

---

### Cas 2 : Agence ↔ Vehicule (Association)

```java
@JsonIgnore // Côté Agence sur la liste vehicules
// Pas d'annotation côté Vehicule.agence
```

**Contexte** :
- Relation d'association simple (un véhicule peut changer d'agence)
- Pas de cascade (on ne supprime pas les véhicules quand on supprime l'agence)

**Besoin métier** :
- On ne veut PAS que GET /api/agences retourne tous les véhicules de chaque agence (trop verbeux)
- Un véhicule doit pouvoir référencer son agence : `{"agence": {"id": 1}}`

**Résultat** :
```json
// GET /api/agences/1
{
  "idAgence": 1,
  "nom": "Auto-Loc Tunis"
  // ✅ vehicules non inclus (grâce à @JsonIgnore)
}

// GET /api/vehicules/1
{
  "idVehicule": 1,
  "immatriculation": "TUN-1234",
  "agence": {"idAgence": 1} // ✅ agence incluse
}
```

---

**Résumé de la stratégie** :

| Type de relation | Annotation | Raison |
|------------------|------------|--------|
| **Composition** (parent porte l'enfant) | `@JsonManagedReference` (parent)<br>`@JsonBackReference` (enfant) | Le parent inclut ses enfants dans le JSON, permet création en cascade |
| **Association** (référence simple) | `@JsonIgnore` (côté liste inverse) | Évite la verbosité, seul le côté propriétaire expose la relation |

---

## Conclusion

Atelier 5 accompli :
- ✅ 9 contrôleurs REST avec CRUD complet
- ✅ Documentation Swagger complète avec annotations
- ✅ Gestion des associations bidirectionnelles avec Jackson
- ✅ Configuration context-path et SpringDoc
- ✅ Tests disponibles via Postman et Swagger UI

**Prochaines étapes** : Introduction des DTOs (Chapitre 10) pour découpler l'API de la couche persistance.
