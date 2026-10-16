package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;

    @Override
    public Reservation create(Reservation reservation) {
        // Validation: refuse un id non null
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        
        // Règle métier: refuser une date de fin antérieure à la date de début
        if (reservation.getDateDebut() != null && reservation.getDateFin() != null) {
            if (reservation.getDateFin().isBefore(reservation.getDateDebut())) {
                throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
            }
        }
        
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        Reservation existant = findById(id);
        
        if (reservation.getDateDebut() != null) {
            existant.setDateDebut(reservation.getDateDebut());
        }
        if (reservation.getDateFin() != null) {
            existant.setDateFin(reservation.getDateFin());
        }
        
        // Vérifier la cohérence des dates après mise à jour
        if (existant.getDateDebut() != null && existant.getDateFin() != null) {
            if (existant.getDateFin().isBefore(existant.getDateDebut())) {
                throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
            }
        }
        
        if (reservation.getStatut() != null) {
            existant.setStatut(reservation.getStatut());
        }
        
        return reservationRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }
}
