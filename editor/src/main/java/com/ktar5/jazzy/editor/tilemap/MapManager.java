package com.ktar5.jazzy.editor.tilemap;

import com.ktar5.jazzy.editor.coordination.EditorCoordinator;
import com.ktar5.jazzy.editor.gui.centerview.tabs.TilemapTab;
import com.ktar5.jazzy.editor.gui.dialogs.CreateBaseTilemap;
import com.ktar5.jazzy.editor.gui.dialogs.GenericAlert;
import com.ktar5.jazzy.editor.gui.dialogs.LoadDialog;
import com.ktar5.jazzy.editor.gui.dialogs.SelectType;
import com.ktar5.jazzy.editor.tilemap.whole.WholeTileLayer;
import com.ktar5.jazzy.editor.util.StringUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MapManager {
    private static MapManager instance;
    private HashMap<UUID, BaseTilemap> openMaps;
    private ArrayList<Class<? extends BaseTilemap>> registeredMapTypes;
    
    public MapManager() {
        instance = this;
        registeredMapTypes = new ArrayList<>();
        openMaps = new HashMap<>();
        
        //Initialize tinylog
        Configurator.defaultConfig()
                .writer(new ConsoleWriter())
                .level(Level.DEBUG)
                .addWriter(new org.pmw.tinylog.writers.FileWriter("log.txt"))
                .formatPattern("{date:mm:ss:SSS} {class_name}.{method}() [{level}]: {message}")
                .activate();
        
        registerTilemapClass(WholeTileLayer.class);
    }
    
    /**
     * Gets the instance of the MapManager
     */
    public static MapManager get() {
        if (instance == null) {
            throw new RuntimeException("Please initialize tile manager first.");
        }
        return instance;
    }
    
    /**
     * Registers a tilemap class to be used in creation/loading dialogs.
     */
    public <T extends BaseTilemap> void registerTilemapClass(Class<? extends T> clazz) {
        registeredMapTypes.add(clazz);
        Logger.debug("Registered tilemap class: " + clazz.getName());
    }
    
    /**
     * Removes the tilemap with the given id
     */
    public void remove(UUID uuid) {
        if (this.openMaps.containsKey(uuid)) {
            Logger.debug("Removed tilemap: " + getMap(uuid).getName());
            openMaps.remove(uuid);
        }
    }
    
    /**
     * Return the map (if it exists) with the id given
     */
    public BaseTilemap getMap(UUID id) {
        if (!openMaps.containsKey(id)) {
            throw new RuntimeException("Tilemap with id: " + id + " doesn't exist");
        }
        return openMaps.get(id);
    }
    
    /**
     * Creates a tilemap given the dialog options
     *
     * @return the BaseTilemap representation of the tilemap that was created in the dialog
     */
    public BaseTilemap createMap() {
        return createMap(SelectType.getType(registeredMapTypes));
    }
    
    /**
     * Create a tilemap of the type specified. Uses a create dialog.
     *
     * @param clazz The class to instantiate (ex: WholeTileLayer.class)
     * @param <T>   The type of tilemap to instantiate (ex: WholeTileLayer)
     * @return the tilemap of type <T> that has been instantiated, otherwise null
     */
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
    
    /**
     * Loads a tilemap given the dialog options
     *
     * @return the BaseTilemap representation of the tilemap that was initialized in the dialog
     */
    public BaseTilemap loadMap() {
        return loadMap(SelectType.getType(registeredMapTypes));
    }
    
    /**
     * Loads a tilemap from a file selected in an "open file" dialog, and instantiates it using
     * the serialization constructor of tilemap.
     *
     * @param clazz The class to instantiate (ex: WholeTileLayer.class)
     * @param <T>   The type of tilemap to instantiate (ex: WholeTileLayer)
     * @return the tilemap of type <T> that has been instantiated, otherwise null
     */
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
        EditorCoordinator.get().getEditor().addTab(tilemap.getNewTilemapTab());
        tilemap.draw(EditorCoordinator.get().getEditor().getTabDrawingPane(tilemap.getId()));
        
        Logger.info("Finished loading map: " + tilemap.getName());
        return tilemap;
    }
    
    /**
     * Save a map with the UUID specified.
     *
     * @param id the uuid of the map to be saved.
     */
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
    
}
