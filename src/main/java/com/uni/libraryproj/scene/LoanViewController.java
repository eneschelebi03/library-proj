package com.uni.libraryproj.scene;

import com.uni.libraryproj.db.DatabaseConnection;
import com.uni.libraryproj.dto.LoanDto;
import com.uni.libraryproj.model.Loan;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class LoanViewController implements Initializable {

    @FXML
    private TextField loanMemberId;
    @FXML
    private TextField loanBookId;
    @FXML
    private TextField loanDueDate;
    @FXML
    private TextField loanDate;

    @FXML
    private Button loanAdd;
    @FXML
    private Button loanUpdate;
    @FXML
    private Button loanDelete;
    @FXML
    private Button loanClear;

    @FXML
    private TableView<Loan> loanTableView;
    @FXML
    private TableColumn<Loan, Integer> loanColumnMemberId;
    @FXML
    private TableColumn<Loan, Integer> loanColumnBookId;
    @FXML
    private TableColumn<Loan, Date> loanColumnLoanDate;
    @FXML
    private TableColumn<Loan, Date> loanColumnDueDate;

    @FXML
    private TextField loanSearchField;

    private final ObservableList<Loan> loanList = FXCollections.observableArrayList();
    private final LoanDto loanDTO;
    private final FilteredList<Loan> filteredData;

    public LoanViewController() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        loanDTO = new LoanDto(databaseConnection);
        filteredData = new FilteredList<>(loanList, p -> true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanAdd.setOnAction(this::handleButtonClickAdd);
        loanUpdate.setOnAction(this::handleButtonClickUpdate);
        loanDelete.setOnAction(this::handleButtonClickDelete);
        loanClear.setOnAction(this::clearTextFields);
        selectRowInTheTableView();

        loanSearchField.setOnKeyPressed(this::setSearchWhenButtonEnterIsPressed);

        loanColumnMemberId.setCellValueFactory(new PropertyValueFactory<>("member_id"));
        loanColumnBookId.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        loanColumnLoanDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        loanColumnDueDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        loadDataFromDatabase();
    }

    private void handleButtonClickAdd(ActionEvent event) {
        System.out.println("Add Button is clicked");

        Integer memberId = Integer.parseInt(loanMemberId.getText());
        Integer bookId = Integer.parseInt(loanBookId.getText());

        String loanDateString = loanDate.getText();
        Date loanDate = null;
        try {
            loanDate = new SimpleDateFormat("yyyy-MM-dd").parse(loanDateString);
        } catch (ParseException e) {
            System.err.println("Error parsing loan date: " + e.getMessage());
        }

        String dueDateString = loanDueDate.getText();
        Date dueDate = null;
        try {
            dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateString);
        } catch (ParseException e) {
            System.err.println("Error parsing due date: " + e.getMessage());
        }

        Loan newLoan = new Loan(memberId, bookId, loanDate, dueDate);

        loanDTO.addLoan(newLoan);
        loanList.add(newLoan);
        loanTableView.setItems(loanList);

        System.out.println("Loan added to database successfully.");
    }

    private void handleButtonClickUpdate(ActionEvent event) {
        System.out.println("Update Button is clicked");

        Loan selectedLoan = loanTableView.getSelectionModel().getSelectedItem();

        if (selectedLoan == null) {
            return;
        }

        int loanId = selectedLoan.getId();
        Integer memberId = Integer.parseInt(loanMemberId.getText());
        Integer bookId = Integer.parseInt(loanBookId.getText());

        String loanDateString = loanDate.getText();
        Date loanDate = null;
        try {
            loanDate = new SimpleDateFormat("yyyy-MM-dd").parse(loanDateString);
        } catch (ParseException e) {
            System.err.println("Error parsing loan date: " + e.getMessage());
        }

        String dueDateString = loanDueDate.getText();
        Date dueDate = null;
        try {
            dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateString);
        } catch (ParseException e) {
            System.err.println("Error parsing due date: " + e.getMessage());
        }

        Loan updatedLoan = new Loan(memberId, bookId, loanDate, dueDate);
        updatedLoan.setId(loanId);

        loanDTO.updateLoan(updatedLoan);
        System.out.println("Loan updated successfully.");
        int selectedIndex = loanTableView.getSelectionModel().getSelectedIndex();
        loanList.set(selectedIndex, updatedLoan);
    }

    private void handleButtonClickDelete(ActionEvent event) {
        System.out.println("Delete Button is clicked");

        Loan selectedLoan = loanTableView.getSelectionModel().getSelectedItem();

        if (selectedLoan == null) {
            System.out.println("No loan selected for deletion.");
            return;
        }

        int loanId = selectedLoan.getId();

        loanDTO.deleteLoan(loanId);
        loanList.remove(selectedLoan);
        System.out.println("Loan deleted successfully.");
    }

    private void selectRowInTheTableView() {
        loanTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loanMemberId.setText(String.valueOf(newSelection.getMember_id()));
                loanBookId.setText(String.valueOf(newSelection.getBook_id()));
                loanDueDate.setText(newSelection.getReturnDate().toString());
                loanDate.setText(newSelection.getLoanDate().toString());
            } else {
                clearTextFields();
            }
        });
    }

    private void clearTextFields(ActionEvent event) {
        clearTextFields();
    }

    private void clearTextFields() {
        loanMemberId.clear();
        loanBookId.clear();
        loanDueDate.clear();
        loanDate.clear();
    }

    private void loadDataFromDatabase() {
        List<Loan> loans = loanDTO.getAllLoans();
        loanList.addAll(loans);
        loanTableView.setItems(filteredData);
    }

    private void setSearchWhenButtonEnterIsPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            searchLoan();
        }
    }

    private void searchLoan() {
        String searchText = loanSearchField.getText().toLowerCase();

        filteredData.setPredicate(loan -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return loan.getMember_id().toString().contains(searchText)
                    || loan.getBook_id().toString().contains(searchText)
                    || loan.getLoanDate().toString().contains(searchText)
                    || loan.getReturnDate().toString().contains(searchText);
        });

        loanTableView.setItems(filteredData);
    }
}
