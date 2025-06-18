package com.example.PEP3_Tingeso_Backend.controllers;

import org.springframework.data.util.Pair;
import com.example.PEP3_Tingeso_Backend.entities.BookingEntity;
import com.example.PEP3_Tingeso_Backend.entities.ClientEntity;
import com.example.PEP3_Tingeso_Backend.repositories.ClientRepository;
import com.example.PEP3_Tingeso_Backend.services.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin("*")
@AllArgsConstructor
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/")
    public ResponseEntity<BookingEntity> createBooking(@RequestBody BookingEntity booking){
        List<ClientEntity> getClients = clientRepository.findAllById(booking.getClients().stream().
                map(ClientEntity::getId).collect(Collectors.toList()));

        booking.setClients(getClients);

        BookingEntity newBooking = bookingService.createBooking(booking);

        return ResponseEntity.ok(newBooking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingEntity> getBookingById(@PathVariable("id") Long id){
        BookingEntity booking = bookingService.getBookingById(id);

        return ResponseEntity.ok(booking);
    }

    @GetMapping("/")
    public ResponseEntity<List<BookingEntity>> getAllBookings(){
        List<BookingEntity> bookings = bookingService.getAllBookings();

        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/")
    public ResponseEntity<BookingEntity> updateBooking(@RequestBody BookingEntity booking){
        BookingEntity updatedBooking = bookingService.updateBooking(booking);

        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookingEntity> deleteBooking(@PathVariable("id") Long id) throws Exception{
        bookingService.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/setPriceAndDuration/{id}")
    public ResponseEntity<List<Pair<String, Double>>> setPriceAndDuration(@PathVariable("id") Long id){
        try{
            List<Pair<String, Double>> clientsBasePrice = bookingService.setPriceAndDurationInBooking(id);
            return ResponseEntity.ok(clientsBasePrice);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/setDiscountPeopleNumber/{id}")
    public ResponseEntity<List<Pair<String, Double>>> setDiscountPeopleNumber(@PathVariable("id") Long id){
        try{
            List<Pair<String, Double>> clientsDiscountPeopleNumber = bookingService.setDiscountByPeopleNumber(id);
            return ResponseEntity.ok(clientsDiscountPeopleNumber);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/discountByFrequentCustomer/{id}")
    public ResponseEntity<List<Pair<String,Double>>> setDiscountFrequentCustomer(@PathVariable("id") Long id){
        try{
            List<Pair<String, Double>> clientsDiscountFrequency= bookingService.discountByFrequentCustomer(id);
            return ResponseEntity.ok(clientsDiscountFrequency);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/discountBySpecialDays/{id}")
    public ResponseEntity<List<Pair<String, Double>>> setDiscountSpecialDays(@PathVariable("id") Long id){
        try{
            List<Pair<String, Double>> clientsDiscountSpecialDays = bookingService.discountBySpecialDays(id);
            return ResponseEntity.ok(clientsDiscountSpecialDays);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/report/revenueByType")
    public List<Map<String, Object>> getRevenueReportByType(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return bookingService.getRevenueReportByBookingType(startDate, endDate);
    }

    @GetMapping("/report/revenueByGroupSize")
    public List<Map<String, Object>> getRevenueReportByGroupSize(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return bookingService.getRevenueReportByGroupSize(startDate, endDate);
    }
}
