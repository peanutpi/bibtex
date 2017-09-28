package com.bibtex.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.bibtex.entity.Book;
import com.bibtex.repository.BookRepository;

@Controller
public class ExportController {


  @Autowired
  private BookRepository bookRepo;

  @GetMapping(value = "/book.csv")
  public void downloadCSV(HttpServletResponse response) throws IOException {

    String csvFileName = "book.csv";

    response.setContentType("text/csv");

    String headerKey = "Content-Disposition";
    String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
    response.setHeader(headerKey, headerValue);

    Iterable<Book> listBooks = bookRepo.findAll();

    ICsvBeanWriter csvWriter =
        new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

    String[] header = {"Id", "Title", "Author", "Year", "Journal", "Volume", "Number", "Pages"};

    csvWriter.writeHeader(header);

    for (Book aBook : listBooks) {
      csvWriter.write(aBook, header);
    }

    csvWriter.close();
  }
}
