package com.example.springcrudsample.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id


@Entity(name = "books")
data class Book(
        @Id @GeneratedValue
        val id: Long? = null,
        val title: String,
        val author: String,
        val publishedAt: Int
)