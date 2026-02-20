package com.airtribe.library.repository;

import com.airtribe.library.domain.Patron;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PatronRepository {
    private final Map<String, Patron> patrons = new ConcurrentHashMap<>();

    public Patron save(Patron patron) {
        patrons.put(patron.getPatronId(), patron);
        return patron;
    }

    public Optional<Patron> findById(String patronId) {
        return Optional.ofNullable(patrons.get(patronId));
    }

    public List<Patron> findAll() {
        return new ArrayList<>(patrons.values());
    }

    public void delete(String patronId) {
        patrons.remove(patronId);
    }
}
