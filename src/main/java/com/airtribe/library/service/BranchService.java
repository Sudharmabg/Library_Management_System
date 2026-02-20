package com.airtribe.library.service;

import com.airtribe.library.domain.*;
import com.airtribe.library.exception.*;
import com.airtribe.library.patterns.EntityFactory;
import com.airtribe.library.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BranchService {
    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);
    private final BranchRepository branchRepository;
    private final BookRepository bookRepository;
    private final EntityFactory entityFactory;

    public BranchService(BranchRepository branchRepository, BookRepository bookRepository,
                         EntityFactory entityFactory) {
        this.branchRepository = branchRepository;
        this.bookRepository = bookRepository;
        this.entityFactory = entityFactory;
    }

    public Branch createBranch(String name, String location) {
        logger.info("Creating branch: Name={}, Location={}", name, location);
        Branch branch = entityFactory.createBranch(name, location);
        return branchRepository.save(branch);
    }

    public Branch getBranch(String branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found: " + branchId));
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public void assignBookToBranch(String isbn, String branchId) {
        logger.info("Assigning book {} to branch {}", isbn, branchId);
        
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + isbn));
        
        Branch branch = getBranch(branchId);

        if (book.getBranchId() != null) {
            Branch oldBranch = getBranch(book.getBranchId());
            oldBranch.removeBook(isbn);
            branchRepository.save(oldBranch);
        }

        book.setBranchId(branchId);
        bookRepository.save(book);

        branch.addBook(isbn);
        branchRepository.save(branch);
        logger.info("Book assigned successfully");
    }

    public void transferBook(String isbn, String fromBranchId, String toBranchId) {
        logger.info("Transferring book {} from {} to {}", isbn, fromBranchId, toBranchId);
        
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + isbn));

        if (!fromBranchId.equals(book.getBranchId())) {
            throw new IllegalArgumentException("Book is not in the source branch");
        }

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException("Cannot transfer borrowed book");
        }

        assignBookToBranch(isbn, toBranchId);
        logger.info("Book transferred successfully");
    }
}
