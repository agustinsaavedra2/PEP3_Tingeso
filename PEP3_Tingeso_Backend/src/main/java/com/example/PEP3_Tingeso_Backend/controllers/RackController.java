package com.example.PEP3_Tingeso_Backend.controllers;

import com.example.PEP3_Tingeso_Backend.entities.BookingEntity;
import com.example.PEP3_Tingeso_Backend.entities.RackEntity;
import com.example.PEP3_Tingeso_Backend.services.BookingService;
import com.example.PEP3_Tingeso_Backend.services.RackService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/api/rack")
@CrossOrigin("*")
public class RackController {

    @Autowired
    private RackService rackService;

    @Autowired
    private BookingService bookingService;

    @PostMapping("/")
    public ResponseEntity<RackEntity> createRack(@RequestBody RackEntity rack){
        RackEntity newRack = rackService.createRack(rack);

        return ResponseEntity.ok(newRack);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RackEntity> getRackById(@PathVariable("id") Long id){
        RackEntity rack = rackService.getRackById(id);

        return ResponseEntity.ok(rack);
    }

    @GetMapping("/")
    public ResponseEntity<List<RackEntity>> getAllRacks(){
        List<RackEntity> racks = rackService.getAllRacks();

        return ResponseEntity.ok(racks);
    }

    @PutMapping("/")
    public ResponseEntity<RackEntity> updateRack(@RequestBody RackEntity rack){
        RackEntity updatedRack = rackService.updateRack(rack);

        return ResponseEntity.ok(updatedRack);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRack(@PathVariable("id") Long id) throws Exception{
        rackService.deleteRack(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyRacks(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                LocalDate startDate){
        Map<String, Map<LocalTime, BookingEntity>> rack = rackService.getWeeklyBookingRackFromDate(startDate);

        return ResponseEntity.ok(rack);
    }

}
