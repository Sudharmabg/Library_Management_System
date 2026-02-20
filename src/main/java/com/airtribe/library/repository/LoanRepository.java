package com.airtribe.library.repository;

import com.airtribe.library.domain.Loan;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class LoanRepository {
    private final Map<String, Loan> loans = new ConcurrentHashMap<>();

    public Loan save(Loan loan) {
        loans.put(loan.getLoanId(), loan);
        return loan;
    }

    public Optional<Loan> findById(String loanId) {
        return Optional.ofNullable(loans.get(loanId));
    }

    public List<Loan> findAll() {
        return new ArrayList<>(loans.values());
    }

    public List<Loan> findByPatronId(String patronId) {
        return loans.values().stream()
                .filter(loan -> loan.getPatronId().equals(patronId))
                .collect(Collectors.toList());
    }

    public Optional<Loan> findActiveByIsbn(String isbn) {
        return loans.values().stream()
                .filter(loan -> loan.getIsbn().equals(isbn) && loan.isActive())
                .findFirst();
    }
}
