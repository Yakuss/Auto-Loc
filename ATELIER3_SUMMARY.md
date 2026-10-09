# Atelier 3 - Spring Data JPA : Résumé de réalisation

## 📋 Objectifs accomplis

✅ Création de 9 interfaces repository dans le package `tn.esprit.autoloc.repository`  
✅ Toutes les interfaces étendent `JpaRepository<Entité, Long>`  
✅ Convention de nommage respectée : préfixe `I` (ex: `IVehiculeRepository`)  
✅ Correction des anomalies détectées (imports dupliqués)  
✅ Documentation complète dans `docs/repository-notes.md`  

## 📁 Structure du projet

```
tn.esprit.autoloc/
├── domain/                    # 9 entités JPA (Ateliers 1 & 2)
│   ├── Agence.java
│   ├── Vehicule.java
│   ├── Employe.java
│   ├── Client.java
│   ├── Reservation.java
│   ├── Contrat.java
│   ├── Paiement.java
│   ├── Equipement.java
│   └── Maintenance.java
│
├── repository/                # 9 interfaces Spring Data JPA ✨ (Atelier 3)
│   ├── IAgenceRepository.java
│   ├── IVehiculeRepository.java
│   ├── IEmployeRepository.java
│   ├── IClientRepository.java
│   ├── IReservationRepository.java
│   ├── IContratRepository.java
│   ├── IPaiementRepository.java
│   ├── IEquipementRepository.java
│   ├── IMaintenanceRepository.java
│   └── README.md
│
├── service/                   # Couche métier (Atelier 4)
└── web/                       # Contrôleurs REST (Atelier 5)
```

## 🔧 Interfaces créées

| Interface | Entité | Type de clé |
|-----------|--------|-------------|
| IAgenceRepository | Agence | Long |
| IVehiculeRepository | Vehicule | Long |
| IEmployeRepository | Employe | Long |
| IClientRepository | Client | Long |
| IReservationRepository | Reservation | Long |
| IContratRepository | Contrat | Long |
| IPaiementRepository | Paiement | Long |
| IEquipementRepository | Equipement | Long |
| IMaintenanceRepository | Maintenance | Long |

## 📝 Exemple de code

```java
package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Contrat;

public interface IContratRepository extends JpaRepository<Contrat, Long> {
}
```

## 🎯 Pourquoi JpaRepository ?

`JpaRepository<T, Long>` a été choisi pour toutes les interfaces car il combine :

1. **ListCrudRepository** : CRUD complet avec List au lieu d'Iterable
   - `save()`, `findById()`, `findAll()`, `deleteById()`, etc.

2. **ListPagingAndSortingRepository** : Tri et pagination
   - `findAll(Sort sort)`
   - `findAll(Pageable pageable)`

3. **Méthodes JPA spécifiques** :
   - `flush()` : Force l'écriture en base
   - `saveAndFlush()` : Sauvegarde immédiate
   - `getReferenceById()` : Retourne un proxy lazy

## 🔍 Anomalies corrigées

### 1. Import dupliqué dans Vehicule.java
- **Règle SonarLint** : java:S1128
- **Problème** : `import java.math.BigDecimal;` apparaissait deux fois
- **Correction** : Suppression du doublon

### 2. Organisation des imports
- **Amélioration** : Imports regroupés et ordonnés (Jakarta, Lombok, Java)

## 🚀 Vérification du fonctionnement

Pour vérifier que les repositories sont correctement détectés par Spring :

```bash
mvn spring-boot:run
```

Dans les logs, rechercher :
```
Found 9 JPA repository interfaces
```

## 📚 Points clés à retenir

### Cascade et OrphanRemoval
```java
@OneToMany(mappedBy = "contrat", 
           cascade = CascadeType.ALL, 
           orphanRemoval = true)
private Set<Paiement> paiements = new HashSet<>();
```
- Les paiements sont automatiquement supprimés avec leur contrat
- ⚠️ Les méthodes `deleteAllInBatch()` contournent cette cascade

### Comportement de save()
| Situation | Opération | Résultat |
|-----------|-----------|----------|
| ID = null | `persist` | INSERT avec génération d'ID |
| ID renseigné | `merge` | SELECT puis UPDATE si modifié |

### Optional pour la sécurité
```java
Optional<Vehicule> vehicule = vehiculeRepository.findById(1L);
vehicule.ifPresent(v -> System.out.println(v.getMarque()));
```

## 📖 Documentation

- **Notes détaillées** : `docs/repository-notes.md`
- **README technique** : `src/main/java/tn/esprit/autoloc/repository/README.md`

## ✅ Livrables

- [x] 9 interfaces repository créées et fonctionnelles
- [x] Convention de nommage `I...Repository` respectée
- [x] Extension de `JpaRepository<Entité, Long>` pour toutes
- [x] Anomalies SonarLint corrigées
- [x] Documentation complète fournie

## 🔜 Prochaine étape

**Atelier 4** : Développement de la couche Service (logique métier)

---

**Date de réalisation** : Atelier 3 - ASI 26-27  
**Auteur** : AutoLoc Project  
**Framework** : Spring Boot 4.1.1 + Spring Data JPA + Hibernate
