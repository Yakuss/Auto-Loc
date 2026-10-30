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
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(name = "Employes", description = "Gestion des employés")
public class EmployeController {

    private final IEmployeService employeService;

    @PostMapping
    @Operation(summary = "Créer un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employé créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Employe> createEmploye(@RequestBody Employe employe) {
        Employe created = employeService.create(employe);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un employé par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé trouvé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Employe> getEmployeById(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        Employe employe = employeService.findById(id);
        return ResponseEntity.ok(employe);
    }

    @GetMapping
    @Operation(summary = "Lister tous les employés")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des employés récupérée avec succès")
    })
    public ResponseEntity<List<Employe>> getAllEmployes() {
        List<Employe> employes = employeService.findAll();
        return ResponseEntity.ok(employes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Employe> updateEmploye(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id,
            @RequestBody Employe employe) {
        Employe updated = employeService.update(id, employe);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employé supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Void> deleteEmploye(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
