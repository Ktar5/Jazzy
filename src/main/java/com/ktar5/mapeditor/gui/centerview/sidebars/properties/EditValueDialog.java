package com.ktar5.mapeditor.gui.centerview.sidebars.properties;

import com.ktar5.mapeditor.properties.StringProperty;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

public class EditValueDialog {
    public static Optional<Pair<String, String>> create(StringProperty property) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Property");
        dialog.setHeaderText("Edit this property");

        ButtonType loginButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField propertyName = new TextField();
        propertyName.setPromptText("Property Name");
        propertyName.setText(property.getName());

        TextField propertyValue = new TextField();
        propertyValue.setPromptText("Property Value");
        propertyValue.setText(property.getValue());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(propertyName, 1, 0);
        grid.add(new Label("Value:"), 0, 1);
        grid.add(propertyValue, 1, 1);

        // Do some validation (using the Java 8 lambda syntax).
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        propertyName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(property.getName())) {
                loginButton.setDisable(false);
            } else {
                loginButton.setDisable(property.getParent().getChildren().containsKey(newValue));
                //todo create information telling user that they already have property of this name
            }
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(propertyName::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(propertyName.getText(), propertyValue.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
