package com.example.PEP3_Tingeso_Backend.controllers;

import com.example.PEP3_Tingeso_Backend.entities.ClientEntity;
import com.example.PEP3_Tingeso_Backend.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/api/client")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/")
    public ResponseEntity<ClientEntity> createClient(@RequestBody ClientEntity client){
        ClientEntity newClient = clientService.createClient(client);
        return ResponseEntity.ok(newClient);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity <ClientEntity> getClientById(@PathVariable("id") Long id){
        ClientEntity client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientEntity> getClientByEmail(@PathVariable("email") String email){
        ClientEntity client = clientService.getClientByEmail(email);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/")
    public ResponseEntity<List<ClientEntity>> getAllClients(){
        List<ClientEntity> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/")
    public ResponseEntity<ClientEntity> updateClient(@RequestBody ClientEntity client){
        ClientEntity updatedClient = clientService.updateClient(client);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long id) throws Exception {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
