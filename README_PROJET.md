# 🚗 Projet AutoLoc - Application de Location de Véhicules

## 📝 Vue d'ensemble

Projet Spring Boot pour la gestion d'un système de location de véhicules avec 9 entités JPA, couches Repository et Service complètes.

---

## ✅ État d'avancement

| Composant | Statut | Fichiers |
|-----------|--------|----------|
| **Entités JPA** | ✅ Complet | 9 entités |
| **Repositories** | ✅ Complet | 9 interfaces |
| **Services (interfaces)** | ✅ Complet | 9 interfaces |
| **Services (implémentations)** | ✅ Complet | 9 classes |
| **CRUD complet** | ✅ Complet | 2 entités (Client, Vehicule) |
| **Contrôleurs REST** | ⏳ À venir | Atelier 5 |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│         PRESENTATION LAYER              │  ← À venir (Atelier 5)
│         (REST Controllers)              │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│          BUSINESS LAYER                 │  ← ✅ FAIT
│          (Services)                     │
│  • ClientService ⭐                     │
│  • VehiculeService ⭐                   │
│  • + 7 autres services                  │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│        PERSISTENCE LAYER                │  ← ✅ FAIT
│        (Repositories)                   │
│  • 9 JpaRepository interfaces           │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│          DATA LAYER                     │  ← ✅ FAIT
│          (Entities)                     │
│  • 9 entités JPA                        │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│           DATABASE                      │
│         MySQL - autoloc_db              │
└─────────────────────────────────────────┘
```

---

## 📦 Entités du domaine

| Entité | Description | Relations |
|--------|-------------|-----------|
| **Agence** | Agence de location | → Employés, Véhicules |
| **Vehicule** | Véhicule à louer | → Agence, Réservations, Équipements, Maintenances |
| **Employe** | Employé d'agence | → Agence |
| **Client** | Client loueur | → Réservations |
| **Reservation** | Réservation d'un véhicule | → Client, Véhicule, Contrat |
| **Contrat** | Contrat de location | → Réservation, Paiements |
| **Paiement** | Paiement d'un contrat | → Contrat |
| **Equipement** | Équipement de véhicule | ↔ Véhicules (ManyToMany) |
| **Maintenance** | Maintenance d'un véhicule | → Véhicule |

---

## 🎯 Services avec CRUD complet

### 1️⃣ ClientService ⭐

```java
✅ CREATE  : save(Client client)
✅ READ    : findById(Long id), findAll()
✅ UPDATE  : update(Long id, Client client)
✅ DELETE  : deleteById(Long id)
✅ COUNT   : count()
```

**Fonctionnalités** :
- Logging avec `@Slf4j`
- Vérification d'existence
- Gestion d'erreurs
- Transactions optimisées

### 2️⃣ VehiculeService ⭐

```java
✅ CREATE  : save(Vehicule vehicule)
✅ READ    : findById(Long id), findAll()
✅ UPDATE  : update(Long id, Vehicule vehicule)
✅ DELETE  : deleteById(Long id)
✅ COUNT   : count()
```

**Fonctionnalités** :
- Logging avec `@Slf4j`
- Vérification d'existence
- Gestion d'erreurs
- Transactions optimisées

---

## 📂 Structure des packages

```
src/main/java/tn/esprit/autoloc/
│
├── AutoLocApplication.java          # Point d'entrée Spring Boot
│
├── domain/                          # 🟢 Entités JPA
│   ├── Agence.java
│   ├── Vehicule.java
│   ├── Employe.java
│   ├── Client.java
│   ├── Reservation.java
│   ├── Contrat.java
│   ├── Paiement.java
│   ├── Equipement.java
│   ├── Maintenance.java
│   └── [Enums] CategorieVehicule, StatutVehicule, etc.
│
├── repository/                      # 🟢 Spring Data JPA
│   ├── IAgenceRepository.java
│   ├── IVehiculeRepository.java
│   ├── IEmployeRepository.java
│   ├── IClientRepository.java
│   ├── IReservationRepository.java
│   ├── IContratRepository.java
│   ├── IPaiementRepository.java
│   ├── IEquipementRepository.java
│   └── IMaintenanceRepository.java
│
├── service/                         # 🟢 Logique métier
│   ├── [Interfaces]
│   │   ├── IAgenceService.java
│   │   ├── IVehiculeService.java
│   │   ├── IEmployeService.java
│   │   ├── IClientService.java
│   │   ├── IReservationService.java
│   │   ├── IContratService.java
│   │   ├── IPaiementService.java
│   │   ├── IEquipementService.java
│   │   └── IMaintenanceService.java
│   │
│   └── [Implémentations]
│       ├── AgenceService.java
│       ├── VehiculeService.java ⭐
│       ├── EmployeService.java
│       ├── ClientService.java ⭐
│       ├── ReservationService.java
│       ├── ContratService.java
│       ├── PaiementService.java
│       ├── EquipementService.java
│       └── MaintenanceService.java
│
└── web/                             # 🔴 À venir (Atelier 5)
    ├── controller/
    └── dto/
