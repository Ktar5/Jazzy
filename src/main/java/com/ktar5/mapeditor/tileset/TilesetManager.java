package com.ktar5.mapeditor.tileset;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.centerview.editor.tabs.TilemapTab;
import com.ktar5.mapeditor.gui.centerview.editor.tabs.TilesetTab;
import com.ktar5.mapeditor.gui.dialogs.CreateWholeTileset;
import com.ktar5.mapeditor.gui.dialogs.GenericAlert;
import com.ktar5.mapeditor.tilemaps.whole.WholeTileset;
import com.ktar5.mapeditor.util.StringUtil;
import javafx.stage.FileChooser;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class TilesetManager {
    private static TilesetManager instance;

    private HashMap<UUID, BaseTileset> tilesetHashMap;

    public static TilesetManager get() {
        if (instance == null) {
            instance = new TilesetManager();
        }
        return instance;
    }

    public TilesetManager() {
        this.tilesetHashMap = new HashMap<>();
    }

    public void remove(UUID uuid) {
        if (this.tilesetHashMap.containsKey(uuid)) {
            Logger.debug("Removed tileset: " + getTileset(uuid).getTilesetFile().getName());
            tilesetHashMap.remove(uuid);
        }
    }

    public BaseTileset getTileset(UUID id) {
        if (!tilesetHashMap.containsKey(id)) {
            throw new RuntimeException("BaseTileset with id: " + id + " doesn't exist");
        }
        return tilesetHashMap.get(id);
    }

    public WholeTileset createTileset() {
        CreateWholeTileset createDialog = CreateWholeTileset.create();
        if (createDialog == null) {
            new GenericAlert("Something went wrong during the process of creating the tileset, please try again.");
            return null;
        }

        File tilesetFile = createDialog.getTilesetFile();
        for (BaseTileset tileset1 : tilesetHashMap.values()) {
            if (tileset1.getTilesetFile().getPath().equals(tilesetFile.getPath())) {
                new GenericAlert("Tileset with path " + tilesetFile.getAbsolutePath() + " already loaded.\n" +
                        "Please close tab for " + tilesetFile.getName() + " then try creating new tileset again.");
                return null;
            }
        }

        WholeTileset tileset = new WholeTileset(createDialog.getSourceFile(),
                createDialog.getTilesetFile(), createDialog.getTileSize(),
                createDialog.getPaddingVertical(), createDialog.getPaddingHorizontal(),
                createDialog.getOffsetLeft(), createDialog.getOffsetUp());
        tilesetHashMap.put(tileset.getId(), tileset);
        Main.root.getCenterView().getEditorViewPane().addTab(new TilesetTab(tileset.getId()));
        return tileset;
    }

    public BaseTileset loadTileset() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));
        File loaderFile = fileChooser.showOpenDialog(null);
        if (loaderFile == null) {
            Logger.info("Tried to load baseTileset, cancelled or failed");
            return null;
        } else if (!loaderFile.exists()) {
            new GenericAlert("The selected file: " + loaderFile.getPath() + " does not exist. Try again.");
            return null;
        }

        Logger.info("Beginning to load baseTileset from file: " + loaderFile.getPath());

        String data = StringUtil.readFileAsString(loaderFile);
        if (data == null || data.isEmpty()) {
            return null;
        }
        BaseTileset baseTileset = new WholeTileset(loaderFile, new JSONObject(data));
        for (BaseTileset temp : tilesetHashMap.values()) {
            if (temp.getTilesetFile().getPath().equals(baseTileset.getTilesetFile().getPath())) {
                new GenericAlert("BaseTileset with path " + baseTileset.getTilesetFile().getAbsolutePath() + " already loaded");
                return null;
            }
        }
        tilesetHashMap.put(baseTileset.getId(), baseTileset);
        Main.root.getCenterView().getEditorViewPane().addTab(new TilesetTab(baseTileset.getId()));
        Logger.info("Finished loading tileset: " + baseTileset.getTilesetFile().getName());
        return baseTileset;
    }

    public void saveTileset(UUID id) {
        Logger.info("Starting save for baseTileset (" + id + ")");

        if (!tilesetHashMap.containsKey(id)) {
            Logger.info("BaseTileset not loaded so could not be saved id: (" + id + ")");
            return;
        }

        BaseTileset baseTileset = tilesetHashMap.get(id);
        if (baseTileset.getTilesetFile().exists()) {
            baseTileset.getTilesetFile().delete();
        }

        try {
            baseTileset.getTilesetFile().createNewFile();
            FileWriter writer = new FileWriter(baseTileset.getTilesetFile());
            writer.write(baseTileset.serialize().toString(4));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Main.root.getCenterView().getEditorViewPane().setChanges(baseTileset.getId(), false);
        Logger.info("Finished save for baseTileset (" + id + ") in " + "\"" + baseTileset.getTilesetFile() + "\"");
    }

}
