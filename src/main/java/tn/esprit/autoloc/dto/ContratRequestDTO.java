package tn.esprit.autoloc.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratRequestDTO {

    @NotNull(message = "La date de signature est obligatoire")
    @PastOrPresent(message = "La date de signature ne peut pas être dans le futur")
    private LocalDate dateSignature;

    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant total doit être supérieur à 0")
    @Digits(integer = 10, fraction = 2, message = "Le montant doit avoir au maximum 10 chiffres avant et 2 après la virgule")
    private BigDecimal montantTotal;

    @NotNull(message = "Le statut de validité est obligatoire")
    private Boolean valide;

    @NotNull(message = "La réservation est obligatoire")
    private Long reservationId;

    // Paiements en cascade (optionnel lors de la création)
    @Valid
    @Builder.Default
    private List<PaiementDTO> paiements = new ArrayList<>();
}
