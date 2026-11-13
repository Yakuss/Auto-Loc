package tn.esprit.autoloc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgenceResponseDTO {
    
    private Long idAgence;
    private String nom;
    private String ville;
    private String adresse;
    private String telephone;
}
