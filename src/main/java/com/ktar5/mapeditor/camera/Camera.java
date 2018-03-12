package com.ktar5.mapeditor.camera;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Camera {
    private int xPos, yPos;
    private int width, height;
    private float zoom;

    public void incrementPosition(int x, int y) {
        this.xPos += x;
        this.yPos += y;
        this.update();
    }

    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        update();
    }

    public void update() {

    }


}
