# 🧪 Guide de Test - DTOs et Validation

## 🚀 Démarrage de l'application

```bash
# Windows
cd c:\Users\yassi\Desktop\AUTO-LOC\autoLoc
.\mvnw.cmd spring-boot:run

# Ou avec Maven installé
mvn spring-boot:run
```

L'application démarre sur **http://localhost:8080**

---

## 📍 Accès aux interfaces

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/v3/api-docs
- **Base URL:** http://localhost:8080

---

## 🎯 Tests prioritaires à réaliser

### 1. Test de création réussie (201 Created)

#### POST http://localhost:8080/agences
```json
{
  "nom": "Agence Tunis Centre",
  "adresse": "Avenue Habib Bourguiba, Tunis",
  "telephone": "71234567"
}
```

**Résultat attendu:** 201 Created avec DTO de réponse incluant l'ID

---

### 2. Test de validation échouée (400 Bad Request)

#### POST http://localhost:8080/vehicules
```json
{
  "marque": "",
  "modele": "208",
  "immatriculation": "INVALID",
  "categorie": "CITADINE",
  "tarifJournalier": -50.0,
  "agenceId": null
}
```

**Résultat attendu:** 400 Bad Request avec `validationErrors`:
```json
{
  "timestamp": "2024-11-12T...",
  "status": 400,
  "error": "Validation Failed",
  "message": "Les données fournies sont invalides",
  "path": "/vehicules",
  "validationErrors": {
    "marque": "La marque est obligatoire",
    "immatriculation": "Format d'immatriculation invalide (ex: 123 TU 4567)",
    "tarifJournalier": "Le tarif doit être positif",
    "agenceId": "L'agence est obligatoire"
  }
}
```

---

### 3. Test de ressource introuvable (404 Not Found)

#### GET http://localhost:8080/vehicules/999

**Résultat attendu:** 404 Not Found
```json
{
  "timestamp": "2024-11-12T...",
  "status": 404,
  "error": "Not Found",
  "message": "Vehicule introuvable avec l'ID: 999",
  "path": "/vehicules/999",
  "validationErrors": null
}
```

---

### 4. Test de création avec association valide

#### Étape 1: Créer une agence
**POST** http://localhost:8080/agences
```json
{
  "nom": "Agence Tunis Centre",
  "adresse": "Avenue Habib Bourguiba",
  "telephone": "71234567"
}
```
→ Noter l'`idAgence` retourné (ex: 1)

#### Étape 2: Créer un véhicule associé
**POST** http://localhost:8080/vehicules
```json
{
  "marque": "Peugeot",
  "modele": "208",
  "annee": 2023,
  "immatriculation": "123 TU 4567",
  "categorie": "CITADINE",
  "statut": "DISPONIBLE",
  "tarifJournalier": 50.0,
  "agenceId": 1
}
```

**Résultat attendu:** 201 Created avec `agenceNom` dans la réponse:
```json
{
  "idVehicule": 1,
  "marque": "Peugeot",
  "modele": "208",
  "annee": 2023,
  "immatriculation": "123 TU 4567",
  "categorie": "CITADINE",
  "statut": "DISPONIBLE",
  "tarifJournalier": 50.0,
  "agenceId": 1,
  "agenceNom": "Agence Tunis Centre"  ← Info enrichie!
}
```

---

### 5. Test de validation email et téléphone

#### POST http://localhost:8080/clients
```json
{
  "nom": "Ben Ali",
  "prenom": "Mohamed",
  "email": "invalid-email",
  "telephone": "123",
  "adresse": "Rue de la République",
  "numeroPermis": "B123456"
}
```

**Résultat attendu:** 400 Bad Request
```json
{
  "validationErrors": {
    "email": "Format d'email invalide",
    "telephone": "Le téléphone doit contenir exactement 8 chiffres"
  }
}
```

---

### 6. Test de validation de dates (Réservation)

