# Atelier 3 - Notes sur la couche Repository

## Choix des interfaces Spring Data JPA

| Interface | Étend | Justification |
|-----------|-------|---------------|
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, findAll renvoie une List, saveAndFlush disponible. Permet la gestion complète des contrats avec cascade sur les paiements. |
| IPaiementRepository | JpaRepository<Paiement, Long> | CRUD complet pour la lecture des paiements. Les modifications passent par le Contrat selon la règle métier. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | CRUD complet, tri et pagination disponibles pour gérer le parc de véhicules. |
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD complet avec cascade sur les employés et véhicules associés. |
| IEmployeRepository | JpaRepository<Employe, Long> | CRUD complet pour la gestion des employés rattachés aux agences. |
| IClientRepository | JpaRepository<Client, Long> | CRUD complet, tri et pagination utiles pour gérer la liste des clients. |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD complet avec cascade sur les contrats. Tri et pagination nécessaires pour l'affichage des réservations. |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD complet pour gérer les équipements disponibles dans le système. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD complet pour suivre les maintenances des véhicules. |

## Anomalies SonarQube for IDE corrigées

| Anomalie SonarQube for IDE | Règle / explication | Correction apportée |
|----------------------------|---------------------|---------------------|
| Import dupliqué dans Vehicule.java | java:S1128 - Les imports non utilisés doivent être supprimés. Un import en double crée de la confusion et de la redondance. | Suppression de l'import en double de `java.math.BigDecimal`. Réorganisation des imports : Jakarta, Lombok, puis Java standard. |
| Utilisation de @Data sur les entités JPA | Lombok @Data génère hashCode/equals sur toutes les propriétés, problématique avec les collections lazy et les associations bidirectionnelles | Les entités utilisent @Getter et @Setter pour un contrôle précis, évitant les problèmes de LazyInitializationException et de StackOverflow dans les associations bidirectionnelles |
| Absence de constructeur sans arguments | JPA nécessite un constructeur par défaut (no-arg constructor) pour l'instanciation des entités via réflexion | Ajout de @NoArgsConstructor sur toutes les entités. @AllArgsConstructor est également présent pour faciliter les tests |

## Points clés de l'implémentation

### Choix de JpaRepository
Toutes les interfaces étendent `JpaRepository<Entity, Long>` car elle combine:
- **ListCrudRepository** : CRUD de base avec retour de List au lieu d'Iterable
- **ListPagingAndSortingRepository** : Tri et pagination avec List
- **Méthodes JPA spécifiques** : flush(), saveAndFlush(), getReferenceById()

### Cascade et OrphanRemoval
- **Contrat → Paiement** : `cascade = CascadeType.ALL, orphanRemoval = true` permet la suppression automatique des paiements lors de la suppression d'un contrat
- **Attention** : Les méthodes `deleteAllInBatch()` contournent le contexte de persistance et ne déclenchent pas la cascade

### Convention de nommage
- Toutes les interfaces commencent par `I` (IContratRepository, IVehiculeRepository...)
- Suivent la convention Spring Data : I + NomEntité + Repository
- Placées dans le package `tn.esprit.autoloc.repository`

### Comportement de save()
- **Entité nouvelle** (id = null) : `persist` → INSERT avec génération d'ID
- **Entité existante** (id != null) : `merge` → SELECT puis UPDATE si modification
- Toujours utiliser l'objet retourné par save()

### Méthodes importantes
- `findById(Long id)` : retourne un `Optional<T>`
- `deleteById(Long id)` : ne lance pas d'exception si l'ID n'existe pas (Spring Data 3)
- `saveAndFlush()` : écriture immédiate en base de données
- `getReferenceById()` : retourne un proxy lazy sans requête immédiate
