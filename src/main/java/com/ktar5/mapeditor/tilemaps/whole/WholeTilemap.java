package com.ktar5.mapeditor.tilemaps.whole;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.PixelatedImageView;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tileset.BaseTileset;
import com.ktar5.mapeditor.tileset.TilesetManager;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import lombok.Getter;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class WholeTilemap extends BaseTilemap<BaseTileset> {

    public WholeTilemap(File saveFile, JSONObject json) {
        super(saveFile, json);
    }

    public WholeTilemap(File saveFile, int width, int height, int tileSize, boolean empty) {
        super(saveFile, width, height, tileSize, empty);
    }

    public WholeTilemap(File saveFile, int width, int height, int tileSize) {
        super(saveFile, width, height, tileSize);
    }

    @Override
    public void loadTilesetIfExists(JSONObject json) {
        if (json.has("tileset")) {
            File tileset = Paths.get(getSaveFile().getPath()).resolve(json.getString("tileset")).toFile();
            BaseTileset baseTileset = TilesetManager.get().loadTileset(tileset);
            this.setTileset(baseTileset);
        }
    }

    @Override
    public void deserializeBlock(String block, int x, int y) {
        if (block.contains("_")) {
            String[] split = block.split("_");
            this.grid[x][y] = new WholeTile(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        } else {
            this.grid[x][y] = new WholeTile(Integer.valueOf(block), 0);
        }
    }

    @Override
    public void setEmpty() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                grid[x][y] = WholeTile.AIR;
            }
        }
    }

    @Override
    public void onClick(MouseEvent event) {
        if (!event.getButton().equals(MouseButton.PRIMARY)) {
            return;
        }
        Node node = event.getPickResult().getIntersectedNode();
        if (node == null) {
            int x = (int) (event.getX() / this.getTileSize());
            int y = (int) (event.getY() / this.getTileSize());
            PixelatedImageView iv = new WholeTileset.ImageTestView((WholeTileset) getTileset(), 0);
            iv.setVisible(true);
            iv.setTranslateX(x * this.getTileSize());
            iv.setTranslateY(y * this.getTileSize());
        } else {
            ((WholeTileset.ImageTestView) node).incrementImage();
        }
    }

    @Override
    public void draw() {
        Pane pane = Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId());
        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x <= getWidth() - 1; x++) {
                int blockId = ((WholeTile) grid[x][y]).getBlockId();
                if (blockId == 0) {
                    continue;
                }
                PixelatedImageView iv = new WholeTileset.ImageTestView(((WholeTileset) this.getTileset()), blockId);
                iv.setVisible(true);
                iv.setTranslateX(x * getTileSize());
                iv.setTranslateY(y * getTileSize());
                pane.getChildren().add(iv);
            }
        }
    }

    @Override
    @CallSuper
    public JSONObject serialize() {
        final JSONObject json = super.serialize();
        if (this.getTileset() != null) {
            Path path = Paths.get(this.getSaveFile().getPath())
                    .relativize(Paths.get(this.getTileset().getSaveFile().getPath()));
            json.put("tileset", path.toString());
        }
        return json;
    }

    public void set(int x, int y, WholeTile tile) {
        this.grid[x][y] = tile;
        setChanged(true);
    }

    public void remove(int x, int y) {
        this.grid[x][y] = WholeTile.AIR;
        setChanged(true);
    }


}
