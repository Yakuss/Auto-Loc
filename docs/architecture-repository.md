# Architecture de la couche Repository - AutoLoc

## Hiérarchie Spring Data JPA

```
Repository<T, ID>                    [interface marqueur, 0 méthode]
    │
    ├─── CrudRepository<T, ID>       [CRUD de base, Iterable]
    │       │
    │       └─── ListCrudRepository<T, ID>  [CRUD de base, List]
    │
    └─── PagingAndSortingRepository<T, ID>  [Tri + Pagination, Iterable]
            │
            └─── ListPagingAndSortingRepository<T, ID>  [Tri + Pagination, List]


JpaRepository<T, ID> = ListCrudRepository 
                     + ListPagingAndSortingRepository 
                     + QueryByExampleExecutor
                     + méthodes JPA (flush, batch...)
```

## Mapping Entités ↔ Repositories

```
┌─────────────────────────────────────────────────────────────────┐
│                     COUCHE DOMAIN                                │
│  (9 entités JPA avec annotations Jakarta Persistence)           │
├─────────────────────────────────────────────────────────────────┤
│  Agence  │  Vehicule  │  Employe  │  Client  │  Reservation     │
│  Contrat │  Paiement  │  Equipement │  Maintenance               │
└─────────────────────────────────────────────────────────────────┘
                              ↕
                    [Spring Data JPA]
                  (génération automatique 
                   des implémentations)
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                  COUCHE REPOSITORY                               │
│         (9 interfaces étendant JpaRepository<T, Long>)          │
├─────────────────────────────────────────────────────────────────┤
│  IAgenceRepository      →  Agence                                │
│  IVehiculeRepository    →  Vehicule                              │
│  IEmployeRepository     →  Employe                               │
│  IClientRepository      →  Client                                │
│  IReservationRepository →  Reservation                           │
│  IContratRepository     →  Contrat                               │
│  IPaiementRepository    →  Paiement                              │
│  IEquipementRepository  →  Equipement                            │
│  IMaintenanceRepository →  Maintenance                           │
└─────────────────────────────────────────────────────────────────┘
                              ↕
                        [Hibernate]
                   (implémentation JPA)
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                    BASE DE DONNÉES                               │
│                    MySQL - autoloc_db                            │
└─────────────────────────────────────────────────────────────────┘
```

## Relations entre entités

```
Agence
  │
  ├─ OneToMany → Employe
  └─ OneToMany → Vehicule
                    │
                    ├─ ManyToOne → Agence
                    ├─ OneToMany → Reservation
                    ├─ ManyToMany → Equipement
                    └─ OneToMany → Maintenance

Client
  │
  └─ OneToMany → Reservation
                    │
                    ├─ ManyToOne → Client
                    ├─ ManyToOne → Vehicule
                    └─ OneToOne → Contrat
                                    │
                                    ├─ OneToOne → Reservation
                                    └─ OneToMany → Paiement (cascade ALL, orphanRemoval)
```

## Flux de données - Exemple : Sauvegarde d'un Contrat

```
[Code métier]
    │
    │  contratRepository.save(contrat)
    ↓
[IContratRepository]
    │  (interface Spring Data JPA)
    ↓
[SimpleJpaRepository] ← proxy généré par Spring Data
    │
    │  entityManager.persist(contrat)  si ID = null
    │  ou
    │  entityManager.merge(contrat)    si ID != null
    ↓
[Hibernate]
    │
    │  • Dirty checking
    │  • Génération SQL
    │  • Cascade CascadeType.ALL
    │  • OrphanRemoval
    ↓
[Base de données MySQL]
    │
    │  INSERT INTO contrat (...)
    │  INSERT INTO paiement (...) [si cascade]
    ↓
[Résultat]
    │
    │  Retour de l'entité persistée avec ID généré
    ↓
[Code métier]
```

## Cascade et OrphanRemoval - Contrat/Paiement

