package com.airtribe.library.repository;

import com.airtribe.library.domain.Book;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BookRepository {
    private final Map<String, Book> books = new ConcurrentHashMap<>();

    public Book save(Book book) {
        books.put(book.getIsbn(), book);
        return book;
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public void delete(String isbn) {
        books.remove(isbn);
    }

    public boolean exists(String isbn) {
        return books.containsKey(isbn);
    }
}
