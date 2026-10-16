# Atelier 4 - Notes sur IoC, DI et la couche Service

## A. Modes d'injection comparés

| Critère | Constructeur | Attribut |
|---------|--------------|----------|
| **Champ final possible ?** | ✅ Oui | ❌ Non |
| **Dépendance visible dans la signature de la classe ?** | ✅ Oui (dans le constructeur) | ❌ Non (masquée dans le champ privé) |
| **Classe utilisable avec `new` hors conteneur sans dépendance `null` ?** | ✅ Oui (on doit fournir les dépendances) | ❌ Non (les champs restent `null`) |
| **Signalé par SonarQube for IDE ?** | ✅ Non (bonne pratique) | ⚠️ Oui (règle S6813 : Field injection should not be used) |

### Conclusion
L'injection par constructeur est recommandée car elle :
- Garantit l'immutabilité (`final`)
- Rend les dépendances explicites
- Permet l'instanciation hors conteneur
- Facilite les tests unitaires

---

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---------|----------------------|------|---------------|
| ContratServiceImpl | IContratRepository | Constructeur | Dépendance obligatoire, champ `final`, injection explicite et testable |
| ClientServiceImpl | IClientRepository | Constructeur | Dépendance obligatoire, immutabilité garantie, facilite les tests |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Respect de la bonne pratique, évite l'injection par attribut signalée par SonarLint |
| AgenceServiceImpl | IAgenceRepository | Constructeur | Champ `final`, rend le service toujours valide après instanciation |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Dépendance explicite dans la signature, testabilité améliorée |
| ReservationServiceImpl | IReservationRepository | Constructeur | Immutabilité, facilite le remplacement de l'implémentation pour les tests |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Convention du projet, utilisation de `@RequiredArgsConstructor` de Lombok |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Garantit qu'aucune méthode ne sera appelée avec un repository `null` |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Lecture seule mais même pattern pour cohérence architecturale |

---

## C. Messages d'erreur du conteneur Spring

### Message 1 : NoSuchBeanDefinitionException
```
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.ContratController
required a bean of type 'tn.esprit.autoloc.service.IContratService'
that could not be found.
```

**Causes probables côté ContratServiceImpl :**
1. **Annotation `@Service` oubliée** : La classe n'est pas détectée par le scan de composants
2. **Classe hors du package de scan** : `ContratServiceImpl` n'est pas dans `tn.esprit.autoloc` ou ses sous-packages

**Correction :**
- Ajouter `@Service` sur `ContratServiceImpl`
- Vérifier que la classe est dans `tn.esprit.autoloc.service`

---

### Message 2 : NoUniqueBeanDefinitionException
```
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.NotificationController
required a single bean, but 2 were found:
 - emailNotificateur
 - smsNotificateur
```

**Cause :** Deux beans du même type existent, Spring ne sait pas lequel injecter.

**Corrections possibles :**

**Option 1 : Utiliser `@Primary`** (sur la classe du bean préféré)
```java
@Service
@Primary
public class EmailNotificateur implements INotificateur {
    // ...
}
```

**Option 2 : Utiliser `@Qualifier`** (sur le paramètre du constructeur)
```java
@RestController
@RequiredArgsConstructor
public class NotificationController {
    @Qualifier("emailNotificateur")
    private final INotificateur notificateur;
}
```

---

### Message 3 : Dépendance circulaire
```
The dependencies of some of the beans in the application context form a cycle:
 clientServiceImpl
 | ^
 v |
 reservationServiceImpl
```

**Pourquoi l'injection par constructeur rend ce cycle impossible à contourner ?**

Avec l'injection par constructeur :
1. Pour créer `ClientServiceImpl`, Spring a besoin de `ReservationServiceImpl`
2. Pour créer `ReservationServiceImpl`, Spring a besoin de `ClientServiceImpl`
3. Impossible de créer l'un sans l'autre → **deadlock**

Avec l'injection par setter/attribut, Spring pourrait créer les deux objets puis injecter les dépendances, mais cela masquerait un problème de conception.

**Piste de conception pour supprimer le cycle :**
- **Extraire la responsabilité commune** dans un troisième service
- Exemple : Créer `ReservationManagerService` qui gère la logique partagée
- Les deux services dépendraient de ce nouveau service au lieu de se référencer mutuellement

---

## D. Anomalies SonarQube for IDE

| Anomalie | Règle / Explication | Correction apportée |
|----------|---------------------|---------------------|
| Injection par attribut avec `@Autowired` | **S6813** : *Field injection should not be used*. L'injection par champ masque les dépendances, empêche l'utilisation de `final`, et rend les tests difficiles. | Remplacement par injection par constructeur avec `@RequiredArgsConstructor` et champ `final` |
| Lever une exception générique `RuntimeException` | **S112** : *Generic exceptions should never be thrown*. Les exceptions génériques ne donnent pas d'information sur la nature de l'erreur. | Création et utilisation de `ResourceNotFoundException` qui hérite de `RuntimeException` avec un message explicite |
| Import inutilisé | **S1128** : *Unused imports should be removed*. Les imports non utilisés polluent le code et créent de la confusion. | Suppression des imports `java.util.Optional` et `long` dans les interfaces de service après refactoring |

---

## E. Questions de compréhension

### 1. Où est le `new` de `ContratServiceImpl` et qui l'exécute ?

Le `new ContratServiceImpl(...)` n'est jamais écrit explicitement dans notre code. C'est le **conteneur Spring IoC** (ApplicationContext) qui l'exécute automatiquement au démarrage de l'application, grâce à :
- L'annotation `@Service` qui déclare la classe comme bean
- Le scan de composants activé par `@SpringBootApplication`

---

### 2. Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?

