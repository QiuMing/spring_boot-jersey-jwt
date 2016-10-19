package com.example;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface BookRepository extends ElasticsearchRepository<Book, Long> {

    List<Book> findByName(String name);

    List<Book> findByRatingBetween(Double start, Double end);
}