#### POST http://localhost:8080/reservations
```json
{
  "dateDebut": "2023-01-01",
  "dateFin": "2023-01-05",
  "montantTotal": 200.0,
  "statut": "EN_ATTENTE",
  "clientId": 1,
  "vehiculeId": 1
}
```

**Résultat attendu:** 400 Bad Request (dates passées)
```json
{
  "validationErrors": {
    "dateDebut": "La date de début doit être dans le futur ou aujourd'hui",
    "dateFin": "La date de fin doit être dans le futur ou aujourd'hui"
  }
}
```

---

### 7. Test de contrat avec paiements imbriqués (Cascade)

#### Prérequis: Créer agence, client, véhicule, réservation

#### POST http://localhost:8080/contrats
```json
{
  "dateDebut": "2024-11-15",
  "dateFin": "2024-11-20",
  "montantTotal": 300.0,
  "reservationId": 1,
  "paiements": [
    {
      "datePaiement": "2024-11-15",
      "montant": 150.0,
      "modePaiement": "CARTE_CREDIT"
    },
    {
      "datePaiement": "2024-11-20",
      "montant": 150.0,
      "modePaiement": "ESPECES"
    }
  ]
}
```

**Résultat attendu:** 201 Created avec les 2 paiements inclus dans `ContratResponseDTO`

---

### 8. Test READ-ONLY pour Paiements

#### GET http://localhost:8080/paiements

**Résultat attendu:** 200 OK avec liste des paiements

#### POST http://localhost:8080/paiements (DOIT ÉCHOUER)

**Résultat attendu:** 405 Method Not Allowed (pas de POST sur paiements)

---

## 📝 Scénario complet de test

### Workflow: Créer une location complète

```
1. POST /agences → Créer "Agence Tunis"
   ✅ 201 Created, retour: idAgence = 1

2. POST /equipements → Créer "GPS"
   ✅ 201 Created, retour: idEquipement = 1

3. POST /vehicules → Créer Peugeot 208 (agenceId=1)
   ✅ 201 Created, retour: idVehicule = 1, agenceNom présent

4. POST /clients → Créer client avec email valide
   ✅ 201 Created, retour: idClient = 1

5. POST /reservations → Réserver véhicule 1 pour client 1
   ✅ 201 Created, retour: idReservation = 1

6. POST /contrats → Créer contrat avec 2 paiements
   ✅ 201 Created, retour: idContrat = 1, 2 paiements

7. GET /paiements → Lister tous les paiements
   ✅ 200 OK, voir les 2 paiements créés

8. GET /contrats/1 → Consulter contrat avec paiements
   ✅ 200 OK, paiements imbriqués présents

9. DELETE /contrats/1 → Supprimer contrat
   ✅ 204 No Content

10. GET /paiements → Vérifier cascade
    ✅ 200 OK, paiements supprimés automatiquement
```

---

## 🎨 Tests Swagger UI

### Accès: http://localhost:8080/swagger-ui.html

### Tests à réaliser:

1. **Vehicules Controller**
   - Tester POST avec validation réussie
   - Tester POST avec validation échouée
   - Tester GET /vehicules/{id} avec ID valide
   - Tester GET /vehicules/999 (404)

2. **Clients Controller**
   - Tester email invalide → 400
   - Tester téléphone invalide → 400
   - Tester création réussie → 201

3. **Reservations Controller**
   - Tester dates passées → 400
   - Tester dates futures → 201

4. **Contrats Controller**
   - Tester contrat avec paiements valides → 201
   - Tester contrat avec paiement invalide → 400

5. **Paiements Controller** (READ-ONLY)
   - Vérifier que seuls GET sont disponibles
   - Pas de POST, PUT, DELETE

---

## 📸 Captures d'écran à prendre

### Dans Swagger:

1. **Liste des endpoints** - Vue d'ensemble de tous les contrôleurs
2. **Schéma VehiculeRequestDTO** - Voir les annotations de validation
3. **POST /vehicules SUCCESS** - 201 avec réponse DTO
4. **POST /vehicules ERROR** - 400 avec validationErrors
5. **GET /vehicules/{id} NOT FOUND** - 404 avec ErrorResponse
6. **POST /contrats** - Contrat avec paiements imbriqués
7. **GET /paiements** - Liste des paiements (read-only)

