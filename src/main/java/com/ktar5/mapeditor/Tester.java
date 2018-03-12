package com.ktar5.mapeditor;

import com.ktar5.mapeditor.grid.MapManager;

public class Tester {

    public void testCreateMap() {
        MapManager.get().createMap(5, 5, 16, 1);
        assert MapManager.get().getMap(1) != null;
    }

    public void testSaveMap() {
        MapManager.get().saveMap(1);
    }

    public void testLoadMap() {
        MapManager.get().loadMap(1);
    }

}
