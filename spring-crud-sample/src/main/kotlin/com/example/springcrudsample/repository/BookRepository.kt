package com.example.springcrudsample.repository

import com.example.springcrudsample.model.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository: JpaRepository<Book, Long> {
}