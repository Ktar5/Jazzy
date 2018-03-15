package com.ktar5.mapeditor.tileset;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.dialogs.GenericAlert;
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
            throw new RuntimeException("BaseTileset with id: " + id + " already exists");
        }
        return tilesetHashMap.get(id);
    }

    public BaseTileset createTileset() {
        /*CreateBaseTilemap createDialog = CreateBaseTilemap.create();
        if (createDialog == null) {
            new GenericAlert("Something went wrong during the process of creating the map, please try again.");
            return null;
        }

        File file = createDialog.getFile();
        for (BaseTileset tileset1 : openMaps.values()) {
            if (tileset1.getSaveFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                new GenericAlert("BaseTileset with path " + file.getAbsolutePath() + " already loaded.\n" +
                        "Please close tab for " + file.getName() + " then try creating new tileset again.");
                return null;
            }
        }

        BaseTileset tileset = BaseTileset.createEmpty(createDialog.getWidth(),
                createDialog.getHeight(), createDialog.getTilesize(), createDialog.getFile());
        openMaps.put(tileset.getId(), tileset);
        Main.root.getCenterView().getEditorViewPane().createTab(tileset.getId());

        return tileset;
        */
        return null;
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
        BaseTileset baseTileset = TilesetDeserializer.deserialize(loaderFile, new JSONObject(data));
        for (BaseTileset temp : tilesetHashMap.values()) {
            if (temp.getTilesetFile().getPath().equals(baseTileset.getTilesetFile().getPath())) {
                new GenericAlert("BaseTileset with path " + baseTileset.getTilesetFile().getAbsolutePath() + " already loaded");
                return null;
            }
        }
        tilesetHashMap.put(baseTileset.getId(), baseTileset);
        Main.root.getCenterView().getEditorViewPane().createTab(baseTileset.getId());
        Logger.info("Finished loading map: " + baseTileset.getTilesetFile().getName());
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
            Main.root.getCenterView().getEditorViewPane().setChanges(baseTileset.getId(), false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Logger.info("Finished save for baseTileset (" + id + ") in " + "\"" + baseTileset.getTilesetFile() + "\"");
    }

}
