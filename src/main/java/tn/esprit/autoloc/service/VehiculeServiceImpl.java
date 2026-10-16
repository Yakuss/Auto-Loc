package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        // Validation: refuse un id non null
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException("Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        
        // Règle métier: refuser une valeur numérique négative
        if (vehicule.getTarifJournalier() != null && vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
        
        log.info("Création d'un nouveau véhicule : {}", vehicule.getImmatriculation());
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule findById(Long id) {
        log.info("Recherche du véhicule avec ID : {}", id);
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findAll() {
        log.info("Récupération de tous les véhicules");
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        log.info("Mise à jour du véhicule avec ID : {}", id);
        
        // Charger l'existant
        Vehicule existant = findById(id);
        
        // Recopier les attributs modifiables
        if (vehicule.getImmatriculation() != null) {
            existant.setImmatriculation(vehicule.getImmatriculation());
        }
        if (vehicule.getMarque() != null) {
            existant.setMarque(vehicule.getMarque());
        }
        if (vehicule.getModele() != null) {
            existant.setModele(vehicule.getModele());
        }
        if (vehicule.getCategorie() != null) {
            existant.setCategorie(vehicule.getCategorie());
        }
        if (vehicule.getTarifJournalier() != null) {
            // Validation
            if (vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
            }
            existant.setTarifJournalier(vehicule.getTarifJournalier());
        }
        if (vehicule.getStatut() != null) {
            existant.setStatut(vehicule.getStatut());
        }
        
        return vehiculeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du véhicule avec ID : {}", id);
        
        // Vérifier l'existence avant suppression
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        
        vehiculeRepository.deleteById(id);
    }
}
