package com.example.PEP3_Tingeso_Backend.services;

import com.example.PEP3_Tingeso_Backend.entities.VoucherEntity;
import com.example.PEP3_Tingeso_Backend.repositories.VoucherRepository;
import org.springframework.data.util.Pair;
import com.example.PEP3_Tingeso_Backend.entities.BookingEntity;
import com.example.PEP3_Tingeso_Backend.entities.ClientEntity;
import com.example.PEP3_Tingeso_Backend.repositories.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    public BookingEntity createBooking(BookingEntity booking) {
        if(booking.getNameBooking() == null || booking.getNameBooking().isBlank()){
            throw new IllegalArgumentException("Booking name cannot be empty");
        }

        if(booking.getLapsNumber() <= 0) {
            throw new IllegalArgumentException("LapsNumber must be greater than 0");
        }

        if(booking.getMaximumTime() <= 0) {
            throw new IllegalArgumentException("Maximum time must be greater than 0");
        }

        if(booking.getBookingDate() == null) {
            throw new IllegalArgumentException("Booking date cannot be null");
        }

        if(booking.getBookingTime() == null) {
            throw new IllegalArgumentException("Booking time cannot be null");
        }

        LocalDate bookingDate = booking.getBookingDate();
        LocalTime bookingTime = booking.getBookingTime();

        DayOfWeek day = bookingDate.getDayOfWeek();

        List<LocalDate> holidays = Arrays.asList(
                LocalDate.of(bookingDate.getYear(), 1, 1),
                LocalDate.of(bookingDate.getYear(), 4, 18),
                LocalDate.of(bookingDate.getYear(), 4, 19),
                LocalDate.of(bookingDate.getYear(), 5, 1),
                LocalDate.of(bookingDate.getYear(), 5, 21),
                LocalDate.of(bookingDate.getYear(), 6, 20),
                LocalDate.of(bookingDate.getYear(), 6, 29),
                LocalDate.of(bookingDate.getYear(), 7, 16),
                LocalDate.of(bookingDate.getYear(), 8, 15),
                LocalDate.of(bookingDate.getYear(), 9, 18),
                LocalDate.of(bookingDate.getYear(), 9, 19),
                LocalDate.of(bookingDate.getYear(), 10, 12),
                LocalDate.of(bookingDate.getYear(), 10, 31),
                LocalDate.of(bookingDate.getYear(), 11, 1),
                LocalDate.of(bookingDate.getYear(), 11, 16),
                LocalDate.of(bookingDate.getYear(), 12, 8),
                LocalDate.of(bookingDate.getYear(), 12, 14),
                LocalDate.of(bookingDate.getYear(), 12, 25)
        );

        boolean isWeekend = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
        boolean isHoliday = holidays.contains(bookingDate);

        if (isWeekend || isHoliday) {
            if (bookingTime.isBefore(LocalTime.of(10, 0)) || bookingTime.isAfter(LocalTime.of(22, 0))) {
                throw new IllegalArgumentException("On weekends and holidays, booking time must be between 10:00 and 22:00");
            }
        } else {
            if (bookingTime.isBefore(LocalTime.of(14, 0)) || bookingTime.isAfter(LocalTime.of(22, 0))) {
                throw new IllegalArgumentException("On weekdays, booking time must be between 14:00 and 22:00");
            }
        }

        if(booking.getTotalDuration() < 0) {
            throw new IllegalArgumentException("Total duration must be greater than 0");
        }

        if(booking.getBasePrice() < 0){
            throw new IllegalArgumentException("Base price must be greater than or equal 0");
        }

        if(booking.getDiscountByPeopleNumber() < 0){
            throw new IllegalArgumentException("Discount by people number must be greater than or equal 0");
        }

        if(booking.getDiscountByFrequentCustomer() < 0){
            throw new IllegalArgumentException("Discount by frequent customer must be greater than or equal 0");
        }

        if(booking.getDiscountBySpecialDays() < 0){
            throw new IllegalArgumentException("Discount by frequent customer must be greater than or equal 0");
        }

        if(booking.getClients() != null){
            for(ClientEntity client : booking.getClients()){
                client.setNumberOfVisits(client.getNumberOfVisits() + 1);
            }
        }

        return bookingRepository.save(booking);
    }

    public BookingEntity getBookingById(Long id){
        return bookingRepository.findById(id).get();
    }

    public List<BookingEntity> getAllBookings(){
        return bookingRepository.findAll();
    }

    public BookingEntity updateBooking(BookingEntity booking){
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) throws Exception{
        try{
            bookingRepository.deleteById(id);
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<Pair<String, Double>> setPriceAndDurationInBooking(Long id){
        BookingEntity booking = bookingRepository.findById(id).get();

        if(booking == null || booking.getClients() == null || booking.getClients().isEmpty()){
            throw new IllegalArgumentException("The booking was not found");
        }

        if(booking.getLapsNumber() == 10 ||
                    (booking.getMaximumTime() > 0 && booking.getMaximumTime() <= 10)){
            booking.setBasePrice(15000.0);
            booking.setTotalDuration(30);
        }

        else if(booking.getLapsNumber() == 15 ||
                    (booking.getMaximumTime() > 10 && booking.getMaximumTime() <= 15)){
            booking.setBasePrice(20000.0);
            booking.setTotalDuration(35);
        }

        else if(booking.getLapsNumber() == 20 ||
                    (booking.getMaximumTime() > 15 && booking.getMaximumTime() <= 20)){
            booking.setBasePrice(25000.0);
            booking.setTotalDuration(40);
        }
        else{
            throw new IllegalArgumentException("The maximum time or the number of laps are out of range");
        }

        List<Pair<String, Double>> clientsBasePrice = new ArrayList<>();

        for(ClientEntity client: booking.getClients()){
            if(client == null){
                throw new IllegalArgumentException("The client was not found");
            }
            
            clientsBasePrice.add(Pair.of(client.getName(), booking.getBasePrice()));
        }

        updateBooking(booking);

        return clientsBasePrice;
    }

    public List<Pair<String, Double>> setDiscountByPeopleNumber(Long id){
        BookingEntity booking = bookingRepository.findById(id).get();
        double discount = 0.0;

        if(booking == null || booking.getClients() == null || booking.getClients().isEmpty()){
            throw new IllegalArgumentException("The booking was not found");
        }

        if(booking.getClients().size() >= 1 && booking.getClients().size() <= 2){
            discount = 0.0;
            booking.setDiscountByPeopleNumber(discount);
        }
        else if(booking.getClients().size() >= 3 && booking.getClients().size() <= 5){
            discount = booking.getBasePrice() * 0.10;
            booking.setDiscountByPeopleNumber(discount);
        }
        else if(booking.getClients().size() >= 6 && booking.getClients().size() <= 10){
            discount = booking.getBasePrice() * 0.20;
            booking.setDiscountByPeopleNumber(discount);
        }
        else if(booking.getClients().size() >= 11 && booking.getClients().size() <= 15){
            discount = booking.getBasePrice() * 0.30;
            booking.setDiscountByPeopleNumber(discount);
        }
        else{
            throw new IllegalArgumentException("Client's size are not in range");
        }

        List<Pair<String, Double>> clientsDiscountSizePeople = new ArrayList<>();

        for(ClientEntity client: booking.getClients()){
            if(client == null){
                throw new IllegalArgumentException("The client was not found");
            }

            clientsDiscountSizePeople.add(Pair.of(client.getName(), booking.getDiscountByPeopleNumber()));
        }

        updateBooking(booking);

        return clientsDiscountSizePeople;
    }

    public List<Pair<String, Double>> discountByFrequentCustomer(Long id) {
        BookingEntity booking = bookingRepository.findById(id).get();
        double discount = 0.0;

        if(booking == null || booking.getClients() == null || booking.getClients().isEmpty()) {
            throw new IllegalArgumentException("The booking was not found");
        }

        List<Pair<String, Double>> clientsDiscountFrequency = new ArrayList<>();

        for (ClientEntity client : booking.getClients()) {
            if (client.getNumberOfVisits() >= 0 && client.getNumberOfVisits() <= 1) {
                discount = booking.getBasePrice() * 0.00;
            }

            else if (client.getNumberOfVisits() >= 2 && client.getNumberOfVisits() <= 4) {
                discount = booking.getBasePrice() * 0.10;
            }

            else if (client.getNumberOfVisits() >= 5 && client.getNumberOfVisits() <= 6) {
                discount = booking.getBasePrice() * 0.20;
            }

            else if (client.getNumberOfVisits() >= 7) {
                discount = booking.getBasePrice() * 0.30;
            }

            clientsDiscountFrequency.add(Pair.of(client.getName(), discount));
        }

        booking.setDiscountByFrequentCustomer(discount);
        updateBooking(booking);

        return clientsDiscountFrequency;
    }

    public List<Pair<String, Double>> discountBySpecialDays(Long id) {
        BookingEntity booking = bookingRepository.findById(id).get();
        LocalDate bookingDate = booking.getBookingDate();

        List<LocalDate> holidays = Arrays.asList(
                LocalDate.of(bookingDate.getYear(), 1, 1),
                LocalDate.of(bookingDate.getYear(), 4, 18),
                LocalDate.of(bookingDate.getYear(), 4, 19),
                LocalDate.of(bookingDate.getYear(), 5, 1),
                LocalDate.of(bookingDate.getYear(), 5, 21),
                LocalDate.of(bookingDate.getYear(), 6, 20),
                LocalDate.of(bookingDate.getYear(), 6, 29),
                LocalDate.of(bookingDate.getYear(), 7, 16),
                LocalDate.of(bookingDate.getYear(), 8, 15),
                LocalDate.of(bookingDate.getYear(), 9, 18),
                LocalDate.of(bookingDate.getYear(), 9, 19),
                LocalDate.of(bookingDate.getYear(), 10, 12),
                LocalDate.of(bookingDate.getYear(), 10, 31),
                LocalDate.of(bookingDate.getYear(), 11, 1),
                LocalDate.of(bookingDate.getYear(), 11, 16),
                LocalDate.of(bookingDate.getYear(), 12, 8),
                LocalDate.of(bookingDate.getYear(), 12, 14),
                LocalDate.of(bookingDate.getYear(), 12, 25)
        );

        Double newBasePrice = booking.getBasePrice();

        if (holidays.contains(bookingDate)) {
            newBasePrice += newBasePrice * 0.10;
        }
        if (bookingDate.getDayOfWeek() == DayOfWeek.SATURDAY || bookingDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            newBasePrice += newBasePrice * 0.15;
        }

        booking.setBasePrice(newBasePrice);

        int groupSize = booking.getClients().size();
        int numberBirthdays = 0;

        for (ClientEntity client : booking.getClients()) {
            if (client != null && client.getBirthDate() != null) {
                LocalDate birthDate = client.getBirthDate();
                if (birthDate.getMonth() == bookingDate.getMonth() &&
                        birthDate.getDayOfMonth() == bookingDate.getDayOfMonth()) {
                    numberBirthdays++;
                }
            }
        }

        int numberPeopleDiscount = 0;
        if (groupSize >= 3 && groupSize <= 5) {
            numberPeopleDiscount = Math.min(1, numberBirthdays);
        } else if (groupSize >= 6 && groupSize <= 10) {
            numberPeopleDiscount = Math.min(2, numberBirthdays);
        }

        List<Pair<String, Double>> clientDiscountBySpecialDays = new ArrayList<>();
        double totalBirthdayDiscount = 0.0;
        int appliedDiscounts = 0;

        for (ClientEntity client : booking.getClients()) {
            if (client == null) {
                throw new IllegalArgumentException("The client was not found");
            }

            double clientDiscount = 0.0;

            if (client.getBirthDate() != null) {
                LocalDate birthDate = client.getBirthDate();

                if (birthDate.getMonth() == bookingDate.getMonth() &&
                        birthDate.getDayOfMonth() == bookingDate.getDayOfMonth() &&
                        appliedDiscounts < numberPeopleDiscount) {

                    clientDiscount = booking.getBasePrice() * 0.50;
                    totalBirthdayDiscount += clientDiscount;
                    appliedDiscounts++;
                }
            }

            clientDiscountBySpecialDays.add(Pair.of(client.getName(), clientDiscount));
        }

        booking.setDiscountBySpecialDays(totalBirthdayDiscount);
        updateBooking(booking);

        return clientDiscountBySpecialDays;
    }

    public List<Map<String, Object>> getRevenueReportByBookingType(LocalDate startDate, LocalDate endDate) {
        List<BookingEntity> bookings = bookingRepository.findByBookingDateBetween(startDate, endDate).get();

        Map<String, Map<String, Object>> reportMap = new HashMap<>();

        for (BookingEntity booking : bookings) {
            String type;

            int minValue = Integer.MAX_VALUE;

            if (booking.getLapsNumber() != null) {
                minValue = Math.min(minValue, booking.getLapsNumber());
            }
            if (booking.getMaximumTime() != null) {
                minValue = Math.min(minValue, booking.getMaximumTime());
            }

            if (minValue <= 10) {
                type = "10 laps or max 10 min";
            } else if (minValue <= 15) {
                type = "15 laps or max 15 min";
            } else if (minValue <= 20) {
                type = "20 laps or max 20 min";
            } else {
                continue;
            }

            List<VoucherEntity> vouchers = voucherRepository.findByBookingId(booking.getId());

            double revenue = vouchers.stream()
                    .mapToDouble(v -> v.getTotal_price() != null ? v.getTotal_price() : 0.0)
                    .sum();

            if (!reportMap.containsKey(type)) {
                Map<String, Object> info = new HashMap<>();
                info.put("type", type);
                info.put("totalBookings", 0);
                info.put("totalRevenue", 0.0);
                reportMap.put(type, info);
            }

            Map<String, Object> report = reportMap.get(type);
            report.put("totalBookings", (int) report.get("totalBookings") + 1);
            report.put("totalRevenue", (double) report.get("totalRevenue") + revenue);
        }

        return new ArrayList<>(reportMap.values());
    }


    public List<Map<String, Object>> getRevenueReportByGroupSize(LocalDate startDate, LocalDate endDate) {
        List<BookingEntity> bookings = bookingRepository.findByBookingDateBetween(startDate, endDate).get();

        Map<String, Map<String, Object>> reportMap = new HashMap<>();

        for (BookingEntity booking : bookings) {
            int groupSize = (booking.getClients() != null) ? booking.getClients().size() : 0;

            String range;
            if (groupSize >= 1 && groupSize <= 2) {
                range = "1-2 people";
            } else if (groupSize >= 3 && groupSize <= 5) {
                range = "3-5 people";
            } else if (groupSize >= 6 && groupSize <= 10) {
                range = "6-10 people";
            } else if (groupSize >= 11 && groupSize <= 15) {
                range = "11-15 people";
            }else{
                range = "Other";
            }

            List<VoucherEntity> vouchers = voucherRepository.findByBookingId(booking.getId());

            double revenue = vouchers.stream()
                    .mapToDouble(v -> v.getTotal_price() != null ? v.getTotal_price() : 0.0)
                    .sum();

            if (!reportMap.containsKey(range)) {
                Map<String, Object> data = new HashMap<>();
                data.put("groupRange", range);
                data.put("totalBookings", 0);
                data.put("totalRevenue", 0.0);
                reportMap.put(range, data);
            }

            Map<String, Object> report = reportMap.get(range);
            report.put("totalBookings", (int) report.get("totalBookings") + 1);
            report.put("totalRevenue", (double) report.get("totalRevenue") + revenue);
        }

        return new ArrayList<>(reportMap.values());
    }
}