### Dans Postman:

1. **Collection complète** - Tous les endpoints organisés
2. **Validation errors** - Exemples de 400 avec validationErrors
3. **Success responses** - Exemples de 201 et 200
4. **Association test** - Véhicule avec agenceNom enrichi

---

## 🔍 Vérifications importantes

### ✅ Checklist de validation

- [ ] Tous les POST retournent 201 Created (pas 200)
- [ ] Tous les DELETE retournent 204 No Content
- [ ] Les erreurs de validation retournent 400 avec `validationErrors`
- [ ] Les ressources introuvables retournent 404
- [ ] Les ResponseDTOs contiennent des infos enrichies (agenceNom, clientNom, etc.)
- [ ] Pas de références circulaires dans les réponses JSON
- [ ] Les paiements sont créés via contrat (cascade)
- [ ] Les paiements ne peuvent pas être créés via POST /paiements
- [ ] Validation email fonctionne (@Email)
- [ ] Validation téléphone fonctionne (@Pattern)
- [ ] Validation immatriculation fonctionne (@Pattern)
- [ ] Validation tarifs fonctionne (@DecimalMin, @DecimalMax)
- [ ] Validation dates fonctionne (@FutureOrPresent, @PastOrPresent)
- [ ] Validation cascade fonctionne (@Valid sur paiements)

---

## 🐛 Problèmes courants

### Erreur: "Agence introuvable avec l'ID: X"
**Solution:** Créer d'abord l'agence avec POST /agences, puis utiliser l'ID retourné

### Erreur: "Les données fournies sont invalides"
**Solution:** Vérifier le format des données (email, téléphone, immatriculation, dates)

### Erreur: StackOverflowError
**Solution:** Ne devrait PLUS arriver avec les DTOs (vérifier qu'on utilise bien DTOs et pas entités)

### Erreur: 405 Method Not Allowed sur POST /paiements
**Solution:** NORMAL! Les paiements sont créés via POST /contrats uniquement

---

## 📊 Résultats attendus

### Format de réponse d'erreur standard
```json
{
  "timestamp": "2024-11-12T14:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Les données fournies sont invalides",
  "path": "/vehicules",
  "validationErrors": {
    "champ1": "Message d'erreur 1",
    "champ2": "Message d'erreur 2"
  }
}
```

### Format de réponse réussie (exemple Vehicule)
```json
{
  "idVehicule": 1,
  "marque": "Peugeot",
  "modele": "208",
  "annee": 2023,
  "immatriculation": "123 TU 4567",
  "categorie": "CITADINE",
  "statut": "DISPONIBLE",
  "tarifJournalier": 50.0,
  "agenceId": 1,
  "agenceNom": "Agence Tunis Centre"
}
```

---

## 🎓 Points à observer pendant les tests

1. **DTOs vs Entités**
   - Les réponses sont maintenant légères (pas de nested objects infinis)
   - Les IDs sont présents + infos enrichies (noms)

2. **Validation automatique**
   - Les erreurs sont capturées AVANT d'atteindre le service
   - Les messages sont clairs et en français

3. **Gestion d'erreurs unifiée**
   - Toutes les erreurs ont le même format (ErrorResponse)
   - Map de validationErrors pour erreurs multiples

4. **Cascade Contrat → Paiements**
   - Créer un contrat crée automatiquement ses paiements
   - Supprimer un contrat supprime automatiquement ses paiements

5. **READ-ONLY pour Paiements**
   - Pas de POST/PUT/DELETE sur /paiements
   - Consultation uniquement via GET

---

**Document:** Guide de Test - Chapitre 10  
**Date:** 12 Novembre 2024  
**Statut:** ✅ Prêt pour les tests

*Bon courage pour les tests! N'hésitez pas à prendre des captures d'écran détaillées.*
