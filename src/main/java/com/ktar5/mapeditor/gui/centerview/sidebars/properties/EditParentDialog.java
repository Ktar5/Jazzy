package com.ktar5.mapeditor.gui.centerview.sidebars.properties;

import com.ktar5.mapeditor.properties.ParentProperty;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class EditParentDialog {
    //TODO show path
    public static Optional<String> create(ParentProperty property) {
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
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

        grid.add(new Label("Name:"), 0, 0);
        grid.add(propertyName, 1, 0);

        // Do some validation (using the Java 8 lambda syntax).
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        propertyName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(property.getName())){
                loginButton.setDisable(false);
            }else{
                loginButton.setDisable(property.getParent().getChildren().containsKey(newValue));
                //todo create information telling user that they already have property of this name
            }
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> propertyName.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return propertyName.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        return result;
    }
}
