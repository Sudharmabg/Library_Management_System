package com.airtribe.library.service;

import com.airtribe.library.domain.*;
import com.airtribe.library.exception.*;
import com.airtribe.library.patterns.*;
import com.airtribe.library.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final EntityFactory entityFactory;
    private final NotificationService notificationService;
    private final Map<String, PatronObserver> patronObservers = new HashMap<>();

    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository,
                              PatronRepository patronRepository, EntityFactory entityFactory,
                              NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.entityFactory = entityFactory;
        this.notificationService = notificationService;
    }

    public Reservation reserveBook(String isbn, String patronId) {
        logger.info("Reservation request: ISBN={}, PatronID={}", isbn, patronId);
        
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + isbn));
        
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException("Patron not found: " + patronId));

        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new IllegalArgumentException("Book is available, no need to reserve");
        }

        Reservation reservation = entityFactory.createReservation(isbn, patronId);
        
        PatronObserver observer = patronObservers.computeIfAbsent(patronId,
                id -> new PatronObserver(patronId, patron.getEmail()));
        notificationService.attach(observer);

        logger.info("Book reserved successfully: ReservationID={}", reservation.getReservationId());
        return reservationRepository.save(reservation);
    }

    public void notifyNextReservation(String isbn) {
        List<Reservation> activeReservations = reservationRepository.findActiveByIsbn(isbn);
        if (!activeReservations.isEmpty()) {
            Reservation nextReservation = activeReservations.get(0);
            String message = String.format("Book %s is now available for your reservation", isbn);
            
            PatronObserver observer = patronObservers.get(nextReservation.getPatronId());
            if (observer != null) {
                notificationService.notifyObservers(message);
            }
            
            nextReservation.setStatus(ReservationStatus.FULFILLED);
            reservationRepository.save(nextReservation);
        }
    }

    public List<Reservation> getReservationsByBook(String isbn) {
        return reservationRepository.findActiveByIsbn(isbn);
    }
}
