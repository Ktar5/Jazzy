package com.ktar5.mapeditor.tilemap.dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Builder
@Getter
public class CreateDialog {
    int width, height, tilesize;
    String name;
    File file;

    public static CreateDialog create() {
        CreateDialogBuilder builder = new CreateDialogBuilder();


        // Create the custom dialog.
        Dialog<CreateDialog> dialog = new Dialog<>();
        dialog.setTitle("Create New Tilemap");
        //dialog.setHeaderText("Look, a Custom Login Dialog");

        // Set the icon (must be included in the project).
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));


        TextField filePath = new TextField();
        filePath.setEditable(false);

        TextField width = new TextField();
        TextField heigh = new TextField();
        TextField tilesize = new TextField();

        width.setPromptText("Map Width");
        heigh.setPromptText("Map Height");
        tilesize.setPromptText("Tile Size");


        width.setTextFormatter(new NumberText());
        heigh.setTextFormatter(new NumberText());
        tilesize.setTextFormatter(new NumberText());

        Button openFileButton = new Button("Select File");
        openFileButton.setOnAction(event -> {
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                filePath.setText(file.getAbsolutePath());
                builder.file(file);
                builder.name(file.getName());
            }
        });

        grid.add(new Label("File Path:"), 0, 0);
        grid.add(openFileButton, 2, 0);
        grid.add(filePath, 1, 0);
        grid.add(new Label("Width:"), 0, 1);
        grid.add(width, 1, 1);
        grid.add(new Label("Height:"), 0, 2);
        grid.add(heigh, 1, 2);
        grid.add(new Label("Tile Size:"), 0, 3);
        grid.add(tilesize, 1, 3);

        // Enable/Disable login button depending on whether a username was entered.
        Node doneButton = dialog.getDialogPane().lookupButton(loginButtonType);
        doneButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        width.textProperty().addListener((observable, oldValue, newValue) -> {
            doneButton.setDisable(newValue.trim().isEmpty());
        });
        heigh.textProperty().addListener((observable, oldValue, newValue) -> {
            doneButton.setDisable(newValue.trim().isEmpty());
        });
        tilesize.textProperty().addListener((observable, oldValue, newValue) -> {
            doneButton.setDisable(newValue.trim().isEmpty());
        });
        filePath.textProperty().addListener((observable, oldValue, newValue) -> {
            doneButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(width::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                builder.height(Integer.valueOf(heigh.getText()));
                builder.width(Integer.valueOf(width.getText()));
                builder.tilesize(Integer.valueOf(tilesize.getText()));
                return builder.build();
            }
            return null;
        });

        Optional<CreateDialog> createDialog = dialog.showAndWait();

        return createDialog.orElse(null);
    }

    public static class NumberText extends TextFormatter {

        public NumberText() {
            super(new UnaryOperator<Change>() {
                @Override
                public Change apply(Change change) {
                    String text = change.getText();
                    if (text.matches("[0-9]*")) {
                        return change;
                    }
                    return null;

                }
            });
        }
    }

}
