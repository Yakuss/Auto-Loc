package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        // Validation: refuse un id non null
        if (client.getIdClient() != null) {
            throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant");
        }
        
        // Règle métier: refuser un client dont un attribut d'identification est vide
        if (client.getNom() == null || client.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du client ne peut pas être vide");
        }
        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email du client ne peut pas être vide");
        }
        
        log.info("Création d'un nouveau client : {} {}", client.getNom(), client.getPrenom());
        return clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        log.info("Recherche du client avec ID : {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        log.info("Récupération de tous les clients");
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client client) {
        log.info("Mise à jour du client avec ID : {}", id);
        
        // Charger l'existant
        Client existant = findById(id);
        
        // Validation des données
        if (client.getNom() != null && !client.getNom().trim().isEmpty()) {
            existant.setNom(client.getNom());
        }
        if (client.getPrenom() != null) {
            existant.setPrenom(client.getPrenom());
        }
        if (client.getEmail() != null && !client.getEmail().trim().isEmpty()) {
            existant.setEmail(client.getEmail());
        }
        if (client.getTelephone() != null) {
            existant.setTelephone(client.getTelephone());
        }
        if (client.getNumPermis() != null) {
            existant.setNumPermis(client.getNumPermis());
        }
        if (client.getDateInscription() != null) {
            existant.setDateInscription(client.getDateInscription());
        }
        
        return clientRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du client avec ID : {}", id);
        
        // Vérifier l'existence avant suppression
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        
        clientRepository.deleteById(id);
    }
}
