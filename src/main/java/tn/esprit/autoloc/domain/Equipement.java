package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement;
    private String libelle;

    @ManyToMany(mappedBy = "equipements")
    private Set<Vehicule> vehicules = new HashSet<>();
}
