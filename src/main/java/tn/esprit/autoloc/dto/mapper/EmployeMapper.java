package tn.esprit.autoloc.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.dto.EmployeRequestDTO;
import tn.esprit.autoloc.dto.EmployeResponseDTO;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

@Component
@RequiredArgsConstructor
public class EmployeMapper {

    private final IAgenceRepository agenceRepository;

    public Employe toEntity(EmployeRequestDTO dto) {
        if (dto == null) return null;
        
        Employe employe = new Employe();
        employe.setNom(dto.getNom());
        employe.setPrenom(dto.getPrenom());
        employe.setRole(dto.getRole());
        
        // Association avec Agence
        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Agence introuvable avec l'ID: " + dto.getAgenceId()));
            employe.setAgence(agence);
        }
        
        return employe;
    }

    public EmployeResponseDTO toResponseDTO(Employe employe) {
        if (employe == null) return null;
        
        EmployeResponseDTO.EmployeResponseDTOBuilder builder = EmployeResponseDTO.builder()
                .idEmploye(employe.getIdEmploye())
                .nom(employe.getNom())
                .prenom(employe.getPrenom())
                .role(employe.getRole());
        
        if (employe.getAgence() != null) {
            builder.agenceId(employe.getAgence().getIdAgence())
                   .agenceNom(employe.getAgence().getNom());
        }
        
        return builder.build();
    }

    public void updateEntityFromDTO(EmployeRequestDTO dto, Employe employe) {
        if (dto == null || employe == null) return;
        if (dto.getNom() != null) employe.setNom(dto.getNom());
        if (dto.getPrenom() != null) employe.setPrenom(dto.getPrenom());
        if (dto.getRole() != null) employe.setRole(dto.getRole());
    }
}
