package com.bibtex.controller;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bibtex.entity.Book;
import com.bibtex.repository.BookRepository;

@RestController
public class BookController {

  @Autowired
  private BookRepository bookRepo;

  @PostMapping("/book")
  @ResponseStatus(HttpStatus.CREATED)
  public Book createBook(@RequestBody Book book) {
    validateBook(book);
    book.setCreated(new Date());
    bookRepo.save(book);
    return book;
  }

  private void validateBook(Book book) {
    if (!StringUtils.hasText(book.getAuthor())) {
      throw new IllegalArgumentException("Author of the book cannot be null");
    }
    if (!StringUtils.hasText(book.getTitle())) {
      throw new IllegalArgumentException("Title of the book cannot be null");
    }
    if (!StringUtils.hasText(book.getJournal())) {
      throw new IllegalArgumentException("Journal of the book cannot be null");
    }
    Book duplicateBook = bookRepo.findByAuthorAndTitle(book.getAuthor(), book.getTitle());
    if(duplicateBook != null){
      throw new IllegalArgumentException("Same Author & Title is already there in db. You can't add it.");
    }
  }

  @PutMapping("/book/{id}")
  public void updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
    getBook(id);
    book.setId(id);
    validateBook(book);
    bookRepo.save(book);
  }

  @GetMapping("/book/{id}")
  public Book getBook(@PathVariable("id") Long id) {
    Book book = bookRepo.findOne(id);
    if (book == null) {
      throw new ResourceNotFoundException(null, null);
    }
    return book;
  }

  @GetMapping("/book")
  public Iterable<Book> getAllBook(@RequestParam(value = "sort", required = false) String sortOn,
      @RequestParam(value = "q", required = false) String q) {
    if (sortOn != null)
      return bookRepo.findAll(new Sort(sortOn));

    if (q != null) {
      return bookRepo.findAll(search(q));
    }

    return bookRepo.findAll();
  }

  @DeleteMapping("/book/{id}")
  public void deleteBook(@PathVariable("id") Long id) {
    try {
      bookRepo.delete(id);
    } catch (EmptyResultDataAccessException e) {

    }
  }

  public static Specification<Book> search(String text) {
    if (!text.contains("%")) {
      text = "%" + text + "%";
    }
    String finalText = text;
    return new Specification<Book>() {

      @Override
      public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query,
          CriteriaBuilder builder) {
        return builder.or(builder.like(root.get("author"), finalText),
            builder.like(root.get("title"), finalText), builder.like(root.get("number"), finalText),
            builder.like(root.get("journal"), finalText));
      }

    };
  }

}
