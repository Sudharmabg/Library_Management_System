package com.airtribe.library.controller;

import com.airtribe.library.domain.Patron;
import com.airtribe.library.service.PatronService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @PostMapping
    public ResponseEntity<Patron> addPatron(@RequestBody PatronRequest request) {
        Patron patron = patronService.addPatron(request.name, request.email);
        return ResponseEntity.status(HttpStatus.CREATED).body(patron);
    }

    @PutMapping("/{patronId}")
    public ResponseEntity<Patron> updatePatron(@PathVariable String patronId, @RequestBody PatronRequest request) {
        Patron patron = patronService.updatePatron(patronId, request.name, request.email);
        return ResponseEntity.ok(patron);
    }

    @GetMapping("/{patronId}")
    public ResponseEntity<Patron> getPatron(@PathVariable String patronId) {
        return ResponseEntity.ok(patronService.getPatron(patronId));
    }

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        return ResponseEntity.ok(patronService.getAllPatrons());
    }

    @GetMapping("/{patronId}/history")
    public ResponseEntity<List<String>> getBorrowingHistory(@PathVariable String patronId) {
        return ResponseEntity.ok(patronService.getBorrowingHistory(patronId));
    }

    static class PatronRequest {
        public String name;
        public String email;
    }
}
