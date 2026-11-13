package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.dto.MaintenanceRequestDTO;
import tn.esprit.autoloc.dto.MaintenanceResponseDTO;
import tn.esprit.autoloc.dto.mapper.MaintenanceMapper;
import tn.esprit.autoloc.service.IMaintenanceService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Gestion des opérations de maintenance sur les véhicules")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;
    private final MaintenanceMapper maintenanceMapper;

    @PostMapping
    @Operation(summary = "Créer une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Maintenance créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (dates incohérentes)")
    })
    public ResponseEntity<MaintenanceResponseDTO> createMaintenance(@Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        Maintenance maintenance = maintenanceMapper.toEntity(requestDTO);
        Maintenance created = maintenanceService.create(maintenance);
        MaintenanceResponseDTO responseDTO = maintenanceMapper.toResponseDTO(created);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une maintenance par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance trouvée"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public ResponseEntity<MaintenanceResponseDTO> getMaintenanceById(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        Maintenance maintenance = maintenanceService.findById(id);
        MaintenanceResponseDTO responseDTO = maintenanceMapper.toResponseDTO(maintenance);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Lister toutes les maintenances")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des maintenances récupérée avec succès")
    })
    public ResponseEntity<List<MaintenanceResponseDTO>> getAllMaintenances() {
        List<Maintenance> maintenances = maintenanceService.findAll();
        List<MaintenanceResponseDTO> responseDTOs = maintenances.stream()
                .map(maintenanceMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance modifiée avec succès"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<MaintenanceResponseDTO> updateMaintenance(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id,
            @Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        Maintenance updated = maintenanceService.update(id, maintenanceMapper.toEntity(requestDTO));
        MaintenanceResponseDTO responseDTO = maintenanceMapper.toResponseDTO(updated);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Maintenance supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public ResponseEntity<Void> deleteMaintenance(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        maintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
