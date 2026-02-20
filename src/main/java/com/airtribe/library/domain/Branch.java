package com.airtribe.library.domain;

import java.util.*;

public class Branch {
    private String branchId;
    private String name;
    private String location;
    private Set<String> bookIsbns;

    public Branch(String branchId, String name, String location) {
        this.branchId = branchId;
        this.name = name;
        this.location = location;
        this.bookIsbns = new HashSet<>();
    }

    public String getBranchId() { return branchId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public Set<String> getBookIsbns() { return new HashSet<>(bookIsbns); }

    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }

    public void addBook(String isbn) {
        bookIsbns.add(isbn);
    }

    public void removeBook(String isbn) {
        bookIsbns.remove(isbn);
    }
}
