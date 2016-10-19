package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getByName(String name) {
        return bookRepository.findByName(name);
    }

    public List<Book> getByRatingInterval(Double start, Double end) {
        return bookRepository.findByRatingBetween(start, end);
    }

    public void addMovie(Book book) {
        bookRepository.save(book);
    }
}
