package com.example.springcrudsample.service

import com.example.springcrudsample.model.Book
import java.util.*

interface BookService {
    fun create(book: Book): Book
    fun getAll(): List<Book>
    fun getbyId(id: Long): Optional<Book>
    fun update(id: Long, book: Book): Optional<Book>
    fun delete(id: Long)
}