**Raisons :**
- **Principe d'inversion de dépendances (DIP)** : dépendre d'abstractions, pas d'implémentations concrètes
- **Testabilité** : on peut facilement fournir un mock ou un stub de `IContratService` pour les tests
- **Flexibilité** : on peut changer l'implémentation (ex: `ContratServiceCacheImpl`) sans modifier le contrôleur
- **Couplage faible** : le contrôleur ne connaît que le contrat (interface), pas les détails d'implémentation

---

### 3. Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.

**Raison :** Un service singleton est **partagé par tous les threads** concurrents. Si on stocke un état mutable, plusieurs requêtes HTTP peuvent le modifier simultanément → **race condition**.

**Exemple de bug :**
```java
@Service
public class ContratServiceImpl {
    private Contrat contratCourant; // ❌ État mutable

    public void traiterContrat(Long id) {
        this.contratCourant = findById(id);  // Thread 1 exécute
        // ... traitement long ...           // Thread 2 écrase contratCourant
        save(this.contratCourant);           // Thread 1 sauvegarde le mauvais contrat !
    }
}
```

**Conséquence :** Thread 1 pourrait sauvegarder les données du Thread 2, causant une corruption de données.

**Solution :** Ne jamais stocker d'état mutable ; utiliser uniquement des paramètres de méthode et variables locales.

---

### 4. Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` au lieu de lui passer directement l'objet reçu ?

**Raisons :**

1. **Éviter l'écrasement complet :** Si on passe directement l'objet reçu à `save()`avec un ID renseigné, JPA déclenche un `merge` qui va :
   - Écraser **tous les champs** (même ceux non envoyés dans la requête → `null`)
   - Potentiellement vider les collections avec `orphanRemoval = true`

2. **Mise à jour partielle :** En chargeant l'existant et en recopiant uniquement les champs fournis, on permet une mise à jour partielle (PATCH) sans perdre les données non modifiées.

3. **Préserver les relations :** Pour un `Contrat`, on évite de toucher à la collection `paiements` qui doit être gérée explicitement.

**Exemple :**
```java
// ❌ Mauvaise approche
public Contrat update(Long id, Contrat contrat) {
    contrat.setId(id);
    return repository.save(contrat); // Écrase tout !
}

// ✅ Bonne approche
public Contrat update(Long id, Contrat contrat) {
    Contrat existant = findById(id);
    if (contrat.getMontantTotal() != null) {
        existant.setMontantTotal(contrat.getMontantTotal());
    }
    return repository.save(existant); // Met à jour uniquement les champs modifiés
}
```

---

### 5. Questions complémentaires

#### Quelle différence entre `@Component` et `@Bean` ?

| Aspect | `@Component` | `@Bean` |
|--------|--------------|---------|
| **Cible** | Annotation de classe | Annotation de méthode dans une classe `@Configuration` |
| **Détection** | Automatique par le scan de composants | Déclaration explicite |
| **Usage** | Classes que **nous écrivons** | Classes de **bibliothèques externes** ou construction complexe |
| **Exemple** | `@Component`, `@Service`, `@Repository`, `@Controller` | Configuration de `DataSource`, `RestTemplate`, etc. |

**Exemple :**
```java
// @Component : nos classes
@Service
public class ClientServiceImpl { }

// @Bean : classes externes ou configuration spéciale
@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

---

#### Entre `@Primary` et `@Qualifier` ?

| Annotation | Placement | Effet |
|-----------|-----------|-------|
| **`@Primary`** | Sur la **classe** du bean | Définit le bean par **défaut** quand plusieurs candidats existent |
| **`@Qualifier`** | Sur le **paramètre** ou le champ | Sélectionne **explicitement** un bean par son nom |

**Usage :**
- `@Primary` : Quand on a un choix par défaut clair (ex: base de données principale)
- `@Qualifier` : Quand on veut contrôler le choix à chaque point d'injection

---

#### Pourquoi `IPaiementService` n'expose-t-il ni création, ni modification, ni suppression ?

**Raison : Modèle de composition forte (Contrat ↔ Paiement)**

- Un `Paiement` **n'existe que dans le contexte d'un `Contrat`**
- La relation est annotée avec `cascade = CascadeType.ALL` et `orphanRemoval = true`
- Les paiements sont **créés, modifiés et supprimés via le Contrat parent**

**Règle métier :**
```java
// ❌ Interdire ceci
Paiement p = new Paiement();
paiementService.create(p); // Incohérent : à quel contrat appartient-il ?

// ✅ Passer par le Contrat
Contrat contrat = contratService.findById(1L);
Paiement paiement = new Paiement();
paiement.setContrat(contrat);
contrat.getPaiements().add(paiement);
contratService.update(contrat.getId(), contrat); // Cascade sauvegarde le paiement
```

`IPaiementService` ne propose donc que **lecture** (`findById`, `findAll`) pour consulter les paiements existants.

---

## Résumé des bonnes pratiques appliquées

✅ **Injection par constructeur** pour toutes les dépendances  
✅ **Champs `final`** pour garantir l'immutabilité  
✅ **Interfaces** pour les dépendances (IRepository → Service, IService → Controller)  
✅ **`@RequiredArgsConstructor`** de Lombok pour générer les constructeurs  
✅ **ResourceNotFoundException** au lieu d'exceptions génériques  
✅ **Services sans état** (singleton-safe)  
✅ **Validation métier** dans les services (dates, montants négatifs, champs obligatoires)  
✅ **Mise à jour par chargement + recopie** pour éviter l'écrasement complet  
✅ **Vérification d'existence** avant suppression  
✅ **Cascade et orphanRemoval** respectés (Paiement lecture seule)  

---

**Atelier 4 complété** ✨  
Architecture multicouche complète : **Domain → Repository → Service → Controller**
