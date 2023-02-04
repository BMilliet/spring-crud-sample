package com.example.springcrudsample.service

import com.example.springcrudsample.model.Book
import com.example.springcrudsample.repository.BookRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookServiceImpl(private val repository: BookRepository): BookService {
    override fun create(book: Book): Book = repository.save(book)

    override fun getAll(): List<Book> = repository.findAll()

    override fun getbyId(id: Long): Optional<Book> = repository.findById(id)

    override fun delete(id: Long) = repository.deleteById(id)

    override fun update(id: Long, book: Book): Optional<Book> {
        val bookById = getbyId(id)
        if (bookById.isEmpty) Optional.empty<Book>()

        return bookById.map {
            val bookUpdated = it.copy(
                title = book.title,
                author = book.author,
                publishedAt = book.publishedAt
            )
            repository.save(bookUpdated)
        }
    }
}