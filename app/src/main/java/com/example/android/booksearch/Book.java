package com.example.android.booksearch;

/**
 * Created by james.beaton on 24/06/2018.
 */

public class Book {

    /** define 2 global variables of type string*/
    /**
     * book title
     */
    private String mBook;
    /**
     * book author
     */
    private String mAuthor;

    /**
     * create public constructor and initialise the two member variables
     * based on the values passed into the constructor
     */
    public Book(String book, String author) {
        mBook = book;
        mAuthor = author;
    }

    /**
     * create public getter methods for each private global
     * variable so that other classes can access them
     */

    public String getmBook() {
        return mBook;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}