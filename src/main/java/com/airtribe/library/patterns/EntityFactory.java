package com.airtribe.library.patterns;

import com.airtribe.library.domain.*;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class EntityFactory {
    
    public Book createBook(String isbn, String title, String author, int publicationYear) {
        return new Book(isbn, title, author, publicationYear);
    }

    public Patron createPatron(String name, String email) {
        String patronId = "P" + UUID.randomUUID().toString().substring(0, 8);
        return new Patron(patronId, name, email);
    }

    public Loan createLoan(String isbn, String patronId) {
        String loanId = "L" + UUID.randomUUID().toString().substring(0, 8);
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(14);
        return new Loan(loanId, isbn, patronId, borrowDate, dueDate);
    }

    public Branch createBranch(String name, String location) {
        String branchId = "B" + UUID.randomUUID().toString().substring(0, 8);
        return new Branch(branchId, name, location);
    }

    public Reservation createReservation(String isbn, String patronId) {
        String reservationId = "R" + UUID.randomUUID().toString().substring(0, 8);
        return new Reservation(reservationId, isbn, patronId);
    }
}
