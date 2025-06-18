package com.example.PEP3_Tingeso_Backend.services;

import com.example.PEP3_Tingeso_Backend.entities.BookingEntity;
import com.example.PEP3_Tingeso_Backend.entities.RackEntity;
import com.example.PEP3_Tingeso_Backend.repositories.BookingRepository;
import com.example.PEP3_Tingeso_Backend.repositories.RackRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@AllArgsConstructor
@Service
public class RackService {

    @Autowired
    private RackRepository rackRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public RackEntity createRack(RackEntity rack) {
        if(rack.getDate() == null){
            throw new IllegalArgumentException("The date in the rack can't be null");
        }

        if(rack.getStartTime() == null){
            throw new IllegalArgumentException("The start time in the rack can't be null");
        }

        if(rack.getEndTime() == null){
            throw new IllegalArgumentException("The start time in the rack can't be null");
        }

        if(rack.getBookingId() <= 0 || rack.getBookingId() == null){
            throw new IllegalArgumentException("The bookingId in the rack can't be null or less than 1");
        }

        List<LocalDate> holidays = Arrays.asList(
                LocalDate.of(rack.getDate().getYear(), 1, 1),
                LocalDate.of(rack.getDate().getYear(), 4, 18),
                LocalDate.of(rack.getDate().getYear(), 4, 19),
                LocalDate.of(rack.getDate().getYear(), 5, 1),
                LocalDate.of(rack.getDate().getYear(), 5, 21),
                LocalDate.of(rack.getDate().getYear(), 6, 20),
                LocalDate.of(rack.getDate().getYear(), 6, 29),
                LocalDate.of(rack.getDate().getYear(), 7, 16),
                LocalDate.of(rack.getDate().getYear(), 8, 15),
                LocalDate.of(rack.getDate().getYear(), 9, 18),
                LocalDate.of(rack.getDate().getYear(), 9, 19),
                LocalDate.of(rack.getDate().getYear(), 10, 12),
                LocalDate.of(rack.getDate().getYear(), 10, 31),
                LocalDate.of(rack.getDate().getYear(), 11, 1),
                LocalDate.of(rack.getDate().getYear(), 11, 16),
                LocalDate.of(rack.getDate().getYear(), 12, 8),
                LocalDate.of(rack.getDate().getYear(), 12, 14),
                LocalDate.of(rack.getDate().getYear(), 12, 25)
        );

        if(rack.getDate().getDayOfWeek() == DayOfWeek.SATURDAY || rack.getDate().getDayOfWeek() == DayOfWeek.SUNDAY
                || holidays.contains(rack.getDate())) {
            if(rack.getStartTime().isBefore(LocalTime.of(10, 0)) ||
                    rack.getEndTime().isAfter(LocalTime.of(22, 0))){
                throw new IllegalArgumentException("Start time must be between 10:00 and 22:00 for weekends and holidays.");
            }
        }
        else{
            if(rack.getStartTime().isBefore(LocalTime.of(14, 0)) ||
                    rack.getEndTime().isAfter(LocalTime.of(22, 0))){
                throw new IllegalArgumentException("Start time must be beetween 14:00 and 22:00 for Mondays, Tuesdays, Wednesdays," +
                        "Thursdays, Fridays.");
            }
        }

        return rackRepository.save(rack);
    }

