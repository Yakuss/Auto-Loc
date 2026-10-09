package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementService implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    public Paiement save(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paiement> findById(Long id) {
        return paiementRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        paiementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return paiementRepository.count();
    }
}
