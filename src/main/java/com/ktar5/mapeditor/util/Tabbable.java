package com.ktar5.mapeditor.util;

import com.ktar5.mapeditor.Main;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import com.ktar5.utilities.annotation.dontoverride.DontOverride;
import javafx.stage.FileChooser;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.util.UUID;

public interface Tabbable {

    public void save();

    @DontOverride
    public default void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Tileset As..");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));
        File saveFile = fileChooser.showSaveDialog(null);
        if (saveFile == null) {
            Logger.info("Tried to save tileset, cancelled or failed");
        }
        updateSaveFile(saveFile);
        save();
    }

    @DontOverride
    public default void setChanged(boolean value) {
        Main.root.getCenterView().getEditorViewPane().getTab(getId()).setEdit(value);
    }

    public UUID getId();

    public String getName();

    public File getSaveFile();

    public void remove();

    public void updateSaveFile(File file);

    public void draw();

    @CallSuper
    public JSONObject serialize();

}