# Couche Repository - AutoLoc

## Vue d'ensemble

Cette couche contient les interfaces Spring Data JPA qui gèrent la persistance des entités du projet AutoLoc. Toutes les interfaces étendent `JpaRepository<T, Long>` pour bénéficier du CRUD complet, du tri, de la pagination et des fonctionnalités JPA avancées.

## Liste des Repositories

### 1. IContratRepository
- **Entité** : Contrat
- **Particularités** : Gère la cascade sur les Paiements (CascadeType.ALL, orphanRemoval = true)

### 2. IPaiementRepository
- **Entité** : Paiement
- **Remarque** : Les paiements sont normalement modifiés via leur Contrat parent

### 3. IVehiculeRepository
- **Entité** : Vehicule
- **Relations** : Agence, Reservations, Equipements, Maintenances

### 4. IAgenceRepository
- **Entité** : Agence
- **Relations** : Employés et Véhicules (cascade ALL)

### 5. IEmployeRepository
- **Entité** : Employe
- **Relation** : Agence

### 6. IClientRepository
- **Entité** : Client
- **Relations** : Reservations

### 7. IReservationRepository
- **Entité** : Reservation
- **Relations** : Client, Vehicule, Contrat (cascade ALL)

### 8. IEquipementRepository
- **Entité** : Equipement
- **Relation** : Véhicules (ManyToMany)

### 9. IMaintenanceRepository
- **Entité** : Maintenance
- **Relation** : Vehicule

## Convention de nommage

- Toutes les interfaces commencent par le préfixe `I`
- Format : `I` + `NomEntité` + `Repository`
- Exemple : `IVehiculeRepository`, `IContratRepository`

## Méthodes disponibles (héritées de JpaRepository)

### CRUD de base
- `save(T entity)` : Crée ou met à jour
- `findById(ID id)` : Recherche par ID (retourne Optional<T>)
- `findAll()` : Liste toutes les entités (retourne List<T>)
- `deleteById(ID id)` : Supprime par ID
- `count()` : Compte les entités
- `existsById(ID id)` : Vérifie l'existence

### Tri et Pagination
- `findAll(Sort sort)` : Liste triée
- `findAll(Pageable pageable)` : Liste paginée

### Méthodes JPA avancées
- `flush()` : Force l'écriture en base
- `saveAndFlush(T entity)` : Sauvegarde et flush immédiat
- `getReferenceById(ID id)` : Retourne un proxy lazy
- `deleteAllInBatch()` : Suppression en batch (⚠️ pas de cascade)

## Points d'attention

### Cascade et OrphanRemoval
Les suppressions via `deleteById()` respectent la cascade et l'orphanRemoval.
⚠️ **Attention** : Les méthodes `*InBatch` contournent le contexte de persistance.

### Comportement de save()
- **ID null** → INSERT (persist)
- **ID existant** → SELECT puis UPDATE si modifié (merge)
- Toujours utiliser l'objet retourné par `save()`

### Optional et null safety
`findById()` retourne un `Optional<T>` pour éviter les NullPointerException.

Exemple d'utilisation :
```java
Optional<Vehicule> vehicule = vehiculeRepository.findById(1L);
vehicule.ifPresent(v -> System.out.println(v.getMarque()));
```

## Atelier réalisé
**Atelier 3** - Spring Data JPA : interfaces repository et opérations CRUD
