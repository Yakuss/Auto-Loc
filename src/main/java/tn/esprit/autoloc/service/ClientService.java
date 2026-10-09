package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientService implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client save(Client client) {
        log.info("Création d'un nouveau client : {} {}", client.getNom(), client.getPrenom());
        return clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findById(Long id) {
        log.info("Recherche du client avec ID : {}", id);
        return clientRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        log.info("Récupération de tous les clients");
        return clientRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {

        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        long count = clientRepository.count();
        log.info("Nombre total de clients : {}", count);
        return count;
    }

    @Override
    public Client update(Long id, Client client) {
        log.info("Mise à jour du client avec ID : {}", id);
        return clientRepository.findById(id)
                .map(existingClient -> {
                    existingClient.setNom(client.getNom());
                    existingClient.setPrenom(client.getPrenom());
                    existingClient.setEmail(client.getEmail());
                    existingClient.setTelephone(client.getTelephone());
                    existingClient.setNumPermis(client.getNumPermis());
                    existingClient.setDateInscription(client.getDateInscription());
                    return clientRepository.save(existingClient);
                })
                .orElseThrow(() -> new RuntimeException("Client avec ID " + id + " introuvable"));
    }
}
