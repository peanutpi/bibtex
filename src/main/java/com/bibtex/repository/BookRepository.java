package com.bibtex.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bibtex.entity.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
  
  Iterable<Book> findAll(Specification<Book> spec);
  
  Book findByAuthorAndTitle(String author, String title);

}
