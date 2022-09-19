package com.maret.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maret.exception.ResourceNotFoundException;
import com.maret.model.Book;
import com.maret.repository.BookRepository;

@RestController
@RequestMapping("/api")
public class BookController {

  @Autowired
  BookRepository bookRepository;

  @GetMapping("/books")
  public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String title) {
    List<Book> books = new ArrayList<Book>();

    if (title == null)
      bookRepository.findAll().forEach(books::add);
    else
      bookRepository.findByTitleContaining(title).forEach(books::add);

    if (books.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(books, HttpStatus.OK);
  }

  @GetMapping("/books/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable("id") long id) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Book with id = " + id));

    return new ResponseEntity<>(book, HttpStatus.OK);
  }

  @PostMapping("/books")
  public ResponseEntity<Book> createBook(@RequestBody Book book) {
    Book newBook = bookRepository.save(new Book(book.getTitle(), book.getDescription(), false));
    return new ResponseEntity<>(newBook, HttpStatus.CREATED);
  }

  @PutMapping("/books/{id}")
  public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book) {
    Book updatedBook = bookRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Book with id = " + id));

    updatedBook.setTitle(book.getTitle());
    updatedBook.setDescription(book.getDescription());
    updatedBook.setPublished(book.isPublished());
    
    return new ResponseEntity<>(bookRepository.save(updatedBook), HttpStatus.OK);
  }

  @DeleteMapping("/books/{id}")
  public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") long id) {
    bookRepository.deleteById(id);
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/books")
  public ResponseEntity<HttpStatus> deleteAllBooks() {
    bookRepository.deleteAll();
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/books/published")
  public ResponseEntity<List<Book>> findByPublished() {
    List<Book> books = bookRepository.findByPublished(true);

    if (books.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    return new ResponseEntity<>(books, HttpStatus.OK);
  }
}
