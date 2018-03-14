package com.ktar5.mapeditor.tilemap;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.alerts.GenericAlert;
import com.ktar5.mapeditor.tilemap.dialogs.CreateDialog;
import com.ktar5.mapeditor.tilemap.dialogs.LoadDialog;
import com.ktar5.mapeditor.util.StringUtil;
import lombok.Getter;
import org.json.JSONObject;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MapManager {
    public static MapManager instance;
    private File tempDir;
    private HashMap<UUID, Tilemap> openMaps;
    @Getter
    private UUID currentLevel = null;

    public MapManager(File dir) {
        instance = this;

        openMaps = new HashMap<>();

        //Initialize tinylog
        Configurator.defaultConfig()
                .writer(new ConsoleWriter())
                .level(Level.DEBUG)
                .addWriter(new org.pmw.tinylog.writers.FileWriter("log.txt"))
                .formatPattern("{date:mm:ss:SSS} {class_name}.{method}() [{level}]: {message}")
                .activate();

        //Create the save and temp directories
        this.tempDir = dir;
        if (!tempDir.exists() || !tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        //Save maps every 5 minutes
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                saveTempAllMaps();
            }
        }, 5 * 60 * 1000, 5 * 60 * 1000);
    }

    public static MapManager get() {
        if (instance == null) {
            throw new RuntimeException("Please initialize tile manager first.");
        }
        return instance;
    }

    public void remove(UUID uuid) {
        if (this.openMaps.containsKey(uuid)) {
            Logger.debug("Removed tilemap: " + getMap(uuid).getMapName());
            openMaps.remove(uuid);
        }
    }

    public Tilemap getMap(UUID id) {
        if (!openMaps.containsKey(id)) {
            throw new RuntimeException("Tilemap with id: " + id + " already exists");
        }
        return openMaps.get(id);
    }

    public Tilemap createMap() {
        CreateDialog createDialog = CreateDialog.create();
        if (createDialog == null) {
            new GenericAlert("Something went wrong during the process of creating the map, please try again.");
            return null;
        }

        File file = createDialog.getFile();
        for (Tilemap tilemap1 : openMaps.values()) {
            if (tilemap1.getSaveFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                new GenericAlert("Tilemap with path " + file.getAbsolutePath() + " already loaded.\n" +
                        "Please close tab for " + file.getName() + " then try creating new map again.");
                return null;
            }
        }

        Tilemap tilemap = Tilemap.createEmpty(createDialog.getWidth(),
                createDialog.getHeight(), createDialog.getTilesize(), createDialog.getFile());
        openMaps.put(tilemap.getId(), tilemap);
        Main.root.getCenterView().getEditorViewPane().createTab(tilemap.getId());
        return tilemap;
    }

    public Tilemap loadMap() {
        File loaderFile = LoadDialog.create();
        if (loaderFile == null) {
            Logger.info("Tried to load map, cancelled or failed");
            return null;
        } else if (!loaderFile.exists()) {
            new GenericAlert("The selected file: " + loaderFile.getPath() + " does not exist. Try again.");
            return null;
        }

        Logger.info("Beginning to load map from file: " + loaderFile.getPath());

        String data = StringUtil.readFileAsString(loaderFile);
        if (data == null || data.isEmpty()) {
            return null;
        }
        Tilemap tilemap = TilemapDeserializer.deserialize(loaderFile, new JSONObject(data));
        for (Tilemap temp : openMaps.values()) {
            if (temp.getSaveFile().getPath().equals(tilemap.getSaveFile().getPath())) {
                new GenericAlert("Tilemap with path " + tilemap.getSaveFile().getAbsolutePath() + " already loaded");
                return null;
            }
        }
        openMaps.put(tilemap.getId(), tilemap);
        Main.root.getCenterView().getEditorViewPane().createTab(tilemap.getId());
        Logger.info("Finished loading map: " + tilemap.getMapName());
        return tilemap;
    }

    public void saveMap(UUID id) {
        Logger.info("Starting save for tilemap (" + id + ")");

        if (!openMaps.containsKey(id)) {
            Logger.info("Map not loaded so could not be saved id: (" + id + ")");
            return;
        }

        Tilemap tilemap = openMaps.get(id);
        if (tilemap.getSaveFile().exists()) {
            tilemap.getSaveFile().delete();
        }

        try {
            tilemap.getSaveFile().createNewFile();
            FileWriter writer = new FileWriter(tilemap.getSaveFile());
            writer.write(TilemapSerializer.serialize(tilemap).toString(4));
            Main.root.getCenterView().getEditorViewPane().setChanges(tilemap.getId(), false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Logger.info("Finished save for tilemap (" + id + ") in " + "\"" + tilemap.getSaveFile() + "\"");
    }

    public void saveTempAllMaps() {
        Logger.info("Saving map backups");
        for (HashMap.Entry<UUID, Tilemap> openMap : openMaps.entrySet()) {
            //TODO
            //saveMap(tempDir, openMap.getKey());
        }
        Logger.info("Finished saving map backups");
    }

    public Tilemap getCurrent() {
        return this.getMap(getCurrentLevel());
    }

}
