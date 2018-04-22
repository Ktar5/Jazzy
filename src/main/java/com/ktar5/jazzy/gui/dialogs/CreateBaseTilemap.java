package com.ktar5.jazzy.gui.dialogs;

import com.ktar5.jazzy.gui.utils.NumberTextField;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.util.Optional;

import static com.ktar5.jazzy.gui.utils.GuiUtils.addListener;

@Builder
@Getter
public class CreateBaseTilemap {
    private int width, height, tileHeight, tileWidth;
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
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));
        
        
        TextField filePath = new TextField();
        filePath.setMaxWidth(240);
        filePath.setPrefWidth(filePath.getMaxWidth());
        filePath.setEditable(false);
        
        NumberTextField width = new NumberTextField("Map Width:");
        NumberTextField height = new NumberTextField("Map Height:");
        NumberTextField tileHeight = new NumberTextField("Tile Height:");
        NumberTextField tileWidth = new NumberTextField("Tile Width:");
        
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
        
        
        GridPane largeValues = new GridPane();
        largeValues.setHgap(10);
        largeValues.setVgap(10);
        largeValues.setPadding(new Insets(20, 150, 10, 10));
        
        largeValues.add(new Label("File Path:"), 0, 0);
        largeValues.add(filePath, 1, 0);
        largeValues.add(openFileButton, 4, 0);
        
        GridPane smallValues = new GridPane();
        smallValues.setHgap(10);
        smallValues.setVgap(10);
        smallValues.setPadding(new Insets(20, 10, 10, 10));
        
        smallValues.add(new Label("Width:"), 0, 0);
        smallValues.add(width, 1, 0);
        
        smallValues.add(new Label("Height:"), 0, 1);
        smallValues.add(height, 1, 1);
        
        
        smallValues.add(new Label("Tile Height:"), 2, 0);
        smallValues.add(tileHeight, 3, 0);
        
        smallValues.add(new Label("Tile Width:"), 2, 1);
        smallValues.add(tileWidth, 3, 1);
        
        // Enable/Disable login button depending on whether a username was entered.
        Node doneButton = dialog.getDialogPane().lookupButton(loginButtonType);
        doneButton.setDisable(true);
        
        addListener((observable, oldValue, newValue) -> doneButton.setDisable(newValue.trim().isEmpty()),
                width, filePath, height, tileHeight, tileWidth);
        
        final VBox vBox = new VBox();
        vBox.getChildren().addAll(largeValues, smallValues);
        
        dialog.getDialogPane().setContent(vBox);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                builder.height(height.getNumber());
                builder.width(width.getNumber());
                builder.tileHeight(tileHeight.getNumber());
                builder.tileWidth(tileWidth.getNumber());
                return builder.build();
            }
            return null;
        });
        
        Optional<CreateBaseTilemap> createDialog = dialog.showAndWait();
        
        return createDialog.orElse(null);
    }
    
}