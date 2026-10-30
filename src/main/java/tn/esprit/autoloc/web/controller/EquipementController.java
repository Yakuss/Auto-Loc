package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Equipements", description = "Gestion des équipements disponibles")
public class EquipementController {

    private final IEquipementService equipementService;

    @PostMapping
    @Operation(summary = "Créer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Équipement créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (libellé vide)")
    })
    public ResponseEntity<Equipement> createEquipement(@RequestBody Equipement equipement) {
        Equipement created = equipementService.create(equipement);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un équipement par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement trouvé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    public ResponseEntity<Equipement> getEquipementById(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        Equipement equipement = equipementService.findById(id);
        return ResponseEntity.ok(equipement);
    }

    @GetMapping
    @Operation(summary = "Lister tous les équipements")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des équipements récupérée avec succès")
    })
    public ResponseEntity<List<Equipement>> getAllEquipements() {
        List<Equipement> equipements = equipementService.findAll();
        return ResponseEntity.ok(equipements);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Equipement> updateEquipement(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id,
            @RequestBody Equipement equipement) {
        Equipement updated = equipementService.update(id, equipement);
        return ResponseEntity.ok(updated);
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
