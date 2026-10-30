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
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;

import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;

    @PostMapping
    @Operation(summary = "Créer un contrat avec ses paiements (cascade)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Contrat> createContrat(@RequestBody Contrat contrat) {
        Contrat created = contratService.create(contrat);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un contrat par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<Contrat> getContratById(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        Contrat contrat = contratService.findById(id);
        return ResponseEntity.ok(contrat);
    }

    @GetMapping
    @Operation(summary = "Lister tous les contrats")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des contrats récupérée avec succès")
    })
    public ResponseEntity<List<Contrat>> getAllContrats() {
        List<Contrat> contrats = contratService.findAll();
        return ResponseEntity.ok(contrats);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Contrat> updateContrat(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id,
            @RequestBody Contrat contrat) {
        Contrat updated = contratService.update(id, contrat);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contrat (cascade supprime les paiements)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<Void> deleteContrat(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
