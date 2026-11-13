package tn.esprit.autoloc.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.dto.VehiculeRequestDTO;
import tn.esprit.autoloc.dto.VehiculeResponseDTO;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

/**
 * Mapper pour convertir entre Vehicule et ses DTOs
 */
@Component
@RequiredArgsConstructor
public class VehiculeMapper {

    private final IAgenceRepository agenceRepository;

    /**
     * Convertit un RequestDTO en entité Vehicule
     */
    public Vehicule toEntity(VehiculeRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setCategorie(dto.getCategorie());
        vehicule.setTarifJournalier(dto.getTarifJournalier());
        vehicule.setStatut(dto.getStatut());

        // Association avec Agence
        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Agence introuvable avec l'ID: " + dto.getAgenceId()));
            vehicule.setAgence(agence);
        }

        return vehicule;
    }

    /**
     * Convertit une entité Vehicule en ResponseDTO
     */
    public VehiculeResponseDTO toResponseDTO(Vehicule vehicule) {
        if (vehicule == null) {
            return null;
        }

        VehiculeResponseDTO.VehiculeResponseDTOBuilder builder = VehiculeResponseDTO.builder()
                .idVehicule(vehicule.getIdVehicule())
                .immatriculation(vehicule.getImmatriculation())
                .marque(vehicule.getMarque())
                .modele(vehicule.getModele())
                .categorie(vehicule.getCategorie())
                .tarifJournalier(vehicule.getTarifJournalier())
                .statut(vehicule.getStatut());

        // Ajouter les infos de l'agence si présente
        if (vehicule.getAgence() != null) {
            builder.agenceId(vehicule.getAgence().getIdAgence())
                   .agenceNom(vehicule.getAgence().getNom());
        }

        return builder.build();
    }

    /**
     * Met à jour une entité existante avec les données du DTO
     */
    public void updateEntityFromDTO(VehiculeRequestDTO dto, Vehicule vehicule) {
        if (dto == null || vehicule == null) {
            return;
        }

        if (dto.getImmatriculation() != null) {
            vehicule.setImmatriculation(dto.getImmatriculation());
        }
        if (dto.getMarque() != null) {
            vehicule.setMarque(dto.getMarque());
        }
        if (dto.getModele() != null) {
            vehicule.setModele(dto.getModele());
        }
        if (dto.getCategorie() != null) {
            vehicule.setCategorie(dto.getCategorie());
        }
        if (dto.getTarifJournalier() != null) {
            vehicule.setTarifJournalier(dto.getTarifJournalier());
        }
        if (dto.getStatut() != null) {
            vehicule.setStatut(dto.getStatut());
        }
    }
}
