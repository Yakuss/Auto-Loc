package tn.esprit.autoloc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.autoloc.domain.RoleEmploye;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeResponseDTO {
    private Long idEmploye;
    private String nom;
    private String prenom;
    private RoleEmploye role;
    private Long agenceId;
    private String agenceNom;
}
