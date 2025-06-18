package com.example.PEP3_Tingeso_Backend.repositories;

import com.example.PEP3_Tingeso_Backend.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    public Optional<ClientEntity> findById(Long id);
    public Optional<ClientEntity> findByEmail(String email);
    public Optional<ClientEntity> findByRut(String rut);
}
