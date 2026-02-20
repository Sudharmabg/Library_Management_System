package com.airtribe.library.domain;

import java.time.LocalDate;

public class Loan {
    private String loanId;
    private String isbn;
    private String patronId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(String loanId, String isbn, String patronId, LocalDate borrowDate, LocalDate dueDate) {
        this.loanId = loanId;
        this.isbn = isbn;
        this.patronId = patronId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public String getLoanId() { return loanId; }
    public String getIsbn() { return isbn; }
    public String getPatronId() { return patronId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public boolean isActive() {
        return returnDate == null;
    }
}
