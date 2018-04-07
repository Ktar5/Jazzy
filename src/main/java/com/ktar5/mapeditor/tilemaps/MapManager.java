package com.ktar5.mapeditor.tilemaps;

import com.ktar5.mapeditor.coordination.EditorCoordinator;
import com.ktar5.mapeditor.gui.centerview.tabs.TilemapTab;
import com.ktar5.mapeditor.gui.dialogs.CreateBaseTilemap;
import com.ktar5.mapeditor.gui.dialogs.GenericAlert;
import com.ktar5.mapeditor.gui.dialogs.LoadDialog;
import com.ktar5.mapeditor.gui.dialogs.WholeOrComposite;
import com.ktar5.mapeditor.tilemaps.composite.CompositeTilemap;
import com.ktar5.mapeditor.tilemaps.whole.WholeTilemap;
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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class MapManager {
    public static MapManager instance;
    private HashMap<UUID, BaseTilemap> openMaps;
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
    }

    public static MapManager get() {
        if (instance == null) {
            throw new RuntimeException("Please initialize tile manager first.");
        }
        return instance;
    }

    public void remove(UUID uuid) {
        if (this.openMaps.containsKey(uuid)) {
            Logger.debug("Removed tilemap: " + getMap(uuid).getName());
            openMaps.remove(uuid);
        }
    }

    public BaseTilemap getMap(UUID id) {
        if (!openMaps.containsKey(id)) {
            throw new RuntimeException("Tilemap with id: " + id + " doesn't exist");
        }
        return openMaps.get(id);
    }

    public BaseTilemap createMap() {
        return createMap(WholeOrComposite.getType(WholeTilemap.class, CompositeTilemap.class));
    }

    public <T extends BaseTilemap> T createMap(Class<? extends T> clazz) {
        CreateBaseTilemap createDialog = CreateBaseTilemap.create();
        if (createDialog == null) {
            new GenericAlert("Something went wrong during the process of creating the map, please try again.");
            return null;
        }

        File file = createDialog.getFile();
        for (BaseTilemap baseTilemap1 : openMaps.values()) {
            if (baseTilemap1.getSaveFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                new GenericAlert("Tilemap with path " + file.getAbsolutePath() + " already loaded.\n" +
                        "Please close tab for " + file.getName() + " then try creating new map again.");
                return null;
            }
        }

        T tilemap;
        try {
            Constructor<? extends T> constructor = clazz.getConstructor(File.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            tilemap = constructor.newInstance(createDialog.getFile(), createDialog.getWidth(),
                    createDialog.getHeight(), createDialog.getTileWidth(), createDialog.getTileHeight());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        openMaps.put(tilemap.getId(), tilemap);
        EditorCoordinator.get().getEditor().addTab(new TilemapTab.WholeTilemapTab(tilemap.getId()));
        return tilemap;
    }

    public BaseTilemap loadMap() {
        return loadMap(WholeOrComposite.getType(WholeTilemap.class, CompositeTilemap.class));
    }

    public <T extends BaseTilemap> T loadMap(Class<? extends T> clazz) {
        File loaderFile = LoadDialog.create("Load a tilemap", "Json Tilemap File", "*.json");
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
            Logger.error("Data from file: " + loaderFile.getPath() + " is either null or empty.");
            return null;
        }

        T tilemap;
        try {
            Constructor<? extends T> constructor = clazz.getConstructor(File.class, JSONObject.class);
            tilemap = constructor.newInstance(loaderFile, new JSONObject(data));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        for (BaseTilemap temp : openMaps.values()) {
            if (temp.getSaveFile().getPath().equals(tilemap.getSaveFile().getPath())) {
                new GenericAlert("Tilemap with path " + tilemap.getSaveFile().getAbsolutePath() + " already loaded");
                return clazz.isInstance(temp) ? (T) temp : null;
            }
        }
        openMaps.put(tilemap.getId(), tilemap);
        EditorCoordinator.get().getEditor().addTab(new TilemapTab.WholeTilemapTab(tilemap.getId()));
        tilemap.draw(EditorCoordinator.get().getEditor().getTabDrawingPane(tilemap.getId()));

        Logger.info("Finished loading map: " + tilemap.getName());
        return tilemap;
    }

    public void saveMap(UUID id) {
        Logger.info("Starting save for baseTilemap (" + id + ")");

        if (!openMaps.containsKey(id)) {
            Logger.info("Map not loaded so could not be saved id: (" + id + ")");
            return;
        }

        BaseTilemap baseTilemap = openMaps.get(id);
        if (baseTilemap.getSaveFile().exists()) {
            baseTilemap.getSaveFile().delete();
        }

        try {
            baseTilemap.getSaveFile().createNewFile();
            FileWriter writer = new FileWriter(baseTilemap.getSaveFile());
            writer.write(baseTilemap.serialize().toString(4));
            EditorCoordinator.get().getEditor().setChanges(baseTilemap.getId(), false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Logger.info("Finished save for baseTilemap (" + id + ") in " + "\"" + baseTilemap.getSaveFile() + "\"");
    }

    public BaseTilemap getCurrent() {
        return this.getMap(getCurrentLevel());
    }

}
