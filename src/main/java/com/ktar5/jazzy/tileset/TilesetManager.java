package com.ktar5.jazzy.tileset;

import com.ktar5.jazzy.coordination.EditorCoordinator;
import com.ktar5.jazzy.gui.centerview.tabs.TilesetTab;
import com.ktar5.jazzy.gui.dialogs.CreateWholeTileset;
import com.ktar5.jazzy.gui.dialogs.GenericAlert;
import com.ktar5.jazzy.gui.dialogs.SelectType;
import com.ktar5.jazzy.tilemaps.sided.SidedTileset;
import com.ktar5.jazzy.tilemaps.whole.WholeTileset;
import com.ktar5.jazzy.util.StringUtil;
import javafx.stage.FileChooser;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class TilesetManager {
    private static TilesetManager instance;
    private ArrayList<Class<? extends BaseTileset>> registeredTilesetTypes;
    private HashMap<UUID, BaseTileset> tilesetHashMap;
    
    public TilesetManager() {
        this.tilesetHashMap = new HashMap<>();
        this.registeredTilesetTypes = new ArrayList<>();
        
        registerTilemapClass(WholeTileset.class);
        registerTilemapClass(SidedTileset.class);
    }
    
    /**
     * Gets the instance of the TilesetManager
     */
    public static TilesetManager get() {
        if (instance == null) {
            instance = new TilesetManager();
        }
        return instance;
    }
    
    /**
     * Registers a tileset class to be used in creation/loading dialogs.
     */
    public <T extends BaseTileset> void registerTilemapClass(Class<? extends T> clazz) {
        registeredTilesetTypes.add(clazz);
        Logger.debug("Registered tilemap class: " + clazz.getName());
    }
    
    /**
     * Removes the tileset with the given id
     */
    public void remove(UUID uuid) {
        if (this.tilesetHashMap.containsKey(uuid)) {
            Logger.debug("Removed tileset: " + getTileset(uuid).getSaveFile().getName());
            tilesetHashMap.remove(uuid);
        }
    }
    
    /**
     * Return the tileset (if it exists) with the given id
     */
    public BaseTileset getTileset(UUID id) {
        if (!tilesetHashMap.containsKey(id)) {
            throw new RuntimeException("BaseTileset with id: " + id + " doesn't exist");
        }
        return tilesetHashMap.get(id);
    }
    
    /**
     * Creates a tileset given the dialog options
     *
     * @return the BaseTileset representation of the tileset that was created in the dialog
     */
    public BaseTileset createTileset() {
        return createTileset(SelectType.getType(registeredTilesetTypes));
    }
    
    /**
     * Create a tileset of the type specified. Uses a create dialog.
     *
     * @param clazz The class to instantiate (ex: WholeTileset.class)
     * @param <T>   The type of tilemap to instantiate (ex: WholeTileset)
     * @return the tileset of type <T> that has been instantiated, otherwise null
     */
    public <T extends BaseTileset> T createTileset(Class<? extends T> clazz) {
        CreateWholeTileset createDialog = CreateWholeTileset.create();
        if (createDialog == null) {
            new GenericAlert("Something went wrong during the process of creating the tileset, please try again.");
            return null;
        }
        
        File tilesetFile = createDialog.getTilesetFile();
        for (BaseTileset tileset1 : tilesetHashMap.values()) {
            if (tileset1.getSaveFile().getPath().equals(tilesetFile.getPath())) {
                new GenericAlert("Tileset with path " + tilesetFile.getAbsolutePath() + " already loaded.\n" +
                        "Please close tab for " + tilesetFile.getName() + " then try creating new tileset again.");
                return null;
            }
        }
        
        T tileset;
        try {
            Constructor<? extends T> constructor = clazz.getConstructor(File.class, File.class,
                    Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            tileset = constructor.newInstance(createDialog.getSourceFile(), createDialog.getTilesetFile(),
                    createDialog.getPaddingVertical(), createDialog.getPaddingHorizontal(),
                    createDialog.getOffsetLeft(), createDialog.getOffsetUp(),
                    createDialog.getTileWidth(), createDialog.getTileHeight());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
        
        tilesetHashMap.put(tileset.getId(), tileset);
        TilesetTab tab;
        EditorCoordinator.get().getEditor().addTab(tab = new TilesetTab(tileset.getId()));
        tab.draw();
        return tileset;
    }
    
    /**
     * Loads a tileset given the dialog options
     *
     * @return the BaseTileset representation of the tileset that was initialized in the dialog
     */
    public BaseTileset loadTileset() {
        return loadTileset(SelectType.getType(registeredTilesetTypes));
    }
    
    /**
     * Load a tileset of the type specified from the file specified.
     *
     * @param clazz The class to instantiate (ex: WholeTileset.class)
     * @param <T>   The type of tilemap to instantiate (ex: WholeTileset)
     * @return the tileset of type <T> that has been instantiated, otherwise null
     */
    public <T extends BaseTileset> T loadTileset(File loaderFile, Class<? extends T> clazz) {
        Logger.info("Beginning to load tileset from file: " + loaderFile.getPath());
        
        String data = StringUtil.readFileAsString(loaderFile);
        if (data == null || data.isEmpty()) {
            return null;
        }
        
        T tileset;
        try {
            Constructor<? extends T> constructor = clazz.getConstructor(File.class, JSONObject.class);
            tileset = constructor.newInstance(loaderFile, new JSONObject(data));
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
        
        for (BaseTileset temp : tilesetHashMap.values()) {
            if (temp.getSaveFile().getPath().equals(tileset.getSaveFile().getPath())) {
                new GenericAlert("Tileset with path " + tileset.getSaveFile().getAbsolutePath() + " already loaded");
                return clazz.isInstance(temp) ? (T) temp : null;
            }
        }
        
        tilesetHashMap.put(tileset.getId(), tileset);
        TilesetTab tilesetTab = new TilesetTab(tileset.getId());
        EditorCoordinator.get().getEditor().addTab(tilesetTab);
        tilesetTab.draw();
        Logger.info("Finished loading tileset: " + tileset.getSaveFile().getName());
        return tileset;
    }
    
    /**
     * Loads a tileset from a file selected in an "open file" dialog, and instantiates it using
     * the serialization constructor of tileset.
     *
     * @param clazz The class to instantiate (ex: WholeTileset.class)
     * @param <T>   The type of tileset to instantiate (ex: WholeTileset)
     * @return the tileset of type <T> that has been instantiated, otherwise null
     */
    public <T extends BaseTileset> T loadTileset(Class<? extends T> clazz) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Tileset");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));
        File loaderFile = fileChooser.showOpenDialog(null);
        if (loaderFile == null) {
            Logger.info("Tried to load tileset, cancelled or failed");
            return null;
        } else if (!loaderFile.exists()) {
            new GenericAlert("The selected file: " + loaderFile.getPath() + " does not exist. Try again.");
            return null;
        }
        
        return loadTileset(loaderFile, clazz);
    }
    
    /**
     * Save a tileset with the UUID specified.
     *
     * @param id the uuid of the tileset to be saved.
     */
    public void saveTileset(UUID id) {
        Logger.info("Starting save for baseTileset (" + id + ")");
        
        if (!tilesetHashMap.containsKey(id)) {
            Logger.info("BaseTileset not loaded so could not be saved id: (" + id + ")");
            return;
        }
        
        BaseTileset baseTileset = tilesetHashMap.get(id);
        
        if (baseTileset.getSaveFile().exists()) {
            baseTileset.getSaveFile().delete();
        }
        
        try {
            baseTileset.getSaveFile().createNewFile();
            FileWriter writer = new FileWriter(baseTileset.getSaveFile());
            writer.write(baseTileset.serialize().toString(4));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("An error occured during save");
            return;
        }
        
        EditorCoordinator.get().getEditor().setChanges(baseTileset.getId(), false);
        Logger.info("Finished save for baseTileset (" + id + ") in " + "\"" + baseTileset.getSaveFile() + "\"");
    }
    
    
}
