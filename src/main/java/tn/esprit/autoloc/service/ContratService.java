package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratService implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat save(Contrat contrat) {
        return contratRepository.save(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contrat> findById(Long id) {
        return contratRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        contratRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return contratRepository.count();
    }
}
