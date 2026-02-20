package com.airtribe.library.controller;

import com.airtribe.library.domain.Reservation;
import com.airtribe.library.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> reserveBook(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.reserveBook(request.isbn, request.patronId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<List<Reservation>> getReservationsByBook(@PathVariable String isbn) {
        return ResponseEntity.ok(reservationService.getReservationsByBook(isbn));
    }

    static class ReservationRequest {
        public String isbn;
        public String patronId;
    }
}
