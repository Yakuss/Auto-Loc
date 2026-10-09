package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Paiement;
import java.util.List;
import java.util.Optional;

public interface IPaiementService {
    Paiement save(Paiement paiement);
    Optional<Paiement> findById(Long id);
    List<Paiement> findAll();
    void deleteById(Long id);
    long count();
}
