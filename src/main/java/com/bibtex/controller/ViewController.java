package com.bibtex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

  @GetMapping("/library")
  public String libraryAdminPanel() {
    return "libraryView.html";
  }
}
