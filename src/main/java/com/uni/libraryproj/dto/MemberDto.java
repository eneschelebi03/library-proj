package com.uni.libraryproj.dto;

import com.uni.libraryproj.db.DatabaseConnection;
import com.uni.libraryproj.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDto {

    private final DatabaseConnection databaseConnection;

    public MemberDto(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean addMember(Member member) {
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO members (name, isActive, contacts, membershipType, book_id) VALUES (?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, member.getName());
            preparedStatement.setBoolean(2, member.getActive());
            preparedStatement.setString(3, member.getContacts());
            preparedStatement.setString(4, member.getMembershipType());

            if (member.getBook_id() != null || member.getBook_id() != 0) {
                preparedStatement.setInt(5, member.getBook_id());
            }


            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isMemberExistingAlready(Member member) {
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM members WHERE name = ?")) {

            preparedStatement.setString(1, member.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to retrieve all members from the database
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        try (Connection connection = this.databaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM members")) {

            while (resultSet.next()) {
                Member member = new Member()
                        .setId(resultSet.getInt("id"))
                        .setName(resultSet.getString("name"))
                        .setActive(resultSet.getBoolean("isActive"))
                        .setContacts(resultSet.getString("contacts"))
                        .setMembershipType(resultSet.getString("membershipType"))
                        .setBook_Id(resultSet.getInt("book_id"));
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    // Method to update a member record in the database
    public boolean updateMember(Member member) {
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE members SET name=?, contacts=?, membershipType=?, book_id=? WHERE id=?")) {

            preparedStatement.setString(1, member.getName());
            preparedStatement.setString(2, member.getContacts());
            preparedStatement.setString(3, member.getMembershipType());
            preparedStatement.setInt(4, member.getBook_id());
            preparedStatement.setInt(5, member.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Method to delete a member record from the database
    public boolean deleteMember(int memberId) {
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM members WHERE id=?")) {

            preparedStatement.setInt(1, memberId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
