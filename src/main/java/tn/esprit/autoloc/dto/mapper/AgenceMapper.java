package tn.esprit.autoloc.dto.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.dto.AgenceRequestDTO;
import tn.esprit.autoloc.dto.AgenceResponseDTO;

@Component
public class AgenceMapper {

    public Agence toEntity(AgenceRequestDTO dto) {
        if (dto == null) return null;

        Agence agence = new Agence();
        agence.setNom(dto.getNom());
        agence.setVille(dto.getVille());
        agence.setAdresse(dto.getAdresse());
        agence.setTelephone(dto.getTelephone());
        return agence;
    }

    public AgenceResponseDTO toResponseDTO(Agence agence) {
        if (agence == null) return null;

        return AgenceResponseDTO.builder()
                .idAgence(agence.getIdAgence())
                .nom(agence.getNom())
                .ville(agence.getVille())
                .adresse(agence.getAdresse())
                .telephone(agence.getTelephone())
                .build();
    }

    public void updateEntityFromDTO(AgenceRequestDTO dto, Agence agence) {
        if (dto == null || agence == null) return;

        if (dto.getNom() != null) agence.setNom(dto.getNom());
        if (dto.getVille() != null) agence.setVille(dto.getVille());
        if (dto.getAdresse() != null) agence.setAdresse(dto.getAdresse());
        if (dto.getTelephone() != null) agence.setTelephone(dto.getTelephone());
    }
}
