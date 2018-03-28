package com.ktar5.mapeditor.tilemaps.whole;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.PixelatedImageView;
import com.ktar5.mapeditor.gui.dialogs.GenericAlert;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tileset.TilesetManager;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class WholeTilemap extends BaseTilemap<WholeTileset> {

    public WholeTilemap(File saveFile, JSONObject json) {
        super(saveFile, json);
    }

    public WholeTilemap(File saveFile, int width, int height, int tileSize) {
        super(saveFile, width, height, tileSize);
    }

    @Override
    public void loadTilesetIfExists(JSONObject json) {
        if (json.has("tileset")) {
            File tileset = Paths.get(getSaveFile().getPath()).resolve(json.getString("tileset")).toFile();
            WholeTileset tileset1 = TilesetManager.get().loadTileset(tileset, WholeTileset.class);
            if (tileset1.getTileSize() != getTileSize()) {
                new GenericAlert("Tileset's tilesize of " + tileset1.getTileSize() + " does not match map's " +
                        "tilesize of " + getTileSize() + ".");
                return;
            }
            this.setTileset(tileset1);
        }
    }

    @Override
    public void deserializeBlock(String block, int x, int y) {
        if (block.equals("0")) {
            return;
        } else if (block.contains("_")) {
            String[] split = block.split("_");
            this.grid[x][y] = new WholeTile(Integer.valueOf(split[0]), Integer.valueOf(split[1]), getTileset());
        } else {
            this.grid[x][y] = new WholeTile(Integer.valueOf(block), 0, getTileset());
        }
    }

    @Override
    public void onClick(MouseEvent event) {
        int x = (int) (event.getX() / this.getTileSize());
        int y = (int) (event.getY() / this.getTileSize());

        if (event.getButton().equals(MouseButton.PRIMARY)) {
            Node node = event.getPickResult().getIntersectedNode();
            if (node == null || !(node instanceof PixelatedImageView)) {
                set(x, y, new WholeTile(1, 0, getTileset()));
            } else {
                WholeTile wholeTile = (WholeTile) this.grid[x][y];
                wholeTile.setBlockId((wholeTile.getBlockId() + 1) % 45);
                wholeTile.updateImageView();
            }
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            remove(x, y);
        } else if (event.getButton().equals(MouseButton.MIDDLE)) {
            Node node = event.getPickResult().getIntersectedNode();
            if (node != null && node instanceof PixelatedImageView) {
                WholeTile wholeTile = (WholeTile) this.grid[x][y];
                wholeTile.setDirection((wholeTile.getDirection() + 1) % 4);
                wholeTile.updateImageView();
            }
        }

    }


    int previousX = -1, previousY = -1;

    @Override
    public void onDrag(MouseEvent event) {
        int x = (int) (event.getX() / this.getTileSize());
        int y = (int) (event.getY() / this.getTileSize());
        if (x >= getWidth() || y >= getHeight() || x < 0 || y < 0) {
            return;
        }
        if (x != previousX || y != previousY) {
            previousX = x;
            previousY = y;

            if (event.getButton().equals(MouseButton.PRIMARY)) {
                Node node = event.getPickResult().getIntersectedNode();
                if (node == null || !(node instanceof PixelatedImageView)) {
                    set(x, y, new WholeTile(1, 0, getTileset()));
                } else {
                    WholeTile wholeTile = (WholeTile) this.grid[x][y];
                    wholeTile.setBlockId(wholeTile.getBlockId());
                    wholeTile.updateImageView();
                }
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                remove(x, y);
            }
        }
    }

    @Override
    public void draw() {
        Pane pane = Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId());
        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x <= getWidth() - 1; x++) {
                if (grid[x][y] == null) {
                    continue;
                }
                int blockId = ((WholeTile) grid[x][y]).getBlockId();
                if (blockId == 0) {
                    continue;
                }
                grid[x][y].draw(pane, x * getTileSize(), y * getTileSize());
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
        if (tile == null || getTileset() == null) {
            return;
        }
        remove(x, y);
        this.grid[x][y] = tile;
        this.grid[x][y].updateImageView();
        Pane pane = Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId());
        this.grid[x][y].draw(pane, x * getTileSize(), y * getTileSize());
        setChanged(true);
    }

    public void remove(int x, int y) {
        if (grid[x][y] == null) {
            return;
        }
        this.grid[x][y].remove(Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId()));
        this.grid[x][y] = null;
        setChanged(true);
    }

}
