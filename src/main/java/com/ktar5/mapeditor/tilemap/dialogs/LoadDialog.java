package com.ktar5.mapeditor.tilemap.dialogs;

import javafx.stage.FileChooser;

import java.io.File;

public class LoadDialog {

    public static File create() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));
        return fileChooser.showOpenDialog(null);
    }

}
