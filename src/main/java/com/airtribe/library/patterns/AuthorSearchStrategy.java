package com.airtribe.library.patterns;

import com.airtribe.library.domain.Book;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
