package com.airtribe.library.service;

import com.airtribe.library.domain.Book;
import com.airtribe.library.exception.BookNotFoundException;
import com.airtribe.library.patterns.*;
import com.airtribe.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;
    private final EntityFactory entityFactory;

    public BookService(BookRepository bookRepository, EntityFactory entityFactory) {
        this.bookRepository = bookRepository;
        this.entityFactory = entityFactory;
    }

    public Book addBook(String isbn, String title, String author, int publicationYear) {
        logger.info("Adding book: ISBN={}, Title={}", isbn, title);
        Book book = entityFactory.createBook(isbn, title, author, publicationYear);
        return bookRepository.save(book);
    }

    public Book updateBook(String isbn, String title, String author, int publicationYear) {
        logger.info("Updating book: ISBN={}", isbn);
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + isbn));
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(publicationYear);
        return bookRepository.save(book);
    }

    public void removeBook(String isbn) {
        logger.info("Removing book: ISBN={}", isbn);
        if (!bookRepository.exists(isbn)) {
            throw new BookNotFoundException("Book not found: " + isbn);
        }
        bookRepository.delete(isbn);
    }

    public Book getBook(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + isbn));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(String searchType, String query) {
        logger.info("Searching books by {}: {}", searchType, query);
        SearchStrategy strategy = switch (searchType.toLowerCase()) {
            case "title" -> new TitleSearchStrategy();
            case "author" -> new AuthorSearchStrategy();
            case "isbn" -> new ISBNSearchStrategy();
            default -> throw new IllegalArgumentException("Invalid search type: " + searchType);
        };
        return strategy.search(bookRepository.findAll(), query);
    }
}
