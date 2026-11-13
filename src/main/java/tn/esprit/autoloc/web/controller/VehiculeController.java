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
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.dto.VehiculeRequestDTO;
import tn.esprit.autoloc.dto.VehiculeResponseDTO;
import tn.esprit.autoloc.dto.mapper.VehiculeMapper;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Vehicules", description = "Gestion des véhicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;

    @PostMapping
    @Operation(summary = "Créer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<VehiculeResponseDTO> createVehicule(@Valid @RequestBody VehiculeRequestDTO requestDTO) {
        Vehicule vehicule = vehiculeMapper.toEntity(requestDTO);
        Vehicule created = vehiculeService.create(vehicule);
        VehiculeResponseDTO responseDTO = vehiculeMapper.toResponseDTO(created);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un véhicule par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public ResponseEntity<VehiculeResponseDTO> getVehiculeById(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        Vehicule vehicule = vehiculeService.findById(id);
        VehiculeResponseDTO responseDTO = vehiculeMapper.toResponseDTO(vehicule);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Lister tous les véhicules")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des véhicules récupérée avec succès")
    })
    public ResponseEntity<List<VehiculeResponseDTO>> getAllVehicules() {
        List<Vehicule> vehicules = vehiculeService.findAll();
        List<VehiculeResponseDTO> responseDTOs = vehicules.stream()
                .map(vehiculeMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<VehiculeResponseDTO> updateVehicule(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
            @Valid @RequestBody VehiculeRequestDTO requestDTO) {
        Vehicule updated = vehiculeService.update(id, vehiculeMapper.toEntity(requestDTO));
        VehiculeResponseDTO responseDTO = vehiculeMapper.toResponseDTO(updated);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public ResponseEntity<Void> deleteVehicule(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
