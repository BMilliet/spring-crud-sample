package com.example.springcrudsample

import com.example.springcrudsample.model.Book
import com.example.springcrudsample.repository.BookRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bookRepository: BookRepository

    @Test
    fun `test find all`() {
        bookRepository.deleteAll()
        bookRepository.save(Book(title = "Crime and Punishment", author = "Fyodor Dostoevsky", publishedAt = 1866))

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].title").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].author").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].publishedAt").isNumber)
    }

    @Test
    fun `test find by id`() {
        bookRepository.deleteAll()
        val book = bookRepository.save(Book(title = "The Brothers Karamazov", author = "Fyodor Dostoevsky", publishedAt = 1880))

        mockMvc.perform(MockMvcRequestBuilders.get("/books/${book.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(book.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value(book.title))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.author").value(book.author))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.publishedAt").value(book.publishedAt))
    }

    @Test
    fun `test create book`() {
        bookRepository.deleteAll()
        val book = bookRepository.save(Book(title = "The Brothers Karamazov", author = "Fyodor Dostoevsky", publishedAt = 1880))
        val bookJSON = ObjectMapper().writeValueAsString(book)

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookJSON))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(book.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value(book.title))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.author").value(book.author))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.publishedAt").value(book.publishedAt))
    }

    @Test
    fun `test update book`() {
        bookRepository.deleteAll()
        val book = bookRepository
            .save(Book(title = "Notes from", author = "Fyodor Dostoevsky", publishedAt = 1864))
            .copy(title = "Notes from Underground")

        val bookJSON = ObjectMapper().writeValueAsString(book)

        mockMvc.perform(MockMvcRequestBuilders.put("/books/${book.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookJSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(book.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value(book.title))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.author").value(book.author))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.publishedAt").value(book.publishedAt))

        val bookById = bookRepository.findById(book.id!!)

        Assertions.assertTrue(bookById.isPresent)
        Assertions.assertEquals(book.title, bookById.get().title)
    }

    @Test
    fun `test delete book`() {
        bookRepository.deleteAll()
        val book = bookRepository.save(Book(title = "The Brothers Karamazov", author = "Fyodor Dostoevsky", publishedAt = 1880))

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/${book.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        val bookById = bookRepository.findById(book.id!!)
        Assertions.assertFalse(bookById.isPresent)
    }
}