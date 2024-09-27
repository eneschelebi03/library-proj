package com.uni.libraryproj.model;

public class Book {

    private Integer id;

    private String title;

    private String isbn;

    private String author;

    private String genre;

    private boolean available;

    public Book() {
    }

    public Book(String title, String isbn, String author, String genre, boolean available) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.genre = genre;
        this.available = available;
    }

    public Integer getId() {
        return id;
    }

    public Book setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public Book setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public boolean isAvailable() {
        return available;
    }

    public Book setAvailable(boolean available) {
        available = available;
        return this;
    }
}
