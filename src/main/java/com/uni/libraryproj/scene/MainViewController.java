package com.uni.libraryproj.scene;

import com.uni.libraryproj.dto.BookDto;
import com.uni.libraryproj.dto.LoanDto;
import com.uni.libraryproj.dto.MemberDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class MainViewController {

    @FXML
    private StackPane contentPane;

    @FXML
    private void loadScene(String fxmlPath) {
        try {
            AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadMembersScene() {
        loadScene("/com/uni/libraryproj/member-view.fxml");
    }

    @FXML
    private void loadBooksScene() {
        loadScene("/com/uni/libraryproj/book-view.fxml");
    }

    @FXML
    private void loadLoansScene() {
        loadScene("/com/uni/libraryproj/loan-view.fxml");
    }
}
