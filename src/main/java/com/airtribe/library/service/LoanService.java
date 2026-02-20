package com.airtribe.library.service;

import com.airtribe.library.domain.*;
import com.airtribe.library.exception.*;
import com.airtribe.library.patterns.EntityFactory;
import com.airtribe.library.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {
    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final EntityFactory entityFactory;
    private final ReservationService reservationService;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository,
                       PatronRepository patronRepository, EntityFactory entityFactory,
                       ReservationService reservationService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.entityFactory = entityFactory;
        this.reservationService = reservationService;
    }

    public Loan checkoutBook(String isbn, String patronId) {
        logger.info("Checkout request: ISBN={}, PatronID={}", isbn, patronId);
        
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + isbn));
        
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException("Patron not found: " + patronId));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException("Book is not available: " + isbn);
        }

        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        patron.borrowBook(isbn);
        patron.addToBorrowingHistory(isbn);
        patronRepository.save(patron);

        Loan loan = entityFactory.createLoan(isbn, patronId);
        logger.info("Book checked out successfully: LoanID={}", loan.getLoanId());
        return loanRepository.save(loan);
    }

    public void returnBook(String isbn) {
        logger.info("Return request: ISBN={}", isbn);
        
        Loan loan = loanRepository.findActiveByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("No active loan found for ISBN: " + isbn));

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + isbn));

        Patron patron = patronRepository.findById(loan.getPatronId())
                .orElseThrow(() -> new PatronNotFoundException("Patron not found: " + loan.getPatronId()));

        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        patron.returnBook(isbn);
        patronRepository.save(patron);

        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);

        reservationService.notifyNextReservation(isbn);
        logger.info("Book returned successfully: ISBN={}", isbn);
    }

    public List<Loan> getPatronLoans(String patronId) {
        return loanRepository.findByPatronId(patronId);
    }
}
