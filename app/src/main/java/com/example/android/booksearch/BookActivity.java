package com.example.android.booksearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class BookActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookActivity.class.getName();

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;

    /**
     * circle loading bar
     */
    private View circleProgressBar;

    /**
     * URL for books data from the Google Books API
     */
    private String mUrlRequestGoogleBooks = "";
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    /**
     * Search field
     */
    private SearchView mSearchViewField;

    boolean isConnected;

    ListView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        // Declaration and initialization ConnectivityManager for checking internet connection
        final ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        /**
         * At the beginning check the connection with internet and save result to (boolean) variable isConnected
         * Checking if network is available
         * If TRUE - work with LoaderManager
         * If FALSE - hide loading spinner and show emptyStateTextView
         */
        checkConnection(cm);

        // Find a reference to the {@link ListView} in the layout
        bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Find a reference to the empty view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Circle progress
        circleProgressBar = findViewById(R.id.loading_indicator);

        // search button
        Button mSearchButton = (Button) findViewById(R.id.search_button);

        // Search field
        mSearchViewField = (SearchView) findViewById(R.id.search_view_field);
        mSearchViewField.onActionViewExpanded();
        mSearchViewField.setIconified(true);
        mSearchViewField.setQueryHint(getString(R.string.search_hint));

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader.
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Progress bar mapping
            circleProgressBar.setVisibility(GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        // Set an item click listener on the Search Button, which sends a request to
        // Google Books API based on value from Search View
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check connection status
                checkConnection(cm);

                if (isConnected) {
                    // Update URL and restart loader to displaying new result of searching
                    updateQueryUrl(mSearchViewField.getQuery().toString());
                    restartLoader();
                } else {
                    // Clear the adapter of previous book data
                    mAdapter.clear();
                    // Set mEmptyStateTextView visible
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    // ...and display message: "No internet connection."
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });
    }

    /**
     * Check if query contains spaces if YES replace these with PLUS sign
     *
     * @param searchValue - user data from SearchView
     * @return improved String URL for making HTTP request
     */
    private String updateQueryUrl(String searchValue) {

        if (searchValue.contains(" ")) {
            searchValue = searchValue.replace(" ", "+");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.google_url_api)).append(searchValue).append(getString(R.string.max_result));
        mUrlRequestGoogleBooks = sb.toString();
        return mUrlRequestGoogleBooks;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL

        updateQueryUrl(mSearchViewField.getQuery().toString());
        return new BookLoader(this, mUrlRequestGoogleBooks);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        // Hide loading indicator because the data has been loaded
        View circleProgressBar = findViewById(R.id.loading_indicator);
        circleProgressBar.setVisibility(View.GONE);


        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books_available);

        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    public void restartLoader() {
        mEmptyStateTextView.setVisibility(GONE);
        circleProgressBar.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BookActivity.this);
    }

    public void checkConnection(ConnectivityManager connectivityManager) {
        // Status of internet connection
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            isConnected = true;

        } else {
            isConnected = false;
        }
    }
}