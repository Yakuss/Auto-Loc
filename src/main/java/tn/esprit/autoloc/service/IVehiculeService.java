package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Vehicule;
import java.util.List;
import java.util.Optional;

public interface IVehiculeService {
    Vehicule save(Vehicule vehicule);
    Optional<Vehicule> findById(Long id);
    List<Vehicule> findAll();
    void deleteById(Long id);
    long count();
    Vehicule update(Long id, Vehicule vehicule);
}
