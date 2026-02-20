package com.airtribe.library.patterns;

import com.airtribe.library.domain.Book;
import java.util.List;

public interface SearchStrategy {
    List<Book> search(List<Book> books, String query);
}