```

---

## ⚙️ Configuration

### application.properties

```properties
# Base de données
spring.datasource.url=jdbc:mysql://localhost:3306/autoloc_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logs
logging.level.org.hibernate.SQL=DEBUG
logging.level.tn.esprit.autoloc=INFO

# Serveur
server.port=8081
```

### pom.xml - Dépendances principales

```xml
<!-- Spring Boot Starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<!-- Base de données -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

---

## 🚀 Lancement du projet

### Prérequis
- Java 17+
- Maven
- MySQL Server
- IntelliJ IDEA (recommandé)

### Étapes

1. **Cloner le projet**
   ```bash
   cd "c:\Users\yassi\Desktop\AUTO-LOC\autoLoc"
   ```

2. **Démarrer MySQL**
   - La base `autoloc_db` sera créée automatiquement

3. **Compiler**
   ```bash
   mvn clean compile
   ```

4. **Lancer l'application**
   ```bash
   mvn spring-boot:run
   ```

5. **Vérifier les logs**
   - Rechercher : `Found 9 JPA repository interfaces`
   - Le serveur démarre sur le port **8081**

---

## 📊 Statistiques du projet

| Métrique | Valeur |
|----------|--------|
| Entités JPA | 9 |
| Repositories | 9 |
| Services (interfaces) | 9 |
| Services (classes) | 9 |
| CRUD complet | 2 (Client, Vehicule) |
| Lignes de code | ~1500+ |
| Fichiers Java | 36 |
| Fichiers de documentation | 7 |

---

## 📚 Documentation

- **`ATELIER3_SUMMARY.md`** - Résumé atelier 3 (repositories)
- **`SERVICE_LAYER_SUMMARY.md`** - Documentation couche service
- **`FINAL_CHECKLIST.md`** - Checklist complète du projet
- **`docs/repository-notes.md`** - Notes techniques repositories
- **`docs/architecture-repository.md`** - Architecture détaillée
- **`VERIFICATION_CHECKLIST.md`** - Checklist de vérification

---

## 🎓 Concepts techniques utilisés

### Spring Framework
- `@SpringBootApplication`
- `@Service`, `@Repository`
- `@Transactional`, `@Transactional(readOnly = true)`
- Injection de dépendances

### Spring Data JPA
- JpaRepository<T, ID>
- Méthodes CRUD héritées
- Génération automatique d'implémentations

### Lombok
- `@Getter`, `@Setter`
- `@NoArgsConstructor`, `@AllArgsConstructor`
- `@RequiredArgsConstructor` (injection)
- `@Slf4j` (logging)

### JPA / Hibernate
- `@Entity`, `@Id`, `@GeneratedValue`
- Relations : `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne`
- `cascade`, `orphanRemoval`
- `@Enumerated`

---

## 🧪 Tests (à implémenter)

### Tests unitaires suggérés
```java
@Test
void testSaveClient() {
    // Arrange
    Client client = new Client();
    client.setNom("Dupont");
    
    // Act
    Client saved = clientService.save(client);
    
    // Assert
    assertNotNull(saved.getIdClient());
}
```

---

## 🔜 Prochaines étapes

### Atelier 5 - Contrôleurs REST
- [ ] Créer les contrôleurs REST
- [ ] DTOs (Data Transfer Objects)
- [ ] Validation des données (@Valid)
- [ ] Documentation API (Swagger)

### Améliorations suggérées
- [ ] Exceptions personnalisées
- [ ] @ControllerAdvice pour gestion globale des erreurs
- [ ] Tests unitaires (JUnit + Mockito)
- [ ] Tests d'intégration
- [ ] Sécurité (Spring Security)

---

## 👨‍💻 Auteur

**Projet AutoLoc**  
ASI 26-27 - Architecture des Systèmes d'Information  
ESPRIT - École Supérieure Privée d'Ingénierie et de Technologies

---

## 📄 Licence

Projet académique - ESPRIT

---

**Status** : ✅ Couches Domain, Repository et Service complètes  
**Dernière mise à jour** : 2024  
**Version Spring Boot** : 4.1.1  
**Version Java** : 17
