package com.ktar5.jazzy.editor.gui.dialogs;

import com.ktar5.jazzy.editor.gui.utils.GuiUtils;
import com.ktar5.jazzy.editor.gui.utils.NumberTextField;
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

import static com.ktar5.jazzy.editor.gui.utils.GuiUtils.addListener;

@Builder
@Getter
public class CreateWholeTileset {
    private File sourceFile, tilesetFile;
    private int tileWidth, tileHeight, paddingVertical, paddingHorizontal, offsetLeft, offsetUp;
    
    public static CreateWholeTileset create() {
        CreateWholeTilesetBuilder builder = new CreateWholeTilesetBuilder();
        
        // Create the custom dialog.
        Dialog<CreateWholeTileset> dialog = new Dialog<>();
        dialog.setTitle("Create New BaseTilemap");
        
        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Select Tileset Image");
        imageChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*.png"));
        
        FileChooser tilesetChooser = new FileChooser();
        tilesetChooser.setTitle("Create Tileset File");
        tilesetChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));
        
        TextField imagePath = new TextField();
        imagePath.setMaxWidth(240);
        imagePath.setPrefWidth(imagePath.getMaxWidth());
        imagePath.setEditable(false);
        
        TextField tilesetPath = new TextField();
        imagePath.setMaxWidth(240);
        imagePath.setPrefWidth(imagePath.getMaxWidth());
        tilesetPath.setEditable(false);
        
        NumberTextField tileHeightField = new NumberTextField("Tile Height", 16);
        NumberTextField tileWidthField = new NumberTextField("Tile Width", 16);
        NumberTextField paddingVertField = new NumberTextField("Vertical Padding", 0);
        NumberTextField paddingHorzField = new NumberTextField("Horizontal Padding", 0);
        NumberTextField offsetLeftField = new NumberTextField("Left Offset", 0);
        NumberTextField offsetUpField = new NumberTextField("Up Offset", 0);
        
        GuiUtils.forAll(field -> field.setPrefWidth(45),
                tileWidthField, tileHeightField, paddingVertField, paddingHorzField, offsetLeftField, offsetUpField);
        
        Button selectImageFileButton = new Button("Select Image File");
        selectImageFileButton.setOnAction(event -> {
            selectImageFileButton.setDisable(true);
            File file = imageChooser.showOpenDialog(null);
            selectImageFileButton.setDisable(false);
            if (file != null) {
                imagePath.setText(file.getAbsolutePath());
                builder.sourceFile(file);
            }
        });
        
        Button selectTilesetFileButton = new Button("Create Tileset File");
        selectTilesetFileButton.setOnAction(event -> {
            selectTilesetFileButton.setDisable(true);
            File file = tilesetChooser.showSaveDialog(null);
            selectTilesetFileButton.setDisable(false);
            if (file != null) {
                tilesetPath.setText(file.getAbsolutePath());
                builder.tilesetFile(file);
            }
        });
        
        GridPane largeValues = new GridPane();
        largeValues.setHgap(10);
        largeValues.setVgap(10);
        largeValues.setPadding(new Insets(20, 150, 10, 10));
        
        largeValues.add(new Label("Image Path:"), 0, 0);
        largeValues.add(imagePath, 1, 0);
        largeValues.add(selectImageFileButton, 4, 0);
        
        largeValues.add(new Label("File Path:"), 0, 1);
        largeValues.add(tilesetPath, 1, 1);
        largeValues.add(selectTilesetFileButton, 4, 1);
        
        
        GridPane smallValues = new GridPane();
        smallValues.setHgap(10);
        smallValues.setVgap(10);
        smallValues.setPadding(new Insets(20, 10, 10, 10));
        
        smallValues.add(new Label("Horiz Pad:"), 0, 0);
        smallValues.add(paddingHorzField, 1, 0);
        
        smallValues.add(new Label("Offset Up:"), 2, 0);
        smallValues.add(offsetUpField, 3, 0);
        
        smallValues.add(new Label("Vert Pad:"), 0, 1);
        smallValues.add(paddingVertField, 1, 1);
        
        smallValues.add(new Label("Offset Left:"), 2, 1);
        smallValues.add(offsetLeftField, 3, 1);
        
        smallValues.add(new Label("Tile Width:"), 4, 0);
        smallValues.add(tileWidthField, 5, 0);
        
        smallValues.add(new Label("Tile Height:"), 4, 1);
        smallValues.add(tileHeightField, 5, 1);
        
        
        // Enable/Disable login button depending on whether a username was entered.
        Node doneButton = dialog.getDialogPane().lookupButton(loginButtonType);
        doneButton.setDisable(true);
        
        // Do some validation (using the Java 8 lambda syntax).
        addListener((observable, oldValue, newValue) -> doneButton.setDisable(newValue.trim().isEmpty()),
                offsetLeftField, offsetUpField, paddingHorzField, paddingVertField,
                tileWidthField, tileHeightField, imagePath, tilesetPath);
        
        final VBox vBox = new VBox();
        vBox.getChildren().addAll(largeValues, smallValues);
        
        dialog.getDialogPane().setContent(vBox);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                builder.tileWidth(tileWidthField.getNumber());
                builder.tileHeight(tileHeightField.getNumber());
                builder.paddingVertical(paddingVertField.getNumber());
                builder.paddingHorizontal(paddingHorzField.getNumber());
                builder.offsetLeft(offsetLeftField.getNumber());
                builder.offsetUp(offsetUpField.getNumber());
                return builder.build();
            }
            return null;
        });
        
        Optional<CreateWholeTileset> createDialog = dialog.showAndWait();
        
        return createDialog.orElse(null);
    }
    
}
