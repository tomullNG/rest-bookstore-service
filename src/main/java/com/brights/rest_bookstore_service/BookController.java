package com.brights.rest_bookstore_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/book")
    public List<Book> getBooks() {
        System.out.println("API Version 2.0");
        return (List<Book>)bookRepository.findAll();
    }

    @GetMapping("/book/{id}")
    public Book getBook(@PathVariable long id) {
        return bookRepository.findBookById(id);
    }

    @PostMapping("/book")
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/book/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book book = bookRepository.findById(id).isPresent() ? bookRepository.findById(id).get() : null;

        if (book != null) {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPrice(updatedBook.getPrice());
            bookRepository.save(book);
            return book;
        }
        return null;
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        Book book = bookRepository.findBookById(id);
        bookRepository.delete(book);
        return ResponseEntity.noContent().build();
    }
}
