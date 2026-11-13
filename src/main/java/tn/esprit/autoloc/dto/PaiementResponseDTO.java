package tn.esprit.autoloc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.autoloc.domain.ModePaiement;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour les réponses contenant un paiement (READ-ONLY)
 * Les paiements sont gérés via les contrats (cascade)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaiementResponseDTO {
    private Long idPaiement;
    private BigDecimal montant;
    private LocalDate datePaiement;
    private ModePaiement modePaiement;
    
    // Info contrat associé
    private Long contratId;
}
