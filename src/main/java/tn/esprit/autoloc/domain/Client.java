package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String numPermis;
    private LocalDate dateInscription;

    // Client 1 --- * Reservation (bidirectionnelle)
    @OneToMany(mappedBy = "client")
    private Set<Reservation> reservations = new HashSet<>();
}
