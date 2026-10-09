package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenceService implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public Agence save(Agence agence) {
        return agenceRepository.save(agence);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Agence> findById(Long id) {
        return agenceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        agenceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return agenceRepository.count();
    }
}
