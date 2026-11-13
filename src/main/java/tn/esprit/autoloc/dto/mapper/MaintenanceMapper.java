package tn.esprit.autoloc.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.dto.MaintenanceRequestDTO;
import tn.esprit.autoloc.dto.MaintenanceResponseDTO;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Component
@RequiredArgsConstructor
public class MaintenanceMapper {

    private final IVehiculeRepository vehiculeRepository;

    public Maintenance toEntity(MaintenanceRequestDTO dto) {
        if (dto == null) return null;
        
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(dto.getDateDebut());
        maintenance.setDateFin(dto.getDateFin());
        maintenance.setDescription(dto.getDescription());
        
        // Association avec Vehicule
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Vehicule introuvable avec l'ID: " + dto.getVehiculeId()));
            maintenance.setVehicule(vehicule);
        }
        
        return maintenance;
    }

    public MaintenanceResponseDTO toResponseDTO(Maintenance maintenance) {
        if (maintenance == null) return null;
        
        MaintenanceResponseDTO.MaintenanceResponseDTOBuilder builder = MaintenanceResponseDTO.builder()
                .idMaintenance(maintenance.getIdMaintenance())
                .dateDebut(maintenance.getDateDebut())
                .dateFin(maintenance.getDateFin())
                .description(maintenance.getDescription());
        
        // Info véhicule
        if (maintenance.getVehicule() != null) {
            builder.vehiculeId(maintenance.getVehicule().getIdVehicule())
                   .vehiculeImmatriculation(maintenance.getVehicule().getImmatriculation())
                   .vehiculeMarque(maintenance.getVehicule().getMarque());
        }
        
        return builder.build();
    }

    public void updateEntityFromDTO(MaintenanceRequestDTO dto, Maintenance maintenance) {
        if (dto == null || maintenance == null) return;
        
        if (dto.getDateDebut() != null) maintenance.setDateDebut(dto.getDateDebut());
        if (dto.getDateFin() != null) maintenance.setDateFin(dto.getDateFin());
        if (dto.getDescription() != null) maintenance.setDescription(dto.getDescription());
    }
}
