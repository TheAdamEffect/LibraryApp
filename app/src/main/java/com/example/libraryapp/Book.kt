package com.example.libraryapp

data class Book(
    val id: Long = 0,
    val title: String,
    val author: String,
    val genre: String,
    val year: Int
)