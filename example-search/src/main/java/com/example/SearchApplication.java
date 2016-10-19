package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/*
Spring Boot 启动加载数据 CommandLineRunner
 */
@SpringBootApplication
public class SearchApplication implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(SearchApplication.class);

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

    private Book getFirstMovie() {
        Book firstBook = new Book();
        firstBook.setId(1);
        firstBook.setRating(8.4d);
        firstBook.setName("第一本书");

        List<Genre> firstBookGenre = new ArrayList<Genre>();
        firstBookGenre.add(new Genre("戏剧"));
        firstBookGenre.add(new Genre("动作"));

        firstBook.setGenre(firstBookGenre);

        return firstBook;
    }

    private Book getSecondMovie() {
        Book secondBook = new Book();
        secondBook.setId(2);
        secondBook.setRating(8.4d);
        secondBook.setName("第二本书");

        List<Genre> secondBookGenre = new ArrayList<Genre>();
        secondBookGenre.add(new Genre("浪漫"));
        secondBookGenre.add(new Genre("动作"));

        secondBook.setGenre(secondBookGenre);

        return secondBook;
    }

    @Override
    public void run(String... strings) throws Exception {
        Book book1 = getFirstMovie();
        bookService.addMovie(book1);

        Book book2 = getSecondMovie();
        bookService.addMovie(book2);

        List<Book> dabanggNamedQuery = bookService.getByName("第一本书");
        logger.info("Content of 第一本书 {}", dabanggNamedQuery);

        List<Book> readyBookQuery = bookService.getByName("第一");
        logger.info("Content of 第一本书 {}", readyBookQuery);

        List<Book> byRating = bookService.getByRatingInterval(7d, 9d);
        logger.info("Content of book by rating 7 9 {}", byRating);
    }
}
