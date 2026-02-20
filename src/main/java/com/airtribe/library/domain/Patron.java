package com.airtribe.library.domain;

import java.util.*;

public class Patron {
    private String patronId;
    private String name;
    private String email;
    private List<String> borrowingHistory;
    private Set<String> currentBorrowedBooks;

    public Patron(String patronId, String name, String email) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
        this.borrowingHistory = new ArrayList<>();
        this.currentBorrowedBooks = new HashSet<>();
    }

    public String getPatronId() { return patronId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getBorrowingHistory() { return new ArrayList<>(borrowingHistory); }
    public Set<String> getCurrentBorrowedBooks() { return new HashSet<>(currentBorrowedBooks); }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public void addToBorrowingHistory(String isbn) {
        borrowingHistory.add(isbn);
    }

    public void borrowBook(String isbn) {
        currentBorrowedBooks.add(isbn);
    }

    public void returnBook(String isbn) {
        currentBorrowedBooks.remove(isbn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return Objects.equals(patronId, patron.patronId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }
}
