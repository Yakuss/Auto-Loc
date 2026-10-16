# 🧪 Guide de test des endpoints REST

## Prérequis
1. L'application doit être démarrée (port 8080 par défaut)
2. Utiliser Postman, curl, ou l'extension REST Client de VS Code

---

## 📋 Liste complète des endpoints

### 1. 🚗 VehiculeController (`/api/vehicules`)

#### GET - Récupérer tous les véhicules
```http
GET http://localhost:8080/api/vehicules
```

#### GET - Récupérer un véhicule par ID
```http
GET http://localhost:8080/api/vehicules/1
```

#### POST - Créer un nouveau véhicule
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

#### PUT - Mettre à jour un véhicule
```http
PUT http://localhost:8080/api/vehicules/1
Content-Type: application/json

{
  "tarifJournalier": 180.00,
  "statut": "LOUE"
}
```

#### DELETE - Supprimer un véhicule
```http
DELETE http://localhost:8080/api/vehicules/1
```

---

### 2. 👤 ClientController (`/api/clients`)

#### GET - Récupérer tous les clients
```http
GET http://localhost:8080/api/clients
```

#### GET - Récupérer un client par ID
```http
GET http://localhost:8080/api/clients/1
```

#### POST - Créer un nouveau client
```http
POST http://localhost:8080/api/clients
Content-Type: application/json

{
  "nom": "Ben Ali",
  "prenom": "Mohamed",
  "email": "mohamed.benali@example.com",
  "telephone": "21612345678",
  "numPermis": "TN123456",
  "dateInscription": "2024-01-15"
}
```

#### PUT - Mettre à jour un client
```http
PUT http://localhost:8080/api/clients/1
Content-Type: application/json

{
  "email": "nouveau.email@example.com",
  "telephone": "21698765432"
}
```

#### DELETE - Supprimer un client
```http
DELETE http://localhost:8080/api/clients/1
```

---

### 3. 📅 ReservationController (`/api/reservations`)

#### GET - Récupérer toutes les réservations
```http
GET http://localhost:8080/api/reservations
```

#### GET - Récupérer une réservation par ID
```http
GET http://localhost:8080/api/reservations/1
```

#### POST - Créer une nouvelle réservation
```http
POST http://localhost:8080/api/reservations
Content-Type: application/json

{
  "dateDebut": "2024-02-01",
  "dateFin": "2024-02-05",
  "statut": "EN_ATTENTE"
}
```

#### PUT - Mettre à jour une réservation
```http
PUT http://localhost:8080/api/reservations/1
Content-Type: application/json

{
  "statut": "CONFIRMEE"
}
```

#### DELETE - Supprimer une réservation
```http
DELETE http://localhost:8080/api/reservations/1
```

---

### 4. 📄 ContratController (`/api/contrats`)

#### GET - Récupérer tous les contrats
```http
GET http://localhost:8080/api/contrats
```

#### GET - Récupérer un contrat par ID
```http
GET http://localhost:8080/api/contrats/1
```

#### POST - Créer un nouveau contrat
```http
POST http://localhost:8080/api/contrats
Content-Type: application/json

{
  "dateSignature": "2024-01-30",
  "montantTotal": 750.00,
  "valide": true
}
```

#### PUT - Mettre à jour un contrat
```http
PUT http://localhost:8080/api/contrats/1
Content-Type: application/json

{
  "montantTotal": 800.00,
  "valide": false
}
```

#### DELETE - Supprimer un contrat
```http
DELETE http://localhost:8080/api/contrats/1
```

---

### 5. 💳 PaiementController (`/api/paiements`) - READ ONLY

#### GET - Récupérer tous les paiements
```http
GET http://localhost:8080/api/paiements
```

#### GET - Récupérer un paiement par ID
```http
GET http://localhost:8080/api/paiements/1
```

**Note:** Les paiements sont gérés via les contrats (composition forte). Pas de création/modification directe.

---

### 6. 🔧 MaintenanceController (`/api/maintenances`)

#### GET - Récupérer toutes les maintenances
```http
GET http://localhost:8080/api/maintenances
```

#### GET - Récupérer une maintenance par ID
```http
GET http://localhost:8080/api/maintenances/1
```

#### POST - Créer une nouvelle maintenance
```http
POST http://localhost:8080/api/maintenances
Content-Type: application/json

{
  "dateDebut": "2024-02-10",
  "dateFin": "2024-02-12",
  "description": "Révision complète et changement d'huile"
}
```

#### PUT - Mettre à jour une maintenance
```http
PUT http://localhost:8080/api/maintenances/1
Content-Type: application/json

{
  "dateFin": "2024-02-13",
  "description": "Révision complète, changement d'huile et freins"
}
```

#### DELETE - Supprimer une maintenance
```http
DELETE http://localhost:8080/api/maintenances/1
```

---

### 7. 🏢 AgenceController (`/api/agences`)

#### GET - Récupérer toutes les agences
```http
GET http://localhost:8080/api/agences
```

#### GET - Récupérer une agence par ID
```http
GET http://localhost:8080/api/agences/1
```

#### POST - Créer une nouvelle agence
```http
POST http://localhost:8080/api/agences
Content-Type: application/json

{
  "nom": "Auto-Loc Tunis",
  "ville": "Tunis",
  "adresse": "Avenue Habib Bourguiba",
  "telephone": "21671234567"
}
```

#### PUT - Mettre à jour une agence
```http
PUT http://localhost:8080/api/agences/1
Content-Type: application/json

{
  "ville": "Sfax",
  "adresse": "Avenue de la Liberté",
  "telephone": "21674123456"
}
```

#### DELETE - Supprimer une agence
```http
DELETE http://localhost:8080/api/agences/1
```

