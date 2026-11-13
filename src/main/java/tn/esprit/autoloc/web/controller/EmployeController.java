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
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.dto.EmployeRequestDTO;
import tn.esprit.autoloc.dto.EmployeResponseDTO;
import tn.esprit.autoloc.dto.mapper.EmployeMapper;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(name = "Employes", description = "Gestion des employés")
public class EmployeController {

    private final IEmployeService employeService;
    private final EmployeMapper employeMapper;

    @PostMapping
    @Operation(summary = "Créer un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employé créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<EmployeResponseDTO> createEmploye(@Valid @RequestBody EmployeRequestDTO requestDTO) {
        Employe employe = employeMapper.toEntity(requestDTO);
        Employe created = employeService.create(employe);
        EmployeResponseDTO responseDTO = employeMapper.toResponseDTO(created);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un employé par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé trouvé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<EmployeResponseDTO> getEmployeById(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        Employe employe = employeService.findById(id);
        EmployeResponseDTO responseDTO = employeMapper.toResponseDTO(employe);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Lister tous les employés")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des employés récupérée avec succès")
    })
    public ResponseEntity<List<EmployeResponseDTO>> getAllEmployes() {
        List<Employe> employes = employeService.findAll();
        List<EmployeResponseDTO> responseDTOs = employes.stream()
                .map(employeMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<EmployeResponseDTO> updateEmploye(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id,
            @Valid @RequestBody EmployeRequestDTO requestDTO) {
        Employe updated = employeService.update(id, employeMapper.toEntity(requestDTO));
        EmployeResponseDTO responseDTO = employeMapper.toResponseDTO(updated);
        return ResponseEntity.ok(responseDTO);
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
