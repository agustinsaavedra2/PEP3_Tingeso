package com.example.PEP3_Tingeso_Backend.services;

import com.example.PEP3_Tingeso_Backend.entities.BookingEntity;
import com.example.PEP3_Tingeso_Backend.entities.ClientEntity;
import com.example.PEP3_Tingeso_Backend.repositories.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @InjectMocks
    BookingService bookingService;

    @Test
    void whenCreateBooking_thenBookingSuccessfully() {
        ClientEntity client1 = new ClientEntity();
        client1.setId(1L);
        client1.setName("John Doe");
        client1.setRut("12.345.678-9");
        client1.setEmail("john.doe@example.com");
        client1.setBirthDate(LocalDate.of(1990, 5, 15));
        client1.setNumberOfVisits(5);

        ClientEntity client2 = new ClientEntity();

        client2.setId(2L);
        client2.setName("Jane Smith");
        client2.setRut("19.765.432-1");
        client2.setEmail("jane.smith@example.com");
        client2.setBirthDate(LocalDate.of(1985, 8, 30));
        client2.setNumberOfVisits(3);

        BookingEntity booking = new BookingEntity();

        booking.setId(1L);
        booking.setNameBooking("Jane Smith");
        booking.setLapsNumber(10);
        booking.setMaximumTime(20);
        booking.setBookingDate(LocalDate.of(2025, 4, 28));
        booking.setBookingTime(LocalTime.of(15, 0));
        booking.setTotalDuration(90);
        booking.setBasePrice(20000.0);
        booking.setDiscountByPeopleNumber(2000.0);
        booking.setDiscountByFrequentCustomer(2000.0);
        booking.setDiscountBySpecialDays(0.0);

        booking.setClients(List.of(client1, client2));

        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(booking);

        BookingEntity savedBooking = bookingService.createBooking(booking);

        assertNotNull(savedBooking);
        assertEquals("Jane Smith", savedBooking.getNameBooking());

        assertEquals(6, client1.getNumberOfVisits());
        assertEquals(4, client2.getNumberOfVisits());
    }

    @Test
    void whenCreateBookingWithEmptyName_thenBookingFails() {
        BookingEntity booking = new BookingEntity();
        ClientEntity client1 = new ClientEntity();
        client1.setId(1L);
        client1.setName("John Doe");
        client1.setRut("12.345.678-9");
        client1.setEmail("john.doe@example.com");
        client1.setBirthDate(LocalDate.of(1990, 5, 15));
        client1.setNumberOfVisits(5);

        ClientEntity client2 = new ClientEntity();

        client2.setId(2L);
        client2.setName("Jane Smith");
        client2.setRut("19.765.432-1");
        client2.setEmail("jane.smith@example.com");
        client2.setBirthDate(LocalDate.of(1985, 8, 30));
        client2.setNumberOfVisits(3);

        booking.setNameBooking("");
        booking.setLapsNumber(10);
        booking.setMaximumTime(20);
        booking.setBookingDate(LocalDate.of(2025, 4, 28));
        booking.setBookingTime(LocalTime.of(15, 0));
        booking.setTotalDuration(30);
        booking.setBasePrice(20000.0);
        booking.setDiscountByPeopleNumber(2000.0);
        booking.setDiscountByFrequentCustomer(2000.0);
        booking.setDiscountBySpecialDays(0.0);
        booking.setClients(List.of(client1, client2));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Booking name cannot be empty", exception.getMessage());
    }

    @Test
    void whenCreateBookingOnWeekendWithInvalidTime_thenBookingFails() {
        BookingEntity booking = new BookingEntity();

        ClientEntity client1 = new ClientEntity();
        client1.setId(1L);
        client1.setName("John Doe");
        client1.setRut("12.345.678-9");
        client1.setEmail("john.doe@example.com");
        client1.setBirthDate(LocalDate.of(1990, 5, 15));
        client1.setNumberOfVisits(5);

        ClientEntity client2 = new ClientEntity();

        client2.setId(2L);
        client2.setName("Jane Smith");
        client2.setRut("19.765.432-1");
        client2.setEmail("jane.smith@example.com");
        client2.setBirthDate(LocalDate.of(1985, 8, 30));
        client2.setNumberOfVisits(3);

        booking.setNameBooking("John Doe");
        booking.setLapsNumber(10);
        booking.setMaximumTime(20);
        booking.setBookingDate(LocalDate.of(2025, 4, 26));
        booking.setBookingTime(LocalTime.of(9, 0));
        booking.setTotalDuration(90);
        booking.setBasePrice(20000.0);
        booking.setDiscountByPeopleNumber(2000.0);
        booking.setDiscountByFrequentCustomer(2000.0);
        booking.setDiscountBySpecialDays(0.0);
        booking.setClients(List.of(client1, client2));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("On weekends and holidays, booking time must be between 10:00 and 22:00", exception.getMessage());
    }

    @Test
    void whenCreateBookingWithNegativeBasePrice_thenBookingFails() {
        BookingEntity booking = new BookingEntity();

        ClientEntity client1 = new ClientEntity();
        client1.setId(1L);
        client1.setName("John Doe");
        client1.setRut("12.345.678-9");
        client1.setEmail("john.doe@example.com");
        client1.setBirthDate(LocalDate.of(1990, 5, 15));
        client1.setNumberOfVisits(5);

        ClientEntity client2 = new ClientEntity();
        client2.setId(2L);
        client2.setName("Jane Smith");
        client2.setRut("19.765.432-1");
        client2.setEmail("jane.smith@example.com");
        client2.setBirthDate(LocalDate.of(1985, 8, 30));
        client2.setNumberOfVisits(3);

        booking.setNameBooking("Jane Smith");
        booking.setLapsNumber(10);
        booking.setMaximumTime(20);
        booking.setBookingDate(LocalDate.of(2025, 4, 28));
        booking.setBookingTime(LocalTime.of(15, 0));
        booking.setTotalDuration(90);
        booking.setBasePrice(-1000.0);  // Negative base price
        booking.setDiscountByPeopleNumber(2000.0);
        booking.setDiscountByFrequentCustomer(2000.0);
        booking.setDiscountBySpecialDays(0.0);
        booking.setClients(List.of(client1, client2));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Base price must be greater than or equal 0", exception.getMessage());
    }

    @Test
    void whenCreateBookingWithClients_thenClientVisitsUpdated() {
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setName("John Doe");
        client.setRut("12.345.678-9");
        client.setEmail("john.doe@example.com");
        client.setBirthDate(LocalDate.of(1990, 5, 15));
        client.setNumberOfVisits(5);

        BookingEntity booking = new BookingEntity();
        booking.setId(1L);
        booking.setNameBooking("Client Visit Update Booking");
        booking.setLapsNumber(10);
        booking.setMaximumTime(20);
        booking.setBookingDate(LocalDate.of(2025, 4, 28));
        booking.setBookingTime(LocalTime.of(15, 0));
        booking.setTotalDuration(90);
        booking.setBasePrice(20000.0);
        booking.setDiscountByPeopleNumber(2000.0);
        booking.setDiscountByFrequentCustomer(2000.0);
        booking.setDiscountBySpecialDays(0.0);
        booking.setClients(List.of(client));

        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(booking);

        BookingEntity savedBooking = bookingService.createBooking(booking);

        assertEquals(6, client.getNumberOfVisits());  // Visits should be updated
    }
}
