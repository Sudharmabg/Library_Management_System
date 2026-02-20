package com.airtribe.library.service;

import com.airtribe.library.domain.Patron;
import com.airtribe.library.exception.PatronNotFoundException;
import com.airtribe.library.patterns.EntityFactory;
import com.airtribe.library.repository.PatronRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatronService {
    private static final Logger logger = LoggerFactory.getLogger(PatronService.class);
    private final PatronRepository patronRepository;
    private final EntityFactory entityFactory;

    public PatronService(PatronRepository patronRepository, EntityFactory entityFactory) {
        this.patronRepository = patronRepository;
        this.entityFactory = entityFactory;
    }

    public Patron addPatron(String name, String email) {
        logger.info("Adding patron: Name={}, Email={}", name, email);
        Patron patron = entityFactory.createPatron(name, email);
        return patronRepository.save(patron);
    }

    public Patron updatePatron(String patronId, String name, String email) {
        logger.info("Updating patron: ID={}", patronId);
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException("Patron not found: " + patronId));
        patron.setName(name);
        patron.setEmail(email);
        return patronRepository.save(patron);
    }

    public Patron getPatron(String patronId) {
        return patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException("Patron not found: " + patronId));
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public List<String> getBorrowingHistory(String patronId) {
        Patron patron = getPatron(patronId);
        return patron.getBorrowingHistory();
    }
}
