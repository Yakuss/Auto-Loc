package tn.esprit.autoloc.dto.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.dto.ClientRequestDTO;
import tn.esprit.autoloc.dto.ClientResponseDTO;

import java.time.LocalDate;

/**
 * Mapper pour convertir entre Client et ses DTOs
 */
@Component
public class ClientMapper {

    public Client toEntity(ClientRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Client client = new Client();
        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setEmail(dto.getEmail());
        client.setTelephone(dto.getTelephone());
        client.setNumPermis(dto.getNumPermis());
        client.setDateInscription(dto.getDateInscription() != null ? dto.getDateInscription() : LocalDate.now());

        return client;
    }

    public ClientResponseDTO toResponseDTO(Client client) {
        if (client == null) {
            return null;
        }

        return ClientResponseDTO.builder()
                .idClient(client.getIdClient())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .telephone(client.getTelephone())
                .numPermis(client.getNumPermis())
                .dateInscription(client.getDateInscription())
                .build();
    }

    public void updateEntityFromDTO(ClientRequestDTO dto, Client client) {
        if (dto == null || client == null) {
            return;
        }

        if (dto.getNom() != null) {
            client.setNom(dto.getNom());
        }
        if (dto.getPrenom() != null) {
            client.setPrenom(dto.getPrenom());
        }
        if (dto.getEmail() != null) {
            client.setEmail(dto.getEmail());
        }
        if (dto.getTelephone() != null) {
            client.setTelephone(dto.getTelephone());
        }
        if (dto.getNumPermis() != null) {
            client.setNumPermis(dto.getNumPermis());
        }
        if (dto.getDateInscription() != null) {
            client.setDateInscription(dto.getDateInscription());
        }
    }
}
