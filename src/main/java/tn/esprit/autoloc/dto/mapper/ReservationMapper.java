package tn.esprit.autoloc.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.dto.ReservationRequestDTO;
import tn.esprit.autoloc.dto.ReservationResponseDTO;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;

    public Reservation toEntity(ReservationRequestDTO dto) {
        if (dto == null) return null;
        
        Reservation reservation = new Reservation();
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(dto.getStatut());
        
        // Association avec Client
        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Client introuvable avec l'ID: " + dto.getClientId()));
            reservation.setClient(client);
        }
        
        // Association avec Vehicule
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Vehicule introuvable avec l'ID: " + dto.getVehiculeId()));
            reservation.setVehicule(vehicule);
        }
        
        return reservation;
    }

    public ReservationResponseDTO toResponseDTO(Reservation reservation) {
        if (reservation == null) return null;
        
        ReservationResponseDTO.ReservationResponseDTOBuilder builder = ReservationResponseDTO.builder()
                .idReservation(reservation.getIdReservation())
                .dateDebut(reservation.getDateDebut())
                .dateFin(reservation.getDateFin())
                .statut(reservation.getStatut());
        
        // Info client
        if (reservation.getClient() != null) {
            builder.clientId(reservation.getClient().getIdClient())
                   .clientNom(reservation.getClient().getNom())
                   .clientPrenom(reservation.getClient().getPrenom());
        }
        
        // Info véhicule
        if (reservation.getVehicule() != null) {
            builder.vehiculeId(reservation.getVehicule().getIdVehicule())
                   .vehiculeImmatriculation(reservation.getVehicule().getImmatriculation())
                   .vehiculeMarque(reservation.getVehicule().getMarque())
                   .vehiculeModele(reservation.getVehicule().getModele());
        }
        
        return builder.build();
    }

    public void updateEntityFromDTO(ReservationRequestDTO dto, Reservation reservation) {
        if (dto == null || reservation == null) return;
        
        if (dto.getDateDebut() != null) reservation.setDateDebut(dto.getDateDebut());
        if (dto.getDateFin() != null) reservation.setDateFin(dto.getDateFin());
        if (dto.getStatut() != null) reservation.setStatut(dto.getStatut());
    }
}
