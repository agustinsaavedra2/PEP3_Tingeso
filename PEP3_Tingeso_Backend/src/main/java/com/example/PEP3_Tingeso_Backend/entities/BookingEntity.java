package com.example.PEP3_Tingeso_Backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.*;
import java.util.List;

@Entity
@Table(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameBooking;
    private Integer lapsNumber;
    private Integer maximumTime;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private int totalDuration;
    private Double basePrice;
    private Double discountByPeopleNumber;
    private Double discountByFrequentCustomer;
    private Double discountBySpecialDays;

    @ManyToMany
    @JoinTable(name="booking_client", joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<ClientEntity> clients;
}
