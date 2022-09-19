package com.maret.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maret.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	  List<Book> findByPublished(boolean published);

	  List<Book> findByTitleContaining(String title);
	}