module com.uni.libraryproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.uni.libraryproj.scene to javafx.fxml;
    opens com.uni.libraryproj to javafx.fxml;
    opens com.uni.libraryproj.model to javafx.base;

    exports com.uni.libraryproj;
    exports com.uni.libraryproj.scene;
}