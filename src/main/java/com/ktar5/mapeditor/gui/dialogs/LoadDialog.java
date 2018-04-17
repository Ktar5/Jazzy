package com.ktar5.mapeditor.gui.dialogs;

import javafx.stage.FileChooser;

import java.io.File;

public class LoadDialog {
    
    public static File create(String title, String extensionDescription, String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionDescription, extension));
        return fileChooser.showOpenDialog(null);
    }
    
}
