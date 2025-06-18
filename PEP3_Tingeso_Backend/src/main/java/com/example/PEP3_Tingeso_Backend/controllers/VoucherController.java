package com.example.PEP3_Tingeso_Backend.controllers;

import com.example.PEP3_Tingeso_Backend.entities.VoucherEntity;
import com.example.PEP3_Tingeso_Backend.services.VoucherService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/voucher")
@CrossOrigin("*")
@AllArgsConstructor
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @PostMapping("/")
    public ResponseEntity<VoucherEntity> createVoucher(@RequestBody VoucherEntity voucher){
        VoucherEntity newVoucher = voucherService.createVoucher(voucher);

        return ResponseEntity.ok(newVoucher);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherEntity> getVoucherById(@PathVariable("id") Long id){
        VoucherEntity voucher = voucherService.getVoucherById(id);

        return ResponseEntity.ok(voucher);
    }

    @GetMapping("/")
    public ResponseEntity<List<VoucherEntity>> getAllVouchers(){
        List<VoucherEntity> vouchers = voucherService.getAllVouchers();

        return ResponseEntity.ok(vouchers);
    }

    @PutMapping("/")
    public ResponseEntity<VoucherEntity> updateVoucher(@RequestBody VoucherEntity voucher){
        VoucherEntity updatedVoucher = voucherService.updateVoucher(voucher);

        return ResponseEntity.ok(updatedVoucher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable("id") Long id) throws Exception{
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/generateClientVouchers/{bookingId}")
    public ResponseEntity<List<VoucherEntity>> generateClientVouchers(@PathVariable("bookingId") Long bookingId){
        List<VoucherEntity> clientVouchers = voucherService.generateVouchers(bookingId);
        return ResponseEntity.ok(clientVouchers);
    }
 }
