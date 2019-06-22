package com.example.android.booksearch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An {@link BookAdapter} knows how to create a list item layout for each book
 * in the data source (a list of {@link Book} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Constructs a new {@link BookAdapter}.
     *
     * @param context of the app
     * @param books   is the list of books, which is the data source of the adapter
     */
    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the book at the given position
     * in the list of books.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (convertView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        //current position of book
        Book currentBook = getItem(position);

        // Find the TextView's for book title and the author name in the list item xml
        TextView nameOfBook = (TextView) listItemView.findViewById(R.id.booktitle);
        TextView nameOfAuthor = (TextView) listItemView.findViewById(R.id.author);

        //set value for each
        assert currentBook != null;
        nameOfBook.setText(currentBook.getmBook());
        nameOfAuthor.setText(currentBook.getmAuthor());

        return listItemView;
    }
}