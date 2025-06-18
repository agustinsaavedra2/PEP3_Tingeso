package com.example.PEP3_Tingeso_Backend.repositories;

import com.example.PEP3_Tingeso_Backend.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    public Optional<BookingEntity> findById(Long id);
    public Optional<List<BookingEntity>> findByBookingDateBetween(LocalDate startTime, LocalDate endTime);
}
