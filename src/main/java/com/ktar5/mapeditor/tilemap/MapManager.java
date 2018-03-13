package com.ktar5.mapeditor.tilemap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.alerts.GenericAlert;
import com.ktar5.mapeditor.tilemap.dialogs.CreateDialog;
import com.ktar5.mapeditor.tilemap.dialogs.LoadDialog;
import lombok.Getter;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;

import java.io.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MapManager {
    public static MapManager instance;
    private final Gson gson;
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

        //Initialize gson
        gson = new GsonBuilder()
                .registerTypeAdapter(Tilemap.class, new TilemapSerializer())
                .registerTypeAdapter(Tilemap.class, new TilemapDeserializer())
                .setPrettyPrinting()
                .create();

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

        UUID id = UUID.randomUUID();
        Tilemap tilemap = Tilemap.createEmpty(createDialog.getWidth(),
                createDialog.getHeight(), createDialog.getTilesize(), createDialog.getFile());
        openMaps.put(id, tilemap);
        Main.root.getCenterView().getEditorViewPane().createTab(id);
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

        //Attempt to initialize the file reader
        try (FileReader reader = new FileReader(loaderFile)) {
            //Load the tilemap from gson
            Tilemap tilemap = gson.fromJson(reader, Tilemap.class);
            tilemap.updateNameAndFile(loaderFile);
            for (Tilemap tilemap1 : openMaps.values()) {
                if (tilemap1.getSaveFile().getAbsolutePath().equals(tilemap.getSaveFile().getAbsolutePath())) {
                    new GenericAlert("Tilemap with path " + tilemap.getSaveFile().getAbsolutePath() + " already loaded");
                    return null;
                }
            }
            openMaps.put(tilemap.getId(), tilemap);
            Main.root.getCenterView().getEditorViewPane().createTab(tilemap.getId());
            Logger.info("Finished loading map: " + tilemap.getMapName());
            return tilemap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            gson.toJson(tilemap, writer);
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
