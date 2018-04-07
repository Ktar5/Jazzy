package com.ktar5.mapeditor.tileset;

import com.ktar5.mapeditor.util.Tabbable;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.imgscalr.Scalr;
import org.json.JSONObject;
import org.mini2Dx.gdx.utils.IntMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
public abstract class BaseTileset implements Tabbable {
    public static final int SCALE = 1;
    private UUID id;
    private IntMap<Image> tileImages;
    private Image originalImage;
    private File sourceFile, saveFile;
    private int tileWidth, tileHeight;
    private int paddingVertical, paddingHorizontal;
    private int offsetLeft, offsetUp;
    private int dimensionX, dimensionY, columns, rows;
    @Setter
    private boolean dragging;

    public BaseTileset(File saveFile, JSONObject json) {
        this(Paths.get(saveFile.getPath()).resolve(json.getString("sourceFile")).toFile(),
                saveFile,
                json.getJSONObject("padding").getInt("vertical"),
                json.getJSONObject("padding").getInt("horizontal"),
                json.getJSONObject("offset").getInt("left"),
                json.getJSONObject("offset").getInt("up"),
                json.getInt("tileWidth"),
                json.getInt("tileHeight"));
    }

    public BaseTileset(File sourceFile, File saveFile, int paddingVertical, int paddingHorizontal,
                       int offsetLeft, int offsetUp, int tileWidth, int tileHeight) {
        this.sourceFile = sourceFile;
        this.saveFile = saveFile;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.offsetLeft = offsetLeft;
        this.offsetUp = offsetUp;
        this.paddingHorizontal = paddingHorizontal;
        this.paddingVertical = paddingVertical;
        tileImages = new IntMap<>();
        this.id = UUID.randomUUID();
        try {
            final BufferedImage readImage = ImageIO.read(sourceFile);
            columns = (readImage.getWidth() - getOffsetLeft()) / (getTileWidth() + getPaddingHorizontal());
            rows = (readImage.getHeight() - getOffsetUp()) / (getTileHeight() + getPaddingVertical());
            originalImage = SwingFXUtils.toFXImage(readImage, null);
            this.dimensionX = columns * getTileWidth();
            this.dimensionY = rows * getTileHeight();
            getTilesetImages(readImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.tileHeight *= SCALE;
        this.tileWidth *= SCALE;
        this.offsetLeft = offsetLeft * SCALE;
        this.offsetUp = offsetUp * SCALE;
        this.paddingHorizontal = paddingHorizontal * SCALE;
        this.paddingVertical = paddingVertical * SCALE;
    }

    public static BufferedImage scale(BufferedImage sbi, int factor) {
        return Scalr.resize(sbi, Scalr.Method.SPEED, sbi.getWidth() * factor);
    }

    public abstract void getTilesetImages(BufferedImage image);

    @Override
    @CallSuper
    public JSONObject serialize() {
        JSONObject json = new JSONObject();

        JSONObject padding = new JSONObject();
        padding.put("horizontal", this.getPaddingHorizontal());
        padding.put("vertical", this.getPaddingVertical());
        json.put("padding", padding);

        JSONObject offset = new JSONObject();
        offset.put("left", this.getOffsetLeft());
        offset.put("up", this.getOffsetUp());
        json.put("offset", offset);

        json.put("tileWidth", tileWidth);
        json.put("tileHeight", tileHeight);

        Path path = Paths.get(this.getSaveFile().getPath())
                .relativize(Paths.get(this.getSourceFile().getPath()));
        json.put("sourceFile", path.toString());
        return json;
    }

    @Override
    public Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> getDimensions() {
        return new Pair<>(new Pair<>(dimensionX, dimensionY), new Pair<>(getTileWidth(), getTileHeight()));
    }

    @Override
    public void remove() {
        TilesetManager.get().remove(getId());
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void save() {
        TilesetManager.get().saveTileset(getId());
    }

    @Override
    public String getName() {
        return getSaveFile().getName();
    }

    @Override
    public File getSaveFile() {
        return saveFile;
    }

    @Override
    public void updateSaveFile(File file) {
        this.saveFile = file;
    }
}
