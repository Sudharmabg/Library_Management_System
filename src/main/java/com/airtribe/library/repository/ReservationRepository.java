package com.airtribe.library.repository;

import com.airtribe.library.domain.Reservation;
import com.airtribe.library.domain.ReservationStatus;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ReservationRepository {
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    public Reservation save(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
        return reservation;
    }

    public Optional<Reservation> findById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    public List<Reservation> findActiveByIsbn(String isbn) {
        return reservations.values().stream()
                .filter(r -> r.getIsbn().equals(isbn) && r.getStatus() == ReservationStatus.ACTIVE)
                .sorted(Comparator.comparing(Reservation::getReservationDate))
                .collect(Collectors.toList());
    }
}