    public RackEntity getRackById(Long id){
        return rackRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Rack not found")
        );
    }

    public List<RackEntity> getAllRacks(){
        return rackRepository.findAll();
    }

    public RackEntity updateRack(RackEntity rack){
        if(rack.getDate() == null){
            throw new IllegalArgumentException("The date in the rack can't be null");
        }

        if(rack.getStartTime() == null){
            throw new IllegalArgumentException("The start time in the rack can't be null");
        }

        if(rack.getEndTime() == null){
            throw new IllegalArgumentException("The start time in the rack can't be null");
        }

        if(rack.getBookingId() <= 0 || rack.getBookingId() == null){
            throw new IllegalArgumentException("The bookingId in the rack can't be null or less than 1");
        }

        List<LocalDate> holidays = Arrays.asList(
                LocalDate.of(rack.getDate().getYear(), 1, 1),
                LocalDate.of(rack.getDate().getYear(), 4, 18),
                LocalDate.of(rack.getDate().getYear(), 4, 19),
                LocalDate.of(rack.getDate().getYear(), 5, 1),
                LocalDate.of(rack.getDate().getYear(), 5, 21),
                LocalDate.of(rack.getDate().getYear(), 6, 20),
                LocalDate.of(rack.getDate().getYear(), 6, 29),
                LocalDate.of(rack.getDate().getYear(), 7, 16),
                LocalDate.of(rack.getDate().getYear(), 8, 15),
                LocalDate.of(rack.getDate().getYear(), 9, 18),
                LocalDate.of(rack.getDate().getYear(), 9, 19),
                LocalDate.of(rack.getDate().getYear(), 10, 12),
                LocalDate.of(rack.getDate().getYear(), 10, 31),
                LocalDate.of(rack.getDate().getYear(), 11, 1),
                LocalDate.of(rack.getDate().getYear(), 11, 16),
                LocalDate.of(rack.getDate().getYear(), 12, 8),
                LocalDate.of(rack.getDate().getYear(), 12, 14),
                LocalDate.of(rack.getDate().getYear(), 12, 25)
        );

        if(rack.getDate().getDayOfWeek() == DayOfWeek.SATURDAY || rack.getDate().getDayOfWeek() == DayOfWeek.SUNDAY
                || holidays.contains(rack.getDate())) {
            if(rack.getStartTime().isBefore(LocalTime.of(10, 0)) ||
                    rack.getEndTime().isAfter(LocalTime.of(22, 0))){
                throw new IllegalArgumentException("Start time must be between 10:00 and 22:00 for weekends and holidays.");
            }
        }
        else{
            if(rack.getStartTime().isBefore(LocalTime.of(14, 0)) ||
                    rack.getEndTime().isAfter(LocalTime.of(22, 0))){
                throw new IllegalArgumentException("Start time must be beetween 14:00 and 22:00 for Mondays, Tuesdays, Wednesdays," +
                        "Thursdays, Fridays.");
            }
        }

        rack.setId(rack.getId());
        rack.setDate(rack.getDate());
        rack.setStartTime(rack.getStartTime());
        rack.setEndTime(rack.getEndTime());
        rack.setBookingId(rack.getBookingId());

        return rackRepository.save(rack);
    }

    public void deleteRack(Long id) throws Exception{
        if(id <= 0 || id == null){
            throw new IllegalArgumentException("Rack not found");
        }

        try{
            rackRepository.deleteById(id);
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public static LocalTime getStartTimeForDate(LocalDate date, List<LocalDate> holidays) {
        DayOfWeek day = date.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY || holidays.contains(date)) {
            return LocalTime.of(10, 0);
        } else {
            return LocalTime.of(14, 0);
        }
    }

    public Map<String, Map<LocalTime, BookingEntity>> getWeeklyBookingRackFromDate(LocalDate startDate) {
        Map<String, Map<LocalTime, BookingEntity>> rack = new LinkedHashMap<>();

        int slotMinutes = 20;
        LocalDate monday = startDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        // Lista de feriados
        List<LocalDate> holidays = Arrays.asList(
                LocalDate.of(startDate.getYear(), 1, 1),
                LocalDate.of(startDate.getYear(), 4, 18),
                LocalDate.of(startDate.getYear(), 4, 19),
                LocalDate.of(startDate.getYear(), 5, 1),
                LocalDate.of(startDate.getYear(), 5, 21),
                LocalDate.of(startDate.getYear(), 6, 20),
                LocalDate.of(startDate.getYear(), 6, 29),
                LocalDate.of(startDate.getYear(), 7, 16),
                LocalDate.of(startDate.getYear(), 8, 15),
                LocalDate.of(startDate.getYear(), 9, 18),
                LocalDate.of(startDate.getYear(), 9, 19),
                LocalDate.of(startDate.getYear(), 10, 12),
                LocalDate.of(startDate.getYear(), 10, 31),
                LocalDate.of(startDate.getYear(), 11, 1),
                LocalDate.of(startDate.getYear(), 11, 16),
                LocalDate.of(startDate.getYear(), 12, 8),
                LocalDate.of(startDate.getYear(), 12, 14),
                LocalDate.of(startDate.getYear(), 12, 25)
        );

        // Buscar reservas entre lunes y domingo
        List<BookingEntity> weekBookings = bookingRepository
                .findByBookingDateBetween(monday, sunday)
                .orElse(Collections.emptyList());

        // Indexar por fecha y hora
        Map<LocalDate, Map<LocalTime, BookingEntity>> bookingMap = new HashMap<>();
        for (BookingEntity booking : weekBookings) {
            bookingMap
                    .computeIfAbsent(booking.getBookingDate(), k -> new HashMap<>())
                    .put(booking.getBookingTime(), booking);
        }

        // Construir mapa día por día
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = monday.plusDays(i);
            String dayName = currentDate.getDayOfWeek().toString();
            Map<LocalTime, BookingEntity> slots = new LinkedHashMap<>();

            LocalTime startTime = getStartTimeForDate(currentDate, holidays);
            LocalTime endTime = LocalTime.of(22, 0);

            for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(slotMinutes)) {
                BookingEntity bookingSlot = bookingMap
                        .getOrDefault(currentDate, Collections.emptyMap())
                        .getOrDefault(time, null);

                slots.put(time, bookingSlot); // null = libre
            }

            rack.put(dayName, slots);
        }

        return rack;
    }

}
