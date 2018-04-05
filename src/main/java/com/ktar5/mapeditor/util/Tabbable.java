package com.ktar5.mapeditor.util;

import com.ktar5.mapeditor.coordination.EditorCoordinator;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import com.ktar5.utilities.annotation.dontoverride.DontOverride;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.util.UUID;

public interface Tabbable extends Interactable {

    public void save();

    @DontOverride
    public default void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Tileset As..");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));
        File saveFile = fileChooser.showSaveDialog(null);
        if (saveFile == null) {
            Logger.info("Tried to save tileset, cancelled or failed");
            return;
        }
        updateSaveFile(saveFile);
        save();
    }

    @DontOverride
    public default void setChanged(boolean value) {
        EditorCoordinator.get().getEditor().getTab(getId()).setEdit(value);
    }

    public void setDragging(boolean dragging);

    public boolean isDragging();

    public UUID getId();

    public Pair<Integer, Integer> getDimensions();

    public String getName();

    public File getSaveFile();

    public void remove();

    public void updateSaveFile(File file);

    public void draw(Pane pane);

    @CallSuper
    public JSONObject serialize();

}
