package com.example.PEP3_Tingeso_Backend.services;

import com.example.PEP3_Tingeso_Backend.entities.ClientEntity;
import com.example.PEP3_Tingeso_Backend.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public ClientEntity createClient(ClientEntity client) {
        if(client.getName() == null || client.getName().isBlank()){
            throw new IllegalArgumentException("Client's name is not valid");
        }

        if(client.getRut() == null || client.getRut().isBlank()){
            throw new IllegalArgumentException("Client's RUT is not valid");
        }

        if(clientRepository.findByRut(client.getRut()).isPresent()){
            throw new IllegalArgumentException("Client already exists");
        }

        if(client.getEmail() == null || client.getEmail().isBlank()){
            throw new IllegalArgumentException("Client's email is not valid");
        }

        if(client.getBirthDate() == null){
            throw new IllegalArgumentException("Client's email is not valid");
        }

        if(client.getNumberOfVisits() < 0){
            throw new IllegalArgumentException("Client's number of visits is not valid");
        }

        return clientRepository.save(client);
    }

    public ClientEntity getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("Client not found"));
    }

    public ClientEntity getClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Client not found")
        );
    }

    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    public ClientEntity updateClient(ClientEntity client) {
        if(client.getName() == null || client.getName().isBlank()){
            throw new IllegalArgumentException("Client's name is not valid");
        }

        if(client.getRut() == null || client.getRut().isBlank()){
            throw new IllegalArgumentException("Client's RUT is not valid");
        }

        if(clientRepository.findByRut(client.getRut()).isPresent()){
            throw new IllegalArgumentException("Client already exists");
        }

        if(client.getEmail() == null || client.getEmail().isBlank()){
            throw new IllegalArgumentException("Client's email is not valid");
        }

        if(client.getBirthDate() == null){
            throw new IllegalArgumentException("Client's email is not valid");
        }

        if(client.getNumberOfVisits() < 0){
            throw new IllegalArgumentException("Client's number of visits is not valid");
        }

        client.setName(client.getName());
        client.setRut(client.getRut());
        client.setEmail(client.getEmail());
        client.setBirthDate(client.getBirthDate());
        client.setNumberOfVisits(client.getNumberOfVisits());

        return clientRepository.save(client);
    }

    public void deleteClient(Long id) throws Exception {
        try{
            clientRepository.deleteById(id);
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
