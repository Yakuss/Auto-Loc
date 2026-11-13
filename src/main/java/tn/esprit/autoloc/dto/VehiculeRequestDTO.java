package tn.esprit.autoloc.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

import java.math.BigDecimal;

/**
 * DTO pour la création et modification d'un véhicule
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeRequestDTO {

    @NotBlank(message = "L'immatriculation est obligatoire")
    @Size(min = 3, max = 20, message = "L'immatriculation doit contenir entre 3 et 20 caractères")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "L'immatriculation doit contenir uniquement des lettres majuscules, chiffres et tirets")
    private String immatriculation;

    @NotBlank(message = "La marque est obligatoire")
    @Size(min = 2, max = 50, message = "La marque doit contenir entre 2 et 50 caractères")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    @Size(min = 1, max = 50, message = "Le modèle doit contenir entre 1 et 50 caractères")
    private String modele;

    @NotNull(message = "La catégorie est obligatoire")
    private CategorieVehicule categorie;

    @NotNull(message = "Le tarif journalier est obligatoire")
    @DecimalMin(value = "0.01", message = "Le tarif journalier doit être supérieur à 0")
    @DecimalMax(value = "10000.00", message = "Le tarif journalier ne peut pas dépasser 10000")
    @Digits(integer = 5, fraction = 2, message = "Le tarif doit avoir au maximum 5 chiffres avant et 2 après la virgule")
    private BigDecimal tarifJournalier;

    @NotNull(message = "Le statut est obligatoire")
    private StatutVehicule statut;

    // ID de l'agence (optionnel)
    private Long agenceId;
}
