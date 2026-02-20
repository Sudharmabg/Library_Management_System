package com.airtribe.library.controller;

import com.airtribe.library.domain.Loan;
import com.airtribe.library.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Loan> checkoutBook(@RequestBody CheckoutRequest request) {
        Loan loan = loanService.checkoutBook(request.isbn, request.patronId);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @PostMapping("/return/{isbn}")
    public ResponseEntity<Void> returnBook(@PathVariable String isbn) {
        loanService.returnBook(isbn);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/patron/{patronId}")
    public ResponseEntity<List<Loan>> getPatronLoans(@PathVariable String patronId) {
        return ResponseEntity.ok(loanService.getPatronLoans(patronId));
    }

    static class CheckoutRequest {
        public String isbn;
        public String patronId;
    }
}
