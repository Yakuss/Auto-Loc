package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat create(Contrat contrat) {
        // Validation: refuse un id non null
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        
        // Règle métier: refuser un montant total négatif
        if (contrat.getMontantTotal() != null && contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
        
        return contratRepository.save(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        // Charger l'existant
        Contrat existant = findById(id);
        
        // Recopier les attributs modifiables (sans toucher à l'id ni à la collection de paiements)
        if (contrat.getMontantTotal() != null) {
            if (contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
            }
            existant.setMontantTotal(contrat.getMontantTotal());
        }
        if (contrat.getDateSignature() != null) {
            existant.setDateSignature(contrat.getDateSignature());
        }
        // valide est un boolean primitif, on peut toujours le mettre à jour
        existant.setValide(contrat.isValide());
        
        return contratRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        // Vérifier l'existence avant suppression
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        
        // La suppression d'un contrat supprime ses paiements par cascade
        contratRepository.deleteById(id);
    }
}
