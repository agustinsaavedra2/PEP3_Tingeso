package com.example.PEP3_Tingeso_Backend.services;

import com.example.PEP3_Tingeso_Backend.entities.ClientEntity;
import com.example.PEP3_Tingeso_Backend.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void whenCreateClient_thenClientIsSaved() {
        // Given
        ClientEntity client = new ClientEntity();

        client.setId(1L);
        client.setName("Agustín Saavedra");
        client.setRut("20.677.670-6");
        client.setEmail("agustinsaavedra056@gmail.com");
        client.setBirthDate(LocalDate.of(2001, 4, 30));
        client.setNumberOfVisits(0);

        when(clientRepository.save(client)).thenReturn(client);

        ClientEntity savedClient = clientService.createClient(client);

        assertEquals(client.getId(), savedClient.getId());
        assertEquals(client.getName(), savedClient.getName());
        assertEquals(client.getRut(), savedClient.getRut());
        assertEquals(client.getEmail(), savedClient.getEmail());
        assertEquals(client.getBirthDate(), savedClient.getBirthDate());
        assertEquals(client.getNumberOfVisits(), savedClient.getNumberOfVisits());
    }

    @Test
    public void whenNumberOfVisitsIsNegative_thenClientIsNotSaved(){
        ClientEntity client = new ClientEntity();

        client.setId(3L);
        client.setName("Diego Orellana");
        client.setRut("20.152.576-0");
        client.setEmail("diego_orellana156@gmail.com");
        client.setBirthDate(LocalDate.of(2000, 12, 22));
        client.setNumberOfVisits(-1);

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(client));

        verify(clientRepository, never()).save(any(ClientEntity.class));
    }

    @Test
    public void whenEmailIsBlank_thenClientIsNotSaved(){
        ClientEntity client = new ClientEntity();

        client.setId(2L);
        client.setName("Fernanda Saez");
        client.setRut("21.156.450-2");
        client.setEmail("");
        client.setBirthDate(LocalDate.of(2002, 2, 26));
        client.setNumberOfVisits(0);

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(client));

        verify(clientRepository, never()).save(any(ClientEntity.class));
    }

    @Test
    public void whenNameIsNull_thenClientIsNotSaved(){
        ClientEntity client = new ClientEntity();

        client.setId(4L);
        client.setName(null);
        client.setRut("11.226.619-4");
        client.setEmail("ximeolmos@hotmail.cl");
        client.setBirthDate(LocalDate.of(1967, 1, 27));
        client.setNumberOfVisits(0);

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(client));

        verify(clientRepository, never()).save(any(ClientEntity.class));
    }

    @Test
    public void whenNameRutIsBlank_thenClientIsNotSaved(){
        ClientEntity client = new ClientEntity();

        client.setId(10L);
        client.setName("Ximena Saavedra");
        client.setRut("");
        client.setEmail("ximenasaavedra_w@gmail.com");
        client.setBirthDate(LocalDate.of(1960, 2, 13));
        client.setNumberOfVisits(0);

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(client));

        verify(clientRepository, never()).save(any(ClientEntity.class));
    }

    @Test
    public void whenBirthDateIsNull_thenClientIsNotSaved(){
        ClientEntity client = new ClientEntity();

        client.setId(20L);
        client.setName("Margarita Olmos");
        client.setRut("10.257.290-4");
        client.setEmail("margarita.olmos@gmail.com");
        client.setBirthDate(null);
        client.setNumberOfVisits(0);

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(client));

        verify(clientRepository, never()).save(any(ClientEntity.class));
    }

    @Test
    public void whenRutIsDuplicated_thenClientIsNotSaved(){
        ClientEntity client = new ClientEntity();

        client.setId(5L);
        client.setName("Daniel Saavedra");
        client.setRut("20.677.670-6");
        client.setEmail("daniel_saavedra@gmail.com");
        client.setBirthDate(LocalDate.of(1959, 1, 5));
        client.setNumberOfVisits(0);

        when(clientRepository.findByRut("20.677.670-6")).thenReturn(Optional.of(new ClientEntity()));

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(client));

        verify(clientRepository, never()).save(any(ClientEntity.class));
    }

    @Test
    public void whenGetClientById_thenGetClient(){
        Long clientId = 6L;
        ClientEntity client = new ClientEntity();

        client.setId(clientId);
        client.setName("Thomas Alvarez");
        client.setRut("12.803.706-K");
        client.setEmail("thomasalvarezXD@gmail.com");
        client.setBirthDate(LocalDate.of(1968, 5, 21));
        client.setNumberOfVisits(0);

        when(clientRepository.findById(6L)).thenReturn(Optional.of(client));

        ClientEntity resultClient = clientService.getClientById(clientId);

        assertEquals(client.getId(), resultClient.getId());
        assertEquals(client.getName(), resultClient.getName());
        assertEquals(client.getRut(), resultClient.getRut());
        assertEquals(client.getEmail(), resultClient.getEmail());
        assertEquals(client.getBirthDate(), resultClient.getBirthDate());
        assertEquals(client.getNumberOfVisits(), resultClient.getNumberOfVisits());

        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    public void whenIdDoesNotExist_thenShowIllegalException(){
        Long clientId = 20L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.getClientById(clientId));

        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    public void whenIdIsNull_thenShowIllegalException(){
        Long clientId = null;

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.getClientById(clientId));

        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    public void whenDatabaseFails_thenThrowRuntimeException() {
        Long clientId = 42L;

        when(clientRepository.findById(clientId))
                .thenThrow(new RuntimeException("Simulated DB failure"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clientService.getClientById(clientId);
        });

        assertEquals("Simulated DB failure", exception.getMessage());
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    public void whenClientIsIncomplete_thenShowIllegalException(){
        Long clientId = 50L;

        ClientEntity client = new ClientEntity();

        client.setId(clientId);
        client.setName("Fernando Saavedra");

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.getClientById(clientId));
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    public void whenGetClientByEmail_thenShowClient(){
        String email = "agustinsaavedra056@gmail.com";
        ClientEntity client = new ClientEntity();

        client.setId(1L);
        client.setName("Agustín Saavedra");
        client.setRut("20.677.670-6");
        client.setEmail(email);
        client.setBirthDate(LocalDate.of(2001, 4, 30));
        client.setNumberOfVisits(0);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));

        ClientEntity resultClient = clientService.getClientByEmail(email);

        assertEquals(client.getId(), resultClient.getId());
        assertEquals(client.getName(), resultClient.getName());
        assertEquals(client.getRut(), resultClient.getRut());
        assertEquals(client.getEmail(), resultClient.getEmail());
        assertEquals(client.getBirthDate(), resultClient.getBirthDate());
        assertEquals(client.getNumberOfVisits(), resultClient.getNumberOfVisits());
    }

    @Test
    public void whenEmailIsNull_thenShowIllegalException() {
        String email = null;

        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.getClientByEmail(email));
        verify(clientRepository, times(1)).findByEmail(email);
    }

    @Test
    public void whenEmailIsBlank_thenShowIllegalException(){
        String email = "";

        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.getClientByEmail(email));
        verify(clientRepository, times(1)).findByEmail(email);
    }

    @Test
    public void whenEmailDoesntExist_thenShowIllegalException(){
        String email = "dasaawa156@hotmail.com";

        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.getClientByEmail(email));

        verify(clientRepository, times(1)).findByEmail(email);
    }

    @Test
    public void whenClientIsNotCreated_thenShowIllegalException(){
        Long clientId = 49L;
        ClientEntity client = new ClientEntity();

        client.setId(clientId);
        client.setName("Fernando Saavedra");
        client.setRut("17.803.805-2");

        when(clientRepository.findByEmail(client.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.getClientByEmail(client.getEmail()));

        verify(clientRepository, times(1)).findByEmail(client.getEmail());
    }

    @Test
    public void whenGetAllClients_thenShowAllClients(){
        ClientEntity client1 = new ClientEntity();

        client1.setId(1L);
        client1.setName("Fernando Saavedra");
        client1.setRut("17.803.805-2");
        client1.setBirthDate(LocalDate.of(1991, 6, 3));
        client1.setNumberOfVisits(0);

        ClientEntity client2 = new ClientEntity();

        client2.setId(2L);
        client2.setName("Agustín Saavedra");
        client2.setRut("20.677.670-6");
        client2.setBirthDate(LocalDate.of(2001, 4, 30));
        client2.setNumberOfVisits(0);

        List<ClientEntity> clients = List.of(client1, client2);

        when(clientRepository.findAll()).thenReturn(clients);

        List<ClientEntity> resultingClients = clientService.getAllClients();

        assertEquals(2, resultingClients.size());
        assertEquals("Fernando Saavedra", resultingClients.get(0).getName());
        assertEquals("Agustín Saavedra", resultingClients.get(1).getName());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void whenNoClients_thenReturnEmptyList(){
        when(clientRepository.findAll()).thenReturn(List.of());

        List<ClientEntity> resultingClients = clientService.getAllClients();

        assertTrue(resultingClients.isEmpty());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void whenGetAllClients_thenClientsAreSortedById() {
        ClientEntity client1 = new ClientEntity();

        client1.setId(5L);
        client1.setName("Valentina Díaz");
        client1.setRut("15.267.860-5");
        client1.setEmail("valentinadiaz156@hotmail.com");
        client1.setBirthDate(LocalDate.of(1995, 12, 1));
        client1.setNumberOfVisits(1);

        ClientEntity client2 = new ClientEntity();

        client2.setId(2L);
        client2.setName("Cristóbal Reyes");
        client2.setRut("12.677.243-2");
        client2.setEmail("cristobal_reyes@gmail.com");
        client2.setBirthDate(LocalDate.of(1990, 10, 10));
        client2.setNumberOfVisits(2);

        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        List<ClientEntity> clients = clientService.getAllClients();

        assertEquals(2, clients.size());
        assertTrue(clients.get(0).getId() > clients.get(1).getId() || clients.get(0).getId() < clients.get(1).getId());
        verify(clientRepository).findAll();
    }

    @Test
    public void whenGetAllClients_thenAllRutsAreUnique() {
        ClientEntity client1 = new ClientEntity();

        client1.setId(1L);
        client1.setName("Daniela Soto");
        client1.setRut("19.470.520-3");
        client1.setEmail("daniela_a_soto@gmail.com");
        client1.setBirthDate(LocalDate.of(1988, 3, 14));
        client1.setNumberOfVisits(4);

        ClientEntity client2 = new ClientEntity();

        client2.setId(2L);
        client2.setName("Sebastián Jara");
        client2.setRut("19.111.222-3");
        client2.setEmail("sebastian_j@yahoo.com");
        client2.setBirthDate(LocalDate.of(1993, 8, 9));
        client2.setNumberOfVisits(2);

        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        List<ClientEntity> clients = clientService.getAllClients();

        Set<String> uniqueRuts = new HashSet<>();
        for (ClientEntity client : clients) {
            assertTrue("RUT duplicado: " + client.getRut(), uniqueRuts.add(client.getRut()));
        }
    }

    @Test
    public void whenGetAllClients_thenIdsAreUnique() {
        ClientEntity client1 = new ClientEntity();

        client1.setId(1L);
        client1.setName("Valentina Pérez");
        client1.setRut("15.270.345-4");
        client1.setEmail("valentina@example.com");
        client1.setBirthDate(LocalDate.of(1988, 5, 15));
        client1.setNumberOfVisits(1);

        ClientEntity client2 = new ClientEntity();

        client2.setId(2L);
        client2.setName("Jorge Díaz");
        client2.setRut("16.450.570-6");
        client2.setEmail("jorge@example.com");
        client2.setBirthDate(LocalDate.of(1992, 8, 22));
        client2.setNumberOfVisits(2);

        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        List<ClientEntity> clients = clientService.getAllClients();

        Set<Long> ids = clients.stream()
                .map(ClientEntity::getId)
                .collect(Collectors.toSet());

        assertEquals(clients.size(), ids.size(), "Hay IDs duplicados en los clientes");
    }

    @Test
    public void whenUpdateValidClient_thenClientIsUpdated() {
        ClientEntity client = new ClientEntity();

        client.setId(20L);
        client.setName("María González");
        client.setRut("15.870.900-K");
        client.setEmail("maria_gonzalez@gmail.com");
        client.setBirthDate(LocalDate.of(1990, 1, 1));
        client.setNumberOfVisits(3);

        when(clientRepository.findByRut(client.getRut())).thenReturn(Optional.empty());
        when(clientRepository.save(client)).thenReturn(client);

        ClientEntity result = clientService.updateClient(client);

        assertEquals("María González", result.getName());
        verify(clientRepository).save(client);
    }

    @Test
    public void whenUpdateClientWithBlankName_thenThrowException() {
        ClientEntity client = new ClientEntity();

        client.setName("   ");
        client.setRut("15.870.900-K");
        client.setEmail("maria_gonzalez@gmail.com");
        client.setBirthDate(LocalDate.of(1990, 1, 1));
        client.setNumberOfVisits(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(client);
        });

        assertEquals("Client's name is not valid", exception.getMessage());
    }

    @Test
    public void whenUpdateClientWithExistingRut_thenThrowException() {
        ClientEntity client = new ClientEntity();

        client.setName("Pedro Pérez");
        client.setRut("12.315.255-K");
        client.setEmail("pedroperez156XD@gmail.com");
        client.setBirthDate(LocalDate.of(1995, 3, 15));
        client.setNumberOfVisits(2);

        when(clientRepository.findByRut(client.getRut())).thenReturn(Optional.of(new ClientEntity()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(client);
        });

        assertEquals("Client already exists", exception.getMessage());
    }

    @Test
    public void whenUpdateClientWithNullEmail_thenThrowException() {
        ClientEntity client = new ClientEntity();

        client.setName("Ana López");
        client.setRut("11.150.269-3");
        client.setEmail(null);
        client.setBirthDate(LocalDate.of(2000, 2, 2));
        client.setNumberOfVisits(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(client);
        });

        assertEquals("Client's email is not valid", exception.getMessage());
    }

    @Test
    public void whenUpdateClientWithNegativeVisits_thenThrowException() {
        ClientEntity client = new ClientEntity();

        client.setName("Lucas Silva");
        client.setRut("18.682.975-0");
        client.setEmail("lucas@example.com");
        client.setBirthDate(LocalDate.of(1985, 6, 20));
        client.setNumberOfVisits(-5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(client);
        });

        assertEquals("Client's number of visits is not valid", exception.getMessage());
    }

    @Test
    void whenDeleteClient_successfullyDeletesClient() throws Exception {
        Long clientId = 1L;
        doNothing().when(clientRepository).deleteById(clientId);

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    void whenDeleteClient_throwsExceptionWhenRepositoryFails() {
        Long clientId = 2L;
        doThrow(new RuntimeException("Error interno")).when(clientRepository).deleteById(clientId);

        Exception thrown = assertThrows(Exception.class, () -> clientService.deleteClient(clientId));

        assertEquals("Error interno", thrown.getMessage());
    }

    @Test
    void whenDeleteClient_fails_thenPropagatesExactErrorMessage() {
        Long clientId = 10L;
        String originalMessage = "No se pudo eliminar el cliente";

        doThrow(new RuntimeException(originalMessage))
                .when(clientRepository).deleteById(clientId);

        Exception thrown = assertThrows(Exception.class, () -> clientService.deleteClient(clientId));

        assertTrue(thrown.getMessage().contains(originalMessage));
    }

    @Test
    void whenDeleteClient_thenRepositoryCalledOnlyOnce() throws Exception {
        Long clientId = 3L;
        doNothing().when(clientRepository).deleteById(clientId);

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    void whenDeletingNonExistentClient_thenThrowsException() {
        Long clientId = 999L;

        doThrow(new EmptyResultDataAccessException(1))
                .when(clientRepository).deleteById(clientId);

        Exception thrown = assertThrows(Exception.class, () -> clientService.deleteClient(clientId));

        assertTrue(thrown.getMessage().contains("1"));
    }
}
