package com.ktar5.mapeditor.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.ktar5.tilejump.tools.mapeditor.TileManager;
import com.ktar5.tilejump.tools.mapeditor.Tilemap;
import com.ktar5.tilejump.tools.mapeditor.tiles.Tile;
import com.ktar5.tilejump.variables.Const;

public class GridRenderer implements Disposable {
    public static final int radius = 16;

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    public GridRenderer() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    public void update(float dTime) {

    }

    public void render(float dTime) {
        //Because OpenGL needs this.
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(TileManager.get().getCamera().getCamera().combined);

        int x = (int) (TileManager.get().getCamera().getCamera().position.x);
        int y = (int) (TileManager.get().getCamera().getCamera().position.y);

        Tilemap tilemap = TileManager.get().getMap(TileManager.get().getCurrentLevel());
        Tile[][] grid = tilemap.grid;

        spriteBatch.begin();
        for (int i = x - radius; i < x + radius; i++) {
            for (int k = y - radius; k < y + radius; k++) {
                if (!tilemap.isInMapRange(i, k)) {
                    continue;
                }
                if (grid[x][y].isFoursquare()) {
                    renderFoursquare(grid[x][y]);
                } else {
                    renderBlock(grid[x][y]);
                }

            }
        }
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(TileManager.get().getCamera().getCamera().combined);
        renderGrid();
        renderCursor();
    }

    public void renderBlock(Tile tile) {
        spriteBatch.draw(null,
                tile.x - .5f, tile.y - .5f, 0, 0, 16, 16,
                1 / Const.SCALE, 1 / Const.SCALE, 0
        );
    }

    public void renderFoursquare(Tile tile) {
        spriteBatch.draw(null,
                tile.x - .5f, tile.y - .5f, 0, 0, 16, 16,
                1 / Const.SCALE, 1 / Const.SCALE, 0
        );
    }

    public void renderGrid() {

    }

    public void renderCursor() {

    }

    public void renderHud() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}

