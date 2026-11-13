package tn.esprit.autoloc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

import java.math.BigDecimal;

/**
 * DTO pour les réponses contenant un véhicule
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeResponseDTO {
    
    private Long idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;
    private CategorieVehicule categorie;
    private BigDecimal tarifJournalier;
    private StatutVehicule statut;
    
    // Information simplifiée sur l'agence (si présente)
    private Long agenceId;
    private String agenceNom;
}
