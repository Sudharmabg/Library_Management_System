package com.airtribe.library.controller;

import com.airtribe.library.domain.Book;
import com.airtribe.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody BookRequest request) {
        Book book = bookService.addBook(request.isbn, request.title, request.author, request.publicationYear);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Book> updateBook(@PathVariable String isbn, @RequestBody BookRequest request) {
        Book book = bookService.updateBook(isbn, request.title, request.author, request.publicationYear);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> removeBook(@PathVariable String isbn) {
        bookService.removeBook(isbn);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBook(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBook(isbn));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String type, @RequestParam String query) {
        return ResponseEntity.ok(bookService.searchBooks(type, query));
    }

    static class BookRequest {
        public String isbn;
        public String title;
        public String author;
        public int publicationYear;
    }
}