```
Suppression d'un Contrat via deleteById(1L) :

contratRepository.deleteById(1L)
    │
    ↓
Hibernate charge le Contrat en mémoire
    │
    ↓
Détection de la cascade REMOVE + orphanRemoval
    │
    ├─ DELETE FROM paiement WHERE contrat_id = 1
    │
    └─ DELETE FROM contrat WHERE id_contrat = 1


⚠️ ATTENTION avec deleteAllInBatch() :
    │
    ↓
DELETE FROM contrat WHERE ...  [exécution directe]
    │
    └─ Pas de passage par le contexte de persistance
       → Cascade et orphanRemoval NON appliqués
       → Risque de violation de contrainte de clé étrangère
```

## Configuration Spring Boot

### application.properties

```properties
# Connexion MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/autoloc_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logs SQL
logging.level.org.hibernate.SQL=DEBUG
logging.level.tn.esprit.autoloc=INFO
```

### Détection automatique des repositories

Spring Boot scanne automatiquement le package `tn.esprit.autoloc.repository` grâce à :
- `@SpringBootApplication` sur `AutoLocApplication`
- Auto-configuration de Spring Data JPA

Au démarrage, le log affiche :
```
Found 9 JPA repository interfaces
```

## Méthodes disponibles (héritées)

### De CrudRepository
- `save(S entity)` : CREATE ou UPDATE
- `saveAll(Iterable<S> entities)` : Multiple save
- `findById(ID id)` : READ par ID → Optional<T>
- `existsById(ID id)` : Vérification existence
- `findAll()` : READ toutes les entités → List<T>
- `findAllById(Iterable<ID> ids)` : READ multiple
- `count()` : Nombre d'entités
- `deleteById(ID id)` : DELETE par ID
- `delete(T entity)` : DELETE par entité
- `deleteAll()` : DELETE toutes

### De PagingAndSortingRepository
- `findAll(Sort sort)` : Liste triée
- `findAll(Pageable pageable)` : Page d'entités

### Spécifiques à JpaRepository
- `flush()` : Force l'écriture en base
- `saveAndFlush(S entity)` : save + flush immédiat
- `saveAllAndFlush(Iterable<S> entities)` : Multiple save + flush
- `getReferenceById(ID id)` : Proxy lazy (pas de SELECT immédiat)
- `deleteAllInBatch()` : Suppression en une requête ⚠️
- `deleteAllByIdInBatch(Iterable<ID> ids)` : Suppression batch ⚠️

## Bonnes pratiques

### ✅ À FAIRE
- Utiliser `Optional` pour gérer les valeurs nulles
- Toujours utiliser l'objet retourné par `save()`
- Utiliser `deleteById()` pour respecter les cascades
- Préfixer les interfaces par `I` (convention AutoLoc)

### ❌ À ÉVITER
- Utiliser `deleteAllInBatch()` sur des entités avec cascade
- Ignorer le retour de `save()`
- Utiliser `findById()` sans vérifier l'Optional
- Ajouter `@Repository` (inutile, détection automatique)

## Exemple de code métier (futur Atelier 4)

```java
@Service
public class ContratService {
    
    @Autowired
    private IContratRepository contratRepository;
    
    public Contrat creerContrat(Contrat contrat) {
        // L'ID est null → INSERT
        return contratRepository.save(contrat);
    }
    
    public Optional<Contrat> trouverContrat(Long id) {
        return contratRepository.findById(id);
    }
    
    public Contrat ajouterPaiement(Long contratId, Paiement paiement) {
        Contrat contrat = contratRepository.findById(contratId)
            .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));
        
        paiement.setContrat(contrat);
        contrat.getPaiements().add(paiement);
        
        // Le paiement sera sauvegardé grâce à la cascade
        return contratRepository.save(contrat);
    }
    
    public void supprimerContrat(Long id) {
        // Les paiements seront supprimés automatiquement
        // grâce à orphanRemoval = true
        contratRepository.deleteById(id);
    }
}
```

---

**Atelier 3 - ASI 26-27**  
Architecture des Systèmes d'Information
