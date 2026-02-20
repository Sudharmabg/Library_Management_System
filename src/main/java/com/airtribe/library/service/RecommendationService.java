package com.airtribe.library.service;

import com.airtribe.library.domain.*;
import com.airtribe.library.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    private final PatronRepository patronRepository;
    private final BookRepository bookRepository;

    public RecommendationService(PatronRepository patronRepository, BookRepository bookRepository) {
        this.patronRepository = patronRepository;
        this.bookRepository = bookRepository;
    }

    public List<Book> getRecommendations(String patronId) {
        logger.info("Generating recommendations for patron: {}", patronId);
        
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new RuntimeException("Patron not found: " + patronId));

        List<String> borrowedIsbns = patron.getBorrowingHistory();
        if (borrowedIsbns.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Integer> authorFrequency = new HashMap<>();
        for (String isbn : borrowedIsbns) {
            bookRepository.findByIsbn(isbn).ifPresent(book -> 
                authorFrequency.merge(book.getAuthor(), 1, Integer::sum)
            );
        }

        String favoriteAuthor = authorFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (favoriteAuthor == null) {
            return Collections.emptyList();
        }

        return bookRepository.findAll().stream()
                .filter(book -> book.getAuthor().equals(favoriteAuthor))
                .filter(book -> !borrowedIsbns.contains(book.getIsbn()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
