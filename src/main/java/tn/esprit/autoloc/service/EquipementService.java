package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementService implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement save(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipement> findById(Long id) {
        return equipementRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        equipementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return equipementRepository.count();
    }
}
