package com.ktar5.mapeditor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ktar5.tilejump.variables.Const;

import java.io.File;

public class MapEditor implements ApplicationListener {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.forceExit = false;
        config.width = 1200;
        config.height = 800;
        config.title = "TiledMapPacker";
        new LwjglApplication(new MapEditor(), config);
    }

    @Override
    public void create() {
        File file = new File("D:\\GameDev\\Projects\\TileJumpGame\\tools\\mapgen");
        if (!file.exists()) {
            file.mkdir();
        }
        new TileManager(file);
        testCreateMap();
        testSaveMap();
    }

    @Override
    public void resize(int width, int height) {
        TileManager.get().getCamera().getViewport().update(width, height);
    }

    float time = 0;
    @Override
    public void render() {
        //Get time since last frame
        float dTime = Gdx.graphics.getDeltaTime();
        //If game too laggy, prevent massive bugs by using a small constant number
        time += Math.min(dTime, Const.MAX_FRAME_TIME);
        //While our time is greater than our fixed step size...
        while (time >= Const.STEP_TIME) {
            time -= Const.STEP_TIME;
            //Update the camera
            TileManager.get().getCamera().update(Const.STEP_TIME);
        }

        TileManager.get().getGridRenderer().update(Const.STEP_TIME);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public void testCreateMap() {
        TileManager.get().createMap(5, 5, 1);
        assert TileManager.get().getMap(1) != null;
    }

    public void testSaveMap() {
        TileManager.get().saveMap(1);
    }

    public void testLoadMap() {
        TileManager.get().loadMap(1);
    }

}