---

### 8. 👔 EmployeController (`/api/employes`)

#### GET - Récupérer tous les employés
```http
GET http://localhost:8080/api/employes
```

#### GET - Récupérer un employé par ID
```http
GET http://localhost:8080/api/employes/1
```

#### POST - Créer un nouveau employé
```http
POST http://localhost:8080/api/employes
Content-Type: application/json

{
  "nom": "Trabelsi",
  "prenom": "Ahmed",
  "role": "GERANT"
}
```

#### PUT - Mettre à jour un employé
```http
PUT http://localhost:8080/api/employes/1
Content-Type: application/json

{
  "role": "COMMERCIAL"
}
```

#### DELETE - Supprimer un employé
```http
DELETE http://localhost:8080/api/employes/1
```

---

### 9. 🛠️ EquipementController (`/api/equipements`)

#### GET - Récupérer tous les équipements
```http
GET http://localhost:8080/api/equipements
```

#### GET - Récupérer un équipement par ID
```http
GET http://localhost:8080/api/equipements/1
```

#### POST - Créer un nouveau équipement
```http
POST http://localhost:8080/api/equipements
Content-Type: application/json

{
  "libelle": "GPS"
}
```

#### PUT - Mettre à jour un équipement
```http
PUT http://localhost:8080/api/equipements/1
Content-Type: application/json

{
  "libelle": "GPS Garmin"
}
```

#### DELETE - Supprimer un équipement
```http
DELETE http://localhost:8080/api/equipements/1
```

---

## 🧪 Scénarios de test complets

### Scénario 1 : Créer un véhicule et le récupérer
```bash
# 1. Créer un véhicule
POST /api/vehicules
# Copier l'ID retourné (ex: 1)

# 2. Récupérer le véhicule créé
GET /api/vehicules/1

# 3. Modifier le véhicule
PUT /api/vehicules/1

# 4. Vérifier la modification
GET /api/vehicules/1
```

### Scénario 2 : Créer un client et une réservation
```bash
# 1. Créer un client
POST /api/clients

# 2. Créer un véhicule
POST /api/vehicules

# 3. Créer une réservation
POST /api/reservations

# 4. Vérifier toutes les réservations
GET /api/reservations
```

### Scénario 3 : Tester les validations (erreurs attendues)
```bash
# Essayer de créer un véhicule sans immatriculation
POST /api/vehicules
{
  "marque": "Toyota"
}
# Devrait retourner une erreur 400

# Essayer de récupérer un ID inexistant
GET /api/vehicules/999
# Devrait retourner une erreur 404
```

---

## 📊 Codes de réponse HTTP attendus

| Code | Signification | Contexte |
|------|---------------|----------|
| 200 OK | Succès | GET, PUT réussis |
| 201 Created | Créé | POST réussi |
| 204 No Content | Pas de contenu | DELETE réussi |
| 400 Bad Request | Mauvaise requête | Validation échouée |
| 404 Not Found | Non trouvé | Ressource inexistante |
| 500 Internal Server Error | Erreur serveur | Erreur applicative |

---

## 🔍 Enums disponibles

### CategorieVehicule
- `ECONOMIQUE`
- `BERLINE`
- `SUV`
- `LUXE`
- `UTILITAIRE`

### StatutVehicule
- `DISPONIBLE`
- `LOUE`
- `EN_MAINTENANCE`
- `HORS_SERVICE`

### StatutReservation
- `EN_ATTENTE`
- `CONFIRMEE`
- `EN_COURS`
- `TERMINEE`
- `ANNULEE`

### ModePaiement
- `ESPECES`
- `CARTE_BANCAIRE`
- `CHEQUE`
- `VIREMENT`

### RoleEmploye
- `GERANT`
- `COMMERCIAL`
- `TECHNICIEN`
- `ADMINISTRATIF`

---

## 💡 Conseils pour les tests

1. **Ordre de création recommandé :**
   - Agence → Employé
   - Client
   - Véhicule → Équipement
   - Réservation → Contrat → Paiement
   - Maintenance

2. **Test des validations :**
   - ID non-null lors de la création (devrait échouer)
   - Dates incohérentes (dateFin < dateDebut)
   - Montants négatifs
   - Champs obligatoires vides

3. **Test des relations :**
   - Créer une agence puis un employé
   - Créer un client puis une réservation
   - Vérifier la suppression en cascade

---

## 🚀 Démarrage rapide avec curl (Windows CMD)

### Test GET simple
```cmd
curl http://localhost:8080/api/vehicules
```

### Test POST avec JSON
```cmd
curl -X POST http://localhost:8080/api/vehicules -H "Content-Type: application/json" -d "{\"immatriculation\":\"TUN-1234\",\"marque\":\"Toyota\",\"modele\":\"Corolla\",\"categorie\":\"BERLINE\",\"tarifJournalier\":150.00,\"statut\":\"DISPONIBLE\"}"
```

### Test PUT
```cmd
curl -X PUT http://localhost:8080/api/vehicules/1 -H "Content-Type: application/json" -d "{\"tarifJournalier\":180.00}"
```

### Test DELETE
```cmd
curl -X DELETE http://localhost:8080/api/vehicules/1
```

---

## ✅ Checklist de test

- [ ] Tous les endpoints GET fonctionnent
- [ ] Tous les endpoints POST créent correctement
- [ ] Tous les endpoints PUT mettent à jour correctement
- [ ] Tous les endpoints DELETE suppriment correctement
- [ ] Les validations métier fonctionnent (erreurs 400)
- [ ] Les ressources inexistantes retournent 404
- [ ] Les relations entre entités fonctionnent
- [ ] Les enums sont correctement validés

---

**Bonne chance pour vos tests ! 🎉**
