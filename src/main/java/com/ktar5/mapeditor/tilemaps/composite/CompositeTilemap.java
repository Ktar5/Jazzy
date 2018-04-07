package com.ktar5.mapeditor.tilemaps.composite;

import com.ktar5.mapeditor.coordination.EditorCoordinator;
import com.ktar5.mapeditor.gui.utils.PixelatedImageView;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.composite.CompositeTile.Corner;
import com.ktar5.mapeditor.tilemaps.whole.WholeTile;
import com.ktar5.mapeditor.tileset.TilesetManager;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Paths;

public class CompositeTilemap extends BaseTilemap<CompositeTileset> {
    public CompositeTilemap(File saveFile, JSONObject json) {
        super(saveFile, json);
    }

    protected CompositeTilemap(File saveFile, int width, int height, int tileWidth, int tileHeight) {
        super(saveFile, width, height, tileWidth, tileHeight);
    }

    @Override
    public void deserializeBlock(String block, int x, int y) {
        this.grid[x][y] = new CompositeTile(getTileset(), block);
    }

    @Override
    protected void loadTilesetIfExists(JSONObject json) {
        if (json.has("tileset")) {
            File tileset = Paths.get(getSaveFile().getPath()).resolve(json.getString("tileset")).toFile();
            CompositeTileset tileset1 = TilesetManager.get().loadTileset(tileset, CompositeTileset.class);
            this.setTileset(tileset1);
        }
    }

    @Override
    public void onClick(MouseEvent event) {
        if (getTileset() == null) {
            return;
        }
        int x = (int) (event.getX() / this.getTileWidth());
        int y = (int) (event.getY() / this.getTileHeight());

        double xRemainder = (event.getX() / (double) this.getTileWidth()) - x;
        double yRemainder = (event.getY() / (double) this.getTileHeight()) - y;
        Corner corner = Corner.fromRemainders(xRemainder, yRemainder);

        if (event.getButton().equals(MouseButton.PRIMARY)) {
            leftClick(event, x, y, corner);
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            rightClick(event, x, y, corner);
        } else if (event.getButton().equals(MouseButton.MIDDLE)) {
            middleClick(event, x, y, corner);
        }
    }

    private int previousX = -1, previousY = -1;
    private Corner previousCorner = Corner.DOWN_LEFT;

    @Override
    public void onDrag(MouseEvent event) {
        if (getTileset() == null) {
            return;
        }

        int x = (int) (event.getX() / this.getTileWidth());
        int y = (int) (event.getY() / this.getTileHeight());

        if (x >= getWidth() || y >= getHeight() || x < 0 || y < 0) {
            return;
        }

        double xRemainder = (event.getX() / (double) this.getTileWidth()) - x;
        double yRemainder = (event.getY() / (double) this.getTileHeight()) - y;
        Corner corner = Corner.fromRemainders(xRemainder, yRemainder);

        if (x == previousX && y == previousY && corner == previousCorner) {
            return;
        }
        previousX = x;
        previousY = y;
        previousCorner = corner;

        if (event.getButton().equals(MouseButton.PRIMARY)) {
            leftClick(event, x, y, corner);
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            rightClick(event, x, y, corner);
        }
    }

    @Override
    public void onMove(MouseEvent event) {

    }

    public void leftClick(MouseEvent event, int x, int y, Corner corner) {
        Node node = event.getPickResult().getIntersectedNode();
        if (node == null || !(node instanceof PixelatedImageView)) {
            set(x, y, corner, 1, 1);
            return;
        }
        if (this.grid[x][y] instanceof WholeTile) {
            WholeTile tile = ((WholeTile) this.grid[x][y]);
            tile.setBlockId(tile.getBlockId() + 1);
            tile.updateImageView();
        } else if (this.grid[x][y] instanceof CompositeTile) {
            CompositeTile tile = ((CompositeTile) this.grid[x][y]);
            tile.set(corner, 1, (tile.getTileparts()[corner.ordinal()].getData() + 1) % 6);
        }
    }

    public void rightClick(MouseEvent event, int x, int y, Corner corner) {
        if (this.grid[x][y] instanceof WholeTile) {
            remove(x, y);
        } else if (this.grid[x][y] instanceof CompositeTile) {
            remove(x, y, corner);
        }
    }

    public void middleClick(MouseEvent event, int x, int y, Corner corner) {
//        Node node = event.getPickResult().getIntersectedNode();
//        if (node != null && node instanceof PixelatedImageView) {
//            WholeTile wholeTile = (WholeTile) this.grid[x][y];
//            wholeTile.setDirection((wholeTile.getDirection() + 1) % 4);
//            wholeTile.updateImageView();
//        }
    }

    @Override
    public void draw(Pane pane) {
        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x <= getWidth() - 1; x++) {
                if (grid[x][y] == null) {
                    continue;
                }
                int blockId = ((WholeTile) grid[x][y]).getBlockId();
                if (blockId == 0) {
                    continue;
                }
                grid[x][y].draw(pane, x * getTileWidth(), y * getTileHeight());
            }
        }
    }

    public void set(int x, int y, Corner corner, int id, int data) {
        CompositeTile tile;
        if (grid[x][y] != null && grid[x][y] instanceof CompositeTile) {
            tile = (CompositeTile) grid[x][y];
            tile.set(corner, id, data);
            tile.updateImageView();
        } else {
            remove(x, y);
            tile = new CompositeTile(getTileset());
            this.grid[x][y] = tile;
            refreshTile(x, y);
        }
    }

    public void set(int x, int y, WholeTile tile) {
        remove(x, y);
        this.grid[x][y] = tile;
        refreshTile(x, y);
    }

    private void refreshTile(int x, int y) {
        if (this.grid[x][y] == null) {
            return;
        }
        this.grid[x][y].updateImageView();
        Pane pane = EditorCoordinator.get().getEditor().getTabDrawingPane(getId());
        this.grid[x][y].draw(pane, x * getTileWidth(), y * getTileHeight());
        setChanged(true);
    }

    public void remove(int x, int y) {
        if (grid[x][y] == null) {
            return;
        }
        this.grid[x][y].remove(EditorCoordinator.get().getEditor().getTabDrawingPane(getId()));
        this.grid[x][y] = null;
        setChanged(true);
    }

    public void remove(int x, int y, Corner corner) {
        if (grid[x][y] == null || !(grid[x][y] instanceof CompositeTile)) {
            return;
        }
        ((CompositeTile) this.grid[x][y]).remove(EditorCoordinator.get().getEditor().getTabDrawingPane(getId()), corner);
        setChanged(true);
    }

}
