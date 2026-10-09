package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IEmployeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeService implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe save(Employe employe) {
        return employeRepository.save(employe);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employe> findById(Long id) {
        return employeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        employeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return employeRepository.count();
    }
}
