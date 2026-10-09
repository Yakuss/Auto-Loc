package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VehiculeService implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Vehicule save(Vehicule vehicule) {
        log.info("Création d'un nouveau véhicule : {}", vehicule.getImmatriculation());
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicule> findById(Long id) {
        log.info("Recherche du véhicule avec ID : {}", id);
        return vehiculeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findAll() {
        log.info("Récupération de tous les véhicules");
        return vehiculeRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du véhicule avec ID : {}", id);

        vehiculeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        long count = vehiculeRepository.count();
        log.info("Nombre total de véhicules : {}", count);
        return count;
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        log.info("Mise à jour du véhicule avec ID : {}", id);
        return vehiculeRepository.findById(id)
                .map(existingVehicule -> {
                    existingVehicule.setImmatriculation(vehicule.getImmatriculation());
                    existingVehicule.setMarque(vehicule.getMarque());
                    existingVehicule.setModele(vehicule.getModele());
                    existingVehicule.setCategorie(vehicule.getCategorie());
                    existingVehicule.setTarifJournalier(vehicule.getTarifJournalier());
                    existingVehicule.setStatut(vehicule.getStatut());
                    return vehiculeRepository.save(existingVehicule);
                })
                .orElseThrow(() -> new RuntimeException("Véhicule avec ID " + id + " introuvable"));
    }
}
