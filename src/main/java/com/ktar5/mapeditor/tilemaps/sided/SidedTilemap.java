package com.ktar5.mapeditor.tilemaps.sided;

import com.ktar5.mapeditor.coordination.EditorCoordinator;
import com.ktar5.mapeditor.gui.utils.PixelatedImageView;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.sided.SidedTile.Side;
import com.ktar5.mapeditor.tilemaps.whole.WholeTile;
import com.ktar5.mapeditor.tileset.TilesetManager;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SidedTilemap extends BaseTilemap<SidedTileset> {
    int currentId = 1;
    
    public SidedTilemap(File saveFile, JSONObject json) {
        super(saveFile, json);
    }
    
    public SidedTilemap(File saveFile, int width, int height, int tileWidth, int tileHeight) {
        super(saveFile, width, height, tileWidth, tileHeight);
    }
    
    @Override
    public void deserializeBlock(String block, int x, int y) {
        if (block.length() != 1) {
            this.grid[x][y] = new SidedTile(getTileset(), block);
        }
    }
    
    @Override
    protected void loadTilesetIfExists(JSONObject json) {
        if (json.has("tileset")) {
            File tileset = Paths.get(getSaveFile().getPath()).resolve(json.getString("tileset")).toFile();
            SidedTileset tileset1 = TilesetManager.get().loadTileset(tileset, SidedTileset.class);
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
        Side side = Side.fromRemainders(xRemainder, yRemainder);
        
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            leftClick(event, x, y, side);
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            rightClick(event, x, y, side);
        } else if (event.getButton().equals(MouseButton.MIDDLE)) {
            middleClick(event, x, y, side);
        }
    }
    
    private int previousX = -1, previousY = -1;
    private Side previousSide = Side.UP;
    
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
        Side side = Side.fromRemainders(xRemainder, yRemainder);
        
        if (x == previousX && y == previousY && side == previousSide) {
            return;
        }
        
        previousX = x;
        previousY = y;
        previousSide = side;
    
        refreshHighlight(x, y, side);
        
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            leftClick(event, x, y, side);
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            rightClick(event, x, y, side);
        }
    }
    
    @Override
    public void onDragStart(MouseEvent event) {
        onDrag(event);
    }
    
    @Override
    public void onMove(MouseEvent event) {
        int x = (int) (event.getX() / this.getTileWidth());
        int y = (int) (event.getY() / this.getTileHeight());
        
        if (x >= getWidth() || y >= getHeight() || x < 0 || y < 0) return;
        
        double xRemainder = (event.getX() / (double) this.getTileWidth()) - x;
        double yRemainder = (event.getY() / (double) this.getTileHeight()) - y;
        Side side = Side.fromRemainders(xRemainder, yRemainder);
        
        if (x == previousX && y == previousY && side == previousSide) return;
        
        previousX = x;
        previousY = y;
        previousSide = side;
        
        refreshHighlight(x, y, side);
    }
    
    public void leftClick(MouseEvent event, int x, int y, Side side) {
        Node node = event.getPickResult().getIntersectedNode();
        if (node == null || !(node instanceof PixelatedImageView)) {
            set(x, y, side, currentId);
        } else if (this.grid[x][y] instanceof WholeTile) {
            WholeTile tile = ((WholeTile) this.grid[x][y]);
            tile.setBlockId(tile.getBlockId() + 1);
            tile.updateAllImageViews();
        } else if (this.grid[x][y] instanceof SidedTile) {
            set(x, y, side, currentId);
        }
    }
    
    public void rightClick(MouseEvent event, int x, int y, Side corner) {
        if (this.grid[x][y] instanceof WholeTile) {
            remove(x, y);
        } else if (this.grid[x][y] instanceof SidedTile) {
            remove(x, y, corner);
        }
    }
    
    public void middleClick(MouseEvent event, int x, int y, Side corner) {
//        Node node = event.getPickResult().getIntersectedNode();
//        if (node != null && node instanceof PixelatedImageView) {
//            WholeTile wholeTile = (WholeTile) this.grid[x][y];
//            wholeTile.setDirection((wholeTile.getDirection() + 1) % 4);
//            wholeTile.updateAllImageViews();
//        }
    }
    
    @Override
    public void draw(Pane pane) {
        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x <= getWidth() - 1; x++) {
                if (grid[x][y] == null) {
                    continue;
                }
                grid[x][y].draw(pane, x * getTileWidth(), y * getTileHeight());
            }
        }
    }
    
    public void set(int x, int y, Side side, int id) {
        SidedTile tile;
        if (grid[x][y] != null && grid[x][y] instanceof SidedTile) {
            tile = (SidedTile) grid[x][y];
            tile.setSide(side, id);
            System.out.println("NNOO");
        } else {
            System.out.println("YYEE");
            tile = new SidedTile(getTileset());
            tile.setSide(side, id);
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
        this.grid[x][y].updateAllImageViews();
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
    
    public void remove(int x, int y, Side side) {
        if (grid[x][y] == null || !(grid[x][y] instanceof SidedTile)) {
            return;
        }
        ((SidedTile) grid[x][y]).removeSide(EditorCoordinator.get().getEditor().getTabDrawingPane(getId()), side);
        setChanged(true);
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
    
    private Polygon triangle;
    
    private void refreshHighlight(int x, int y, Side side) {
        if (triangle == null) {
            triangle = new Polygon(0, 0,
                    this.getTileWidth() / 2, this.getTileHeight() / 2,
                    this.getTileWidth(), 0);
            triangle.setFill(Color.AQUA.deriveColor(0, 1, 1, .5f));
            EditorCoordinator.get().getEditor().getTabDrawingPane(getId()).getChildren().add(triangle);
        }
        triangle.toFront();
        triangle.setRotate(90 * side.ordinal());
        triangle.setTranslateX(x * this.getTileWidth() + (this.getTileWidth() * side.x));
        triangle.setTranslateY(y * this.getTileHeight() + (this.getTileHeight() * side.y));
    }
    
}
