package tn.esprit.autoloc.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.dto.ContratRequestDTO;
import tn.esprit.autoloc.dto.ContratResponseDTO;
import tn.esprit.autoloc.dto.PaiementDTO;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContratMapper {

    private final PaiementMapper paiementMapper;
    private final IReservationRepository reservationRepository;

    public Contrat toEntity(ContratRequestDTO dto) {
        if (dto == null) return null;
        
        Contrat contrat = new Contrat();
        contrat.setDateSignature(dto.getDateSignature());
        contrat.setMontantTotal(dto.getMontantTotal());
        contrat.setValide(dto.getValide());
        
        // Association avec Reservation
        if (dto.getReservationId() != null) {
            Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Reservation introuvable avec l'ID: " + dto.getReservationId()));
            contrat.setReservation(reservation);
            reservation.setContrat(contrat);
        }
        
        // Créer les paiements associés (cascade)
        if (dto.getPaiements() != null && !dto.getPaiements().isEmpty()) {
            for (PaiementDTO paiementDTO : dto.getPaiements()) {
                Paiement paiement = new Paiement();
                paiement.setMontant(paiementDTO.getMontant());
                paiement.setDatePaiement(paiementDTO.getDatePaiement());
                paiement.setModePaiement(paiementDTO.getModePaiement());
                paiement.setContrat(contrat);
                contrat.getPaiements().add(paiement);
            }
        }
        
        return contrat;
    }

    public ContratResponseDTO toResponseDTO(Contrat contrat) {
        if (contrat == null) return null;
        
        ContratResponseDTO.ContratResponseDTOBuilder builder = ContratResponseDTO.builder()
                .idContrat(contrat.getIdContrat())
                .dateSignature(contrat.getDateSignature())
                .montantTotal(contrat.getMontantTotal())
                .valide(contrat.getValide());
        
        // Info réservation
        if (contrat.getReservation() != null) {
            builder.reservationId(contrat.getReservation().getIdReservation());
        }
        
        // Liste des paiements
        if (contrat.getPaiements() != null) {
            builder.paiements(
                contrat.getPaiements().stream()
                    .map(paiementMapper::toResponseDTO)
                    .collect(Collectors.toList())
            );
        } else {
            builder.paiements(new ArrayList<>());
        }
        
        return builder.build();
    }

    public void updateEntityFromDTO(ContratRequestDTO dto, Contrat contrat) {
        if (dto == null || contrat == null) return;
        
        if (dto.getDateSignature() != null) contrat.setDateSignature(dto.getDateSignature());
        if (dto.getMontantTotal() != null) contrat.setMontantTotal(dto.getMontantTotal());
        if (dto.getValide() != null) contrat.setValide(dto.getValide());
        
        // Note: Les paiements ne sont pas modifiables via PUT (composition forte)
    }
}
