package com.uni.libraryproj.scene;

import com.uni.libraryproj.db.DatabaseConnection;
import com.uni.libraryproj.dto.MemberDto;
import com.uni.libraryproj.model.Member;
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

public class MemberViewController implements Initializable {

    @FXML
    private TextField memberName;
    @FXML
    private TextField memberContactInfo;
    @FXML
    private TextField memberMembershipType;
    @FXML
    private TextField memberBookId;

    @FXML
    private Button memberAdd;
    @FXML
    private Button memberUpdate;
    @FXML
    private Button memberDelete;
    @FXML
    private Button memberClear;


    @FXML
    private TableView<Member> memberTableView;
    @FXML
    private TableColumn<Member, String> memberColumnName;
    @FXML
    private TableColumn<Member, String> memberColumnContacts;
    @FXML
    private TableColumn<Member, String> memberColumnMembershipType;
    @FXML
    private TableColumn<Member, Integer> memberColumnBookId;

    @FXML
    private TextField memberSearchField;


    private final ObservableList<Member> memberList = FXCollections.observableArrayList();
    private final MemberDto memberDTO;
    private final FilteredList<Member> filteredData;


    public MemberViewController() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        memberDTO = new MemberDto(databaseConnection);
        filteredData = new FilteredList<>(memberList, p -> true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberAdd.setOnAction(this::handleButtonClickAdd);
        memberUpdate.setOnAction(this::handleButtonClickUpdate);
        memberDelete.setOnAction(this::handleButtonClickDelete);
        memberClear.setOnAction(this::clearTextFields);
        selectRowInTheTableView();

        memberSearchField.setOnKeyPressed(this::setSearchWhenButtonEnterIsPressed);

        memberColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        memberColumnContacts.setCellValueFactory(new PropertyValueFactory<>("contacts"));
        memberColumnMembershipType.setCellValueFactory(new PropertyValueFactory<>("membershipType"));
        memberColumnBookId.setCellValueFactory(new PropertyValueFactory<>("book_id"));

        loadDataFromDatabase();
    }


    private void handleButtonClickAdd(ActionEvent event) {
        System.out.println("Add Button is clicked");
        int bookId = 0;

        if (!memberBookId.getText().isEmpty()) {
            bookId = Integer.parseInt(memberBookId.getText());
        }
        Member newMember = new Member(memberName.getText(), true, memberContactInfo.getText(), memberMembershipType.getText(), bookId);

        if (memberDTO.isMemberExistingAlready(newMember)) {
            System.out.println("Member already existing in database.");
            return;
        }

        memberDTO.addMember(newMember);
        memberList.add(newMember);
        memberTableView.setItems(memberList);
        System.out.println("Member added to database successfully.");
    }

    private void handleButtonClickUpdate(ActionEvent event) {
        System.out.println("Update Button is clicked");

        Member selectedMember = memberTableView.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            return;
        }

        int memberIdentificationNumber = selectedMember.getId();

        Member updatedMember = new Member(memberName.getText(), true, memberContactInfo.getText(), memberMembershipType.getText(), Integer.parseInt(memberBookId.getText()));
        updatedMember.setId(memberIdentificationNumber);
        memberDTO.updateMember(updatedMember);
        System.out.println("Member updated successfully.");
        int selectedIndex = memberTableView.getSelectionModel().getSelectedIndex();
        memberList.set(selectedIndex, updatedMember);
    }


    private void handleButtonClickDelete(ActionEvent event) {
        System.out.println("Delete Button is clicked");

        Member selectedMember = memberTableView.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            System.out.println("No member selected for deletion.");
            return;
        }

        int memberIdentificationNumber = selectedMember.getId();

        memberDTO.deleteMember(memberIdentificationNumber);
        memberList.remove(selectedMember);
        System.out.println("Member deleted successfully.");
    }

    private void selectRowInTheTableView() {
        memberTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            if (newSelection != null) {
                memberName.setText(newSelection.getName());
                memberContactInfo.setText(newSelection.getContacts());
                memberMembershipType.setText(newSelection.getMembershipType());
                memberBookId.setText(String.valueOf(newSelection.getBook_id()));
            } else {
                clearTextFields();
            }
        });
    }

    private void clearTextFields(ActionEvent event) {
        clearTextFields();
    }

    private void clearTextFields() {
        memberName.clear();
        memberContactInfo.clear();
        memberMembershipType.clear();
        memberBookId.clear();
    }


    private void loadDataFromDatabase() {

        List<Member> members = memberDTO.getAllMembers();
        memberList.addAll(members);
        memberTableView.setItems(memberList);
    }

    private void setSearchWhenButtonEnterIsPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            searchMember();
        }
    }

    private void searchMember() {
        String searchText = memberSearchField.getText().toLowerCase();

        filteredData.setPredicate(member -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return member.getName().toLowerCase().contains(searchText)
                    || member.getContacts().toLowerCase().contains(searchText)
                    || member.getMembershipType().toLowerCase().contains(searchText)
                    || member.getBook_id().toString().contains(searchText);
        });

        memberTableView.setItems(filteredData);
    }
}
