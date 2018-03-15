package com.ktar5.mapeditor.gui.dialogs;

import com.ktar5.mapeditor.gui.utils.NumberTextField;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.util.Optional;

import static com.ktar5.mapeditor.gui.utils.GuiUtils.addListener;

@Builder
@Getter
public class CreateBaseTilemap {
    private int width, height, tilesize;
    private File file;

    public static CreateBaseTilemap create() {
        CreateBaseTilemapBuilder builder = new CreateBaseTilemapBuilder();

        // Create the custom dialog.
        Dialog<CreateBaseTilemap> dialog = new Dialog<>();
        dialog.setTitle("Create New BaseTilemap");
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

        NumberTextField width = new NumberTextField("Map Width:");
        NumberTextField heigh = new NumberTextField("Map Height:");
        NumberTextField tilesize = new NumberTextField("Tile Size:");

        Button openFileButton = new Button("Select File");
        openFileButton.setOnAction(event -> {
            openFileButton.setDisable(true);
            File file = fileChooser.showSaveDialog(null);
            openFileButton.setDisable(false);
            if (file != null) {
                filePath.setText(file.getAbsolutePath());
                builder.file(file);
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

        addListener((observable, oldValue, newValue) -> doneButton.setDisable(newValue.trim().isEmpty()),
                width, filePath, heigh, tilesize);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                builder.height(heigh.getNumber());
                builder.width(width.getNumber());
                builder.tilesize(tilesize.getNumber());
                return builder.build();
            }
            return null;
        });

        Optional<CreateBaseTilemap> createDialog = dialog.showAndWait();

        return createDialog.orElse(null);
    }

}