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
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.dto.EquipementRequestDTO;
import tn.esprit.autoloc.dto.EquipementResponseDTO;
import tn.esprit.autoloc.dto.mapper.EquipementMapper;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Equipements", description = "Gestion des équipements disponibles")
public class EquipementController {

    private final IEquipementService equipementService;
    private final EquipementMapper equipementMapper;

    @PostMapping
    @Operation(summary = "Créer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Équipement créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (libellé vide)")
    })
    public ResponseEntity<EquipementResponseDTO> createEquipement(@Valid @RequestBody EquipementRequestDTO requestDTO) {
        Equipement equipement = equipementMapper.toEntity(requestDTO);
        Equipement created = equipementService.create(equipement);
        EquipementResponseDTO responseDTO = equipementMapper.toResponseDTO(created);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un équipement par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement trouvé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    public ResponseEntity<EquipementResponseDTO> getEquipementById(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        Equipement equipement = equipementService.findById(id);
        EquipementResponseDTO responseDTO = equipementMapper.toResponseDTO(equipement);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Lister tous les équipements")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des équipements récupérée avec succès")
    })
    public ResponseEntity<List<EquipementResponseDTO>> getAllEquipements() {
        List<Equipement> equipements = equipementService.findAll();
        List<EquipementResponseDTO> responseDTOs = equipements.stream()
                .map(equipementMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<EquipementResponseDTO> updateEquipement(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id,
            @Valid @RequestBody EquipementRequestDTO requestDTO) {
        Equipement updated = equipementService.update(id, equipementMapper.toEntity(requestDTO));
        EquipementResponseDTO responseDTO = equipementMapper.toResponseDTO(updated);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Équipement supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    public ResponseEntity<Void> deleteEquipement(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        equipementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
