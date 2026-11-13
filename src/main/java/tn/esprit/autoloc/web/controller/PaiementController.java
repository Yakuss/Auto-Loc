package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.dto.PaiementResponseDTO;
import tn.esprit.autoloc.dto.mapper.PaiementMapper;
import tn.esprit.autoloc.service.IPaiementService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Consultation des paiements (lecture seule - gérés via Contrats)")
public class PaiementController {

    private final IPaiementService paiementService;
    private final PaiementMapper paiementMapper;

    // Lecture seule: aucune création, modification, ni suppression
    // Les paiements sont gérés via les contrats (cascade)

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un paiement par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    public ResponseEntity<PaiementResponseDTO> getPaiementById(
            @Parameter(description = "Identifiant du paiement") @PathVariable Long id) {
        Paiement paiement = paiementService.findById(id);
        PaiementResponseDTO responseDTO = paiementMapper.toResponseDTO(paiement);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Lister tous les paiements")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des paiements récupérée avec succès")
    })
    public ResponseEntity<List<PaiementResponseDTO>> getAllPaiements() {
        List<Paiement> paiements = paiementService.findAll();
        List<PaiementResponseDTO> responseDTOs = paiements.stream()
                .map(paiementMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }
}
