package com.uni.libraryproj.scene;

import com.uni.libraryproj.db.DatabaseConnection;
import com.uni.libraryproj.dto.BookDto;
import com.uni.libraryproj.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BookViewController implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField genreField;
    @FXML
    private CheckBox availableCheckBox;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;

    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TableColumn<Book, Boolean> availableColumn;

    @FXML
    private TextField searchField;

    private final ObservableList<Book> bookList = FXCollections.observableArrayList();
    private final BookDto bookDto;
    private final FilteredList<Book> filteredData;

    public BookViewController() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        bookDto = new BookDto(databaseConnection);
        filteredData = new FilteredList<>(bookList, p -> true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setOnAction(this::handleAddButton);
        updateButton.setOnAction(this::handleUpdateButton);
        deleteButton.setOnAction(this::handleDeleteButton);
        clearButton.setOnAction(this::handleClearButton);

        initializeTableView();

        searchField.setOnKeyPressed(this::handleSearchEnterKeyPressed);

        loadDataFromDatabase();
    }

    private void initializeTableView() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        bookTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayBookDetails(newSelection);
            } else {
                clearFields();
            }
        });
    }

    private void handleAddButton(ActionEvent event) {
        Book newBook = createBookFromFields();

        if (bookDto.addBook(newBook)) {
            bookList.add(newBook);
            bookTableView.setItems(bookList);
            clearFields();
        } else {
            System.out.println("Failed to add book to the database.");
        }
    }

    private void handleUpdateButton(ActionEvent event) {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            return;
        }

        Book updatedBook = createBookFromFields();
        updatedBook.setId(selectedBook.getId());

        if (bookDto.updateBook(updatedBook)) {
            int selectedIndex = bookTableView.getSelectionModel().getSelectedIndex();
            bookList.set(selectedIndex, updatedBook);
            clearFields();
        } else {
            System.out.println("Failed to update book in the database.");
        }
    }

    private void handleDeleteButton(ActionEvent event) {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            return;
        }

        if (bookDto.deleteBook(selectedBook.getId())) {
            bookList.remove(selectedBook);
            clearFields();
        } else {
            System.out.println("Failed to delete book from the database.");
        }
    }

    private void handleClearButton(ActionEvent event) {
        clearFields();
    }

    private void handleSearchEnterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            searchBooks();
        }
    }

    private void loadDataFromDatabase() {
        List<Book> books = bookDto.getAllBooks();
        bookList.addAll(books);
        bookTableView.setItems(bookList);
    }

    private void searchBooks() {
        String searchText = searchField.getText().toLowerCase();

        filteredData.setPredicate(book -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return book.getTitle().toLowerCase().contains(searchText)
                    || book.getIsbn().toLowerCase().contains(searchText)
                    || book.getAuthor().toLowerCase().contains(searchText)
                    || book.getGenre().toLowerCase().contains(searchText)
                    || String.valueOf(book.isAvailable()).contains(searchText);
        });

        bookTableView.setItems(filteredData);
    }

    private Book createBookFromFields() {
        String title = titleField.getText();
        String isbn = isbnField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();
        boolean available = availableCheckBox.isSelected();

        return new Book(title, isbn, author, genre, available);
    }

    private void displayBookDetails(Book book) {
        titleField.setText(book.getTitle());
        isbnField.setText(book.getIsbn());
        authorField.setText(book.getAuthor());
        genreField.setText(book.getGenre());
        availableCheckBox.setSelected(book.isAvailable());
    }

    private void clearFields() {
        titleField.clear();
        isbnField.clear();
        authorField.clear();
        genreField.clear();
        availableCheckBox.setSelected(false);
    }
}
