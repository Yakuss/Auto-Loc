package tn.esprit.autoloc.dto.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.dto.PaiementResponseDTO;

/**
 * Mapper pour Paiement (READ-ONLY - pas de RequestDTO car géré via Contrat)
 */
@Component
public class PaiementMapper {

    public PaiementResponseDTO toResponseDTO(Paiement paiement) {
        if (paiement == null) return null;
        
        PaiementResponseDTO.PaiementResponseDTOBuilder builder = PaiementResponseDTO.builder()
                .idPaiement(paiement.getIdPaiement())
                .montant(paiement.getMontant())
                .datePaiement(paiement.getDatePaiement())
                .modePaiement(paiement.getModePaiement());
        
        // Info contrat
        if (paiement.getContrat() != null) {
            builder.contratId(paiement.getContrat().getIdContrat());
        }
        
        return builder.build();
    }
}
