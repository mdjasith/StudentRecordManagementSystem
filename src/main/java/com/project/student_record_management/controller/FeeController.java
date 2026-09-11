package com.project.student_record_management.controller;

import com.project.student_record_management.dto.FeeDTO;
import com.project.student_record_management.entity.Fee;
import com.project.student_record_management.service.FeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fees")
@CrossOrigin(origins = "http://localhost:3000")
public class FeeController {

    @Autowired
    private FeeService feeService;

    @PostMapping
    public ResponseEntity<Fee> createFee(@Valid @RequestBody FeeDTO feeDTO) {
        return new ResponseEntity<>(feeService.createFee(feeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/pay/{feeId}")
    public ResponseEntity<Fee> payFee(@PathVariable Long feeId, @RequestParam Double amount) {
        return ResponseEntity.ok(feeService.payFee(feeId, amount));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Fee> getFeeByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(feeService.getFeeByStudentId(studentId));
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Fee>> getUnpaidFees() {
        return ResponseEntity.ok(feeService.getUnpaidFees());
    }

    @GetMapping("/partial")
    public ResponseEntity<List<Fee>> getPartialFees() {
        return ResponseEntity.ok(feeService.getPartialFees());
    }

    @GetMapping("/check/{studentId}")
    public ResponseEntity<Boolean> checkFeeStatus(@PathVariable Long studentId) {
        return ResponseEntity.ok(feeService.isFeePaid(studentId));
    }

    @GetMapping
    public ResponseEntity<List<Fee>> getAllFees() {
        return ResponseEntity.ok(feeService.getAllFees());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFee(@PathVariable Long id) {
        feeService.deleteFee(id);
        return ResponseEntity.noContent().build();
    }
}