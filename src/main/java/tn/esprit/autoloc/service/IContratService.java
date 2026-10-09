package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Contrat;
import java.util.List;
import java.util.Optional;

public interface IContratService {
    Contrat save(Contrat contrat);
    Optional<Contrat> findById(Long id);
    List<Contrat> findAll();
    void deleteById(Long id);
    long count();
}
