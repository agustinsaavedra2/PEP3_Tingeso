package com.example.PEP3_Tingeso_Backend.services;

import com.example.PEP3_Tingeso_Backend.entities.BookingEntity;
import com.example.PEP3_Tingeso_Backend.entities.RackEntity;
import com.example.PEP3_Tingeso_Backend.repositories.BookingRepository;
import com.example.PEP3_Tingeso_Backend.repositories.RackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RackServiceTest {

    @Mock
    RackRepository rackRepository;

    @Mock
    BookingRepository bookingRepository;

    @InjectMocks
    RackService rackService;

    @InjectMocks
    BookingService bookingService;

    @Test
    public void whenCreateRackWithValidData_thenCreateRack() {
        RackEntity rack = new RackEntity();

        rack.setId(1L);
        rack.setDate(LocalDate.of(2025, 5, 10));
        rack.setStartTime(LocalTime.of(14, 0));
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(1L);

        when(rackRepository.save(rack)).thenReturn(rack);

        RackEntity savedRack = rackService.createRack(rack);

        assertEquals(rack.getId(), savedRack.getId());
        assertEquals(rack.getDate(), savedRack.getDate());
        assertEquals(rack.getStartTime(), savedRack.getStartTime());
        assertEquals(rack.getEndTime(), savedRack.getEndTime());
        assertEquals(rack.getBookingId(), savedRack.getBookingId());
    }

    @Test
    public void whenCreateRackWithInvalidStartTime_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setId(2L);
        rack.setDate(LocalDate.of(2025, 4, 30));
        rack.setStartTime(LocalTime.of(9, 0));
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(2L);

        assertThrows(IllegalArgumentException.class, () -> {
            rackService.createRack(rack);
        });

        verify(rackRepository, never()).save(any(RackEntity.class));
    }

    @Test
    public void whenCreateRackWithInvalidEndTime_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setId(2L);
        rack.setDate(LocalDate.of(2025, 5, 21));
        rack.setStartTime(LocalTime.of(10, 0));
        rack.setEndTime(LocalTime.of(23, 0));
        rack.setBookingId(3L);

        assertThrows(IllegalArgumentException.class, () -> {
            rackService.createRack(rack);
        });

        verify(rackRepository, never()).save(any(RackEntity.class));
    }

    @Test
    public void whenCreateRackWithNullDate_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setId(10L);
        rack.setDate(null);
        rack.setStartTime(LocalTime.of(14, 0));
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(3L);

        assertThrows(IllegalArgumentException.class, () -> {
            rackService.createRack(rack);
        });

        verify(rackRepository, never()).save(any(RackEntity.class));
    }

    @Test
    public void whenCreateRackWithBlankStatus_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setId(10L);
        rack.setDate(LocalDate.of(2025, 2, 25));
        rack.setStartTime(LocalTime.of(10, 0));
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(3L);

        assertThrows(IllegalArgumentException.class, () -> {
            rackService.createRack(rack);
        });

        verify(rackRepository, never()).save(any(RackEntity.class));
    }

    @Test
    public void whenCreateRackWithNegativeBookingId_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setId(10L);
        rack.setDate(LocalDate.of(2025, 6, 5));
        rack.setStartTime(LocalTime.of(14, 0));
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(-20L);

        assertThrows(IllegalArgumentException.class, () -> {
            rackService.createRack(rack);
        });

        verify(rackRepository, never()).save(any(RackEntity.class));
    }

    @Test
    public void whenCreateRackWithNullStartTime_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setId(50L);
        rack.setDate(LocalDate.of(2025, 9, 1));
        rack.setStartTime(null);
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(20L);

        assertThrows(IllegalArgumentException.class, () -> {
            rackService.createRack(rack);
        });

        verify(rackRepository, never()).save(any(RackEntity.class));
    }

    @Test
    public void whenGetRackByIdExists_thenReturnRack() {
        Long rackId = 50L;
        RackEntity rack = new RackEntity();

        rack.setId(rackId);
        rack.setDate(LocalDate.of(2025, 9, 1));
        rack.setStartTime(LocalTime.of(14, 0));
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(20L);

        when(rackRepository.findById(rackId)).thenReturn(Optional.of(rack));

        RackEntity foundRack = rackService.getRackById(rackId);

        assertNotNull(foundRack);
        assertEquals(rack.getId(), foundRack.getId());
        assertEquals(rack.getDate(), foundRack.getDate());
        assertEquals(rack.getStartTime(), foundRack.getStartTime());
        assertEquals(rack.getEndTime(), foundRack.getEndTime());
        assertEquals(rack.getBookingId(), foundRack.getBookingId());
    }

    @Test
    public void whenIdDoesNotExist_thenThrowException() {
        Long rackId = 50L;
        when(rackRepository.findById(rackId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> rackService.getRackById(rackId));

        verify(rackRepository, times(1)).findById(rackId);
    }

    @Test
    public void whenRackNotFound_thenThrowsIllegalArgumentException() {
        Long rackId = 999L;

        when(rackRepository.findById(rackId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            rackService.getRackById(rackId);
        });

        assertEquals("Rack not found", thrown.getMessage());
        verify(rackRepository, times(1)).findById(rackId);
    }

    @Test
    public void whenGetRackById_thenRepositoryCalledOnce() {
        Long rackId = 20L;

        RackEntity rack = new RackEntity();
        rack.setId(rackId);

        when(rackRepository.findById(rackId)).thenReturn(Optional.of(rack));

        rackService.getRackById(rackId);

        verify(rackRepository, times(1)).findById(rackId);
        verifyNoMoreInteractions(rackRepository);
    }

    @Test
    public void whenGetRackByIdWithPartialData_thenReturnsRackWithNullFields() {
        Long rackId = 30L;
        RackEntity rack = new RackEntity();

        rack.setId(rackId);
        rack.setDate(null);
        rack.setStartTime(LocalTime.of(14, 0));
        rack.setEndTime(null);
        rack.setBookingId(77L);

        when(rackRepository.findById(rackId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> rackService.getRackById(rackId));

        verify(rackRepository, times(1)).findById(rackId);
    }

    @Test
    public void whenNoRacksInRepository_thenReturnsEmptyList() {
        when(rackRepository.findAll()).thenReturn(Collections.emptyList());

        List<RackEntity> result = rackService.getAllRacks();

        assertTrue(result.isEmpty());
        verify(rackRepository, times(1)).findAll();
    }

    @Test
    public void whenGetAllRacks_thenReturnsRackList() {
        List<RackEntity> rackList = List.of(
                new RackEntity(1L, LocalDate.of(2025, 4, 7), LocalTime.of(14, 0), LocalTime.of(22, 0), 1L),
                new RackEntity(2L, LocalDate.of(2025, 4, 14), LocalTime.of(14, 0), LocalTime.of(22, 0), 2L)
        );

        when(rackRepository.findAll()).thenReturn(rackList);

        List<RackEntity> result = rackService.getAllRacks();

        assertEquals(2, result.size());
    }

    @Test
    public void whenRacksHaveAllAttributes_thenAttributesAreCorrect() {
        RackEntity rack = new RackEntity(
                50L,
                LocalDate.of(2025, 9, 1),
                LocalTime.of(14, 0),
                LocalTime.of(22, 0),
                20L);

        when(rackRepository.findAll()).thenReturn(List.of(rack));

        List<RackEntity> result = rackService.getAllRacks();

        RackEntity retrieved = result.get(0);

        assertEquals(50L, retrieved.getId().longValue());
        assertEquals(LocalDate.of(2025, 9, 1), retrieved.getDate());
        assertEquals(LocalTime.of(14, 0), retrieved.getStartTime());
        assertEquals(LocalTime.of(22, 0), retrieved.getEndTime());
        assertEquals(20L, retrieved.getBookingId());
    }

    @Test
    public void whenRepositoryThrowsException_thenExceptionPropagates() {
        when(rackRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            rackService.getAllRacks();
        });

        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    public void whenGetAllRacks_thenRepositoryCalledOnce() {
        when(rackRepository.findAll()).thenReturn(Collections.emptyList());

        rackService.getAllRacks();

        verify(rackRepository, times(1)).findAll();
    }

    @Test
    public void whenValidRack_thenUpdateSuccessfully() {
        RackEntity rack = new RackEntity();

        rack.setId(1L);
        rack.setDate(LocalDate.of(2025, 5, 6)); // Martes
        rack.setStartTime(LocalTime.of(14, 0));
        rack.setEndTime(LocalTime.of(20, 0));
        rack.setBookingId(10L);

        when(rackRepository.save(rack)).thenReturn(rack);

        RackEntity result = rackService.updateRack(rack);

        assertEquals(LocalDate.of(2025, 5, 6), result.getDate());
        assertEquals(LocalTime.of(14, 0), result.getStartTime());
        assertEquals(LocalTime.of(20, 0), result.getEndTime());
        assertEquals(Long.valueOf(10), result.getBookingId());

        verify(rackRepository).save(rack);
    }

    @Test
    public void whenDateIsNull_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setStartTime(LocalTime.of(14, 0));
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(1L);

        assertThrows(IllegalArgumentException.class, () -> rackService.updateRack(rack));

        verify(rackRepository, never()).save(rack);
    }

    @Test
    public void whenEndTimeIsTooLateOnWeekend_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setDate(LocalDate.of(2025, 5, 10));
        rack.setStartTime(LocalTime.of(10, 0));
        rack.setEndTime(LocalTime.of(23, 0));
        rack.setBookingId(3L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rackService.updateRack(rack));

        assertTrue(ex.getMessage().contains("Start time must be between 10:00 and 22:00 for weekends and holidays."));
    }

    @Test
    public void whenStartTimeIsTooEarlyOnWeekday_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setDate(LocalDate.of(2025, 5, 5));
        rack.setStartTime(LocalTime.of(10, 0));
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(2L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rackService.updateRack(rack));
        assertTrue(ex.getMessage().contains("Start time must be beetween 14:00 and 22:00"));
    }

    @Test
    public void whenBookingIdIsZero_thenThrowException() {
        RackEntity rack = new RackEntity();

        rack.setDate(LocalDate.of(2025, 5, 10));
        rack.setStartTime(LocalTime.of(14, 0));
        rack.setEndTime(LocalTime.of(22, 0));
        rack.setBookingId(0L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rackService.updateRack(rack));
        assertEquals("The bookingId in the rack can't be null or less than 1", ex.getMessage());
    }

    @Test
    public void whenValidId_thenRackIsDeleted() throws Exception {
        Long rackId = 1L;

        doNothing().when(rackRepository).deleteById(rackId);

        rackService.deleteRack(rackId);

        verify(rackRepository, times(1)).deleteById(rackId);
    }

    @Test
    public void whenRepositoryThrowsException_thenExceptionIsThrown() {
        Long rackId = 999L;

        doThrow(new RuntimeException("Rack not found")).when(rackRepository).deleteById(rackId);

        Exception exception = assertThrows(Exception.class, () -> {
            rackService.deleteRack(rackId);
        });

        assertEquals("Rack not found", exception.getMessage());
        verify(rackRepository, times(1)).deleteById(rackId);
    }

    @Test
    public void whenRackIdIsZero_thenThrowIllegalArgumentException() {
        Long rackId = -10L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rackService.deleteRack(rackId);
        });

        assertEquals("Rack not found", exception.getMessage());

        verify(rackRepository, times(0)).deleteById(rackId);
    }


    @Test
    public void whenRackNotFound_thenThrowException() {
        Long rackId = 10L;

        doThrow(new IllegalArgumentException("Rack not found")).when(rackRepository).deleteById(rackId);

        Exception exception = assertThrows(Exception.class, () -> {
            rackService.deleteRack(rackId);
        });

        assertEquals("Rack not found", exception.getMessage());
        verify(rackRepository, times(1)).deleteById(rackId);
    }

    @Test
    public void whenRackIdIsNegative_thenThrowIllegalArgumentException() {
        Long rackId = -1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rackService.deleteRack(rackId);
        });

        assertEquals("Rack not found", exception.getMessage());

        verify(rackRepository, times(0)).deleteById(rackId);
    }

    @Test
    public void whenSaturday_thenReturnStartTimeAt10AM() {
        LocalDate saturday = LocalDate.of(2025, 4, 26);
        List<LocalDate> holidays = new ArrayList<>();

        LocalTime result = rackService.getStartTimeForDate(saturday, holidays);

        assertEquals(LocalTime.of(10, 0), result);
    }

    @Test
    public void whenHolidayOnFriday_thenReturnStartTimeAt10AM() {
        LocalDate holiday = LocalDate.of(2025, 4, 18);
        List<LocalDate> holidays = Arrays.asList(holiday);

        LocalTime result = rackService.getStartTimeForDate(holiday, holidays);

        assertEquals(LocalTime.of(10, 0), result);
    }

    @Test
    public void whenMondayAndNotHoliday_thenReturnStartTimeAt2PM() {
        LocalDate monday = LocalDate.of(2025, 4, 28);
        List<LocalDate> holidays = new ArrayList<>();

        LocalTime result = rackService.getStartTimeForDate(monday, holidays);

        assertEquals(LocalTime.of(14, 0), result);
    }

    @Test
    public void whenWednesdayAndNotHoliday_thenReturnStartTimeAt2PM() {
        LocalDate wednesday = LocalDate.of(2025, 4, 30);
        List<LocalDate> holidays = new ArrayList<>();

        LocalTime result = rackService.getStartTimeForDate(wednesday, holidays);

        assertEquals(LocalTime.of(14, 0), result);
    }

    @Test
    public void whenSunday_thenReturnStartTimeAt10AM() {
        LocalDate sunday = LocalDate.of(2025, 4, 27);
        List<LocalDate> holidays = new ArrayList<>();

        LocalTime result = rackService.getStartTimeForDate(sunday, holidays);

        assertEquals(LocalTime.of(10, 0), result);
    }

    @Test
    public void testGetWeeklyBookingRackFromDate_SparseBookingsAcrossWeek() {
        LocalDate startDate = LocalDate.of(2025, 4, 20); // Domingo
        LocalDate monday = startDate.with(DayOfWeek.MONDAY);
        LocalDate wednesday = monday.plusDays(2);
        LocalDate friday = monday.plusDays(4);
        LocalDate sunday = monday.plusDays(6);

        BookingEntity mondayBooking = new BookingEntity();
        mondayBooking.setBookingDate(monday);
        mondayBooking.setBookingTime(LocalTime.of(14, 0));

        BookingEntity wednesdayBooking = new BookingEntity();
        wednesdayBooking.setBookingDate(wednesday);
        wednesdayBooking.setBookingTime(LocalTime.of(15, 0));

        BookingEntity fridayBooking = new BookingEntity();
        fridayBooking.setBookingDate(friday);
        fridayBooking.setBookingTime(LocalTime.of(18, 0));

        when(bookingRepository.findByBookingDateBetween(monday, sunday))
                .thenReturn(Optional.of(Arrays.asList(mondayBooking, wednesdayBooking, fridayBooking)));

        Map<String, Map<LocalTime, BookingEntity>> result = rackService.getWeeklyBookingRackFromDate(startDate);

        assertNotNull(result);
        assertEquals(7, result.size());

        Map<LocalTime, BookingEntity> mondaySlots = result.get("MONDAY");
        assertNotNull(mondaySlots);
        assertEquals(mondayBooking, mondaySlots.get(LocalTime.of(14, 0)));

        // Verificar que el miércoles tiene la reserva correcta
        Map<LocalTime, BookingEntity> wednesdaySlots = result.get("WEDNESDAY");
        assertNotNull(wednesdaySlots);
        assertEquals(wednesdayBooking, wednesdaySlots.get(LocalTime.of(15, 0)));

        // Verificar que el viernes tiene la reserva correcta
        Map<LocalTime, BookingEntity> fridaySlots = result.get("FRIDAY");
        assertNotNull(fridaySlots);
        assertEquals(fridayBooking, fridaySlots.get(LocalTime.of(18, 0)));

        // Verificar que martes, jueves y domingo no tienen reservas
        Map<LocalTime, BookingEntity> tuesdaySlots = result.get("TUESDAY");
        assertTrue(tuesdaySlots.values().stream().allMatch(Objects::isNull));

        Map<LocalTime, BookingEntity> thursdaySlots = result.get("THURSDAY");
        assertTrue(thursdaySlots.values().stream().allMatch(Objects::isNull));

        Map<LocalTime, BookingEntity> sundaySlots = result.get("SUNDAY");
        assertTrue(sundaySlots.values().stream().allMatch(Objects::isNull));
    }

    @Test
    public void testGetWeeklyBookingRackWithHolidays() {
        LocalDate startDate = LocalDate.of(2025, 12, 8);
        LocalDate monday = startDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        BookingEntity booking1 = new BookingEntity();
        booking1.setBookingDate(monday);
        booking1.setBookingTime(LocalTime.of(10, 0));

        when(bookingRepository.findByBookingDateBetween(monday, sunday))
                .thenReturn(Optional.of(Arrays.asList(booking1)));


        Map<String, Map<LocalTime, BookingEntity>> result = rackService.getWeeklyBookingRackFromDate(startDate);


        Map<LocalTime, BookingEntity> mondaySlots = result.get("MONDAY");
        assertNotNull(mondaySlots);
        assertTrue(mondaySlots.containsKey(LocalTime.of(10, 0)));


        LocalTime expectedStartTime = LocalTime.of(10, 0);
        assertEquals(expectedStartTime, mondaySlots.keySet().iterator().next());
    }

    @Test
    public void testGetWeeklyBookingRackWithNoBookings() {
        LocalDate startDate = LocalDate.of(2025, 4, 20);
        LocalDate monday = startDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        when(bookingRepository.findByBookingDateBetween(monday, sunday))
                .thenReturn(Optional.of(Collections.emptyList()));

        Map<String, Map<LocalTime, BookingEntity>> result = rackService.getWeeklyBookingRackFromDate(startDate);

        for (Map<LocalTime, BookingEntity> daySlots : result.values()) {
            assertFalse(daySlots.isEmpty());
            assertTrue(daySlots.values().stream().allMatch(Objects::isNull));
        }
    }

    @Test
    public void testGetWeeklyBookingRackWithInvalidStartDate() {
        LocalDate startDate = LocalDate.of(2025, 4, 25);

        when(bookingRepository.findByBookingDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Optional.of(Collections.emptyList()));

        Map<String, Map<LocalTime, BookingEntity>> result = rackService.getWeeklyBookingRackFromDate(startDate);

        LocalDate expectedMonday = startDate.with(DayOfWeek.MONDAY);
        assertTrue(result.containsKey(expectedMonday.getDayOfWeek().toString()));
    }

    @Test
    public void testGetWeeklyBookingRackFromDate_EmptyOptionalFromRepository() {
        LocalDate startDate = LocalDate.of(2025, 4, 20);
        LocalDate monday = startDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        when(bookingRepository.findByBookingDateBetween(monday, sunday)).thenReturn(Optional.empty());

        Map<String, Map<LocalTime, BookingEntity>> result = rackService.getWeeklyBookingRackFromDate(startDate);

        assertNotNull(result);
        assertEquals(7, result.size());

        for (Map<LocalTime, BookingEntity> slots : result.values()) {
            assertTrue(slots.values().stream().allMatch(Objects::isNull));
        }
    }
}
