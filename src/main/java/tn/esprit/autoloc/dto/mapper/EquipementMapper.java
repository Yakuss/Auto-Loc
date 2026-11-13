package tn.esprit.autoloc.dto.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.dto.EquipementRequestDTO;
import tn.esprit.autoloc.dto.EquipementResponseDTO;

@Component
public class EquipementMapper {

    public Equipement toEntity(EquipementRequestDTO dto) {
        if (dto == null) return null;
        Equipement equipement = new Equipement();
        equipement.setLibelle(dto.getLibelle());
        return equipement;
    }

    public EquipementResponseDTO toResponseDTO(Equipement equipement) {
        if (equipement == null) return null;
        return EquipementResponseDTO.builder()
                .idEquipement(equipement.getIdEquipement())
                .libelle(equipement.getLibelle())
                .build();
    }

    public void updateEntityFromDTO(EquipementRequestDTO dto, Equipement equipement) {
        if (dto == null || equipement == null) return;
        if (dto.getLibelle() != null) equipement.setLibelle(dto.getLibelle());
    }
}
