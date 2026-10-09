package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Agence;
import java.util.List;
import java.util.Optional;

public interface IAgenceService {
    Agence save(Agence agence);
    Optional<Agence> findById(Long id);
    List<Agence> findAll();
    void deleteById(Long id);
    long count();
}
