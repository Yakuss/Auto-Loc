package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Employe;
import java.util.List;
import java.util.Optional;

public interface IEmployeService {
    Employe save(Employe employe);
    Optional<Employe> findById(Long id);
    List<Employe> findAll();
    void deleteById(Long id);
    long count();
}
