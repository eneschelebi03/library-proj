package com.uni.libraryproj.dto;

import com.uni.libraryproj.db.DatabaseConnection;
import com.uni.libraryproj.model.Loan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDto {

    private final DatabaseConnection databaseConnection;

    public LoanDto(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }


    public boolean addLoan(Loan loan) {
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO loans (book_id, loaner_id, loanDate, returnDate) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setInt(1, loan.getBook_id());
            preparedStatement.setInt(2, loan.getLoaner_id());
            preparedStatement.setDate(3, new java.sql.Date(loan.getLoanDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(loan.getReturnDate().getTime()));

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to retrieve all loans from the database
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        try (Connection connection = this.databaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM loans")) {

            while (resultSet.next()) {
                Loan loan = new Loan()
                        .setId(resultSet.getInt("id"))
                        .setBook_id(resultSet.getInt("book_id"))
                        .setLoaner_id(resultSet.getInt("loaner_id"))
                        .setLoanDate(resultSet.getDate("loanDate"))
                        .setReturnDate(resultSet.getDate("returnDate"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    // Method to update a loan record in the database
    public boolean updateLoan(Loan loan) {
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE loans SET loanDate=?, returnDate=? WHERE book_id=? AND loaner_id=?")) {

            preparedStatement.setDate(1, new java.sql.Date(loan.getLoanDate().getTime()));
            preparedStatement.setDate(2, new java.sql.Date(loan.getReturnDate().getTime()));
            preparedStatement.setInt(3, loan.getBook_id());
            preparedStatement.setInt(4, loan.getLoaner_id());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete a loan record from the database
    public boolean deleteLoan(int loanId) {
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM loans WHERE id=?")) {

            preparedStatement.setInt(1, loanId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
