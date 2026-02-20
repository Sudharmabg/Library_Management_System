package com.airtribe.library.domain;

import java.time.LocalDateTime;

public class Reservation {
    private String reservationId;
    private String isbn;
    private String patronId;
    private LocalDateTime reservationDate;
    private ReservationStatus status;

    public Reservation(String reservationId, String isbn, String patronId) {
        this.reservationId = reservationId;
        this.isbn = isbn;
        this.patronId = patronId;
        this.reservationDate = LocalDateTime.now();
        this.status = ReservationStatus.ACTIVE;
    }

    public String getReservationId() { return reservationId; }
    public String getIsbn() { return isbn; }
    public String getPatronId() { return patronId; }
    public LocalDateTime getReservationDate() { return reservationDate; }
    public ReservationStatus getStatus() { return status; }

    public void setStatus(ReservationStatus status) { this.status = status; }
}
