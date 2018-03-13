package com.ktar5.mapeditor.input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyPress implements EventHandler<KeyEvent> {


    @Override
    public void handle(KeyEvent event) {
        KeyCode code = event.getCode();
//        switch (code) {
//            case LEFT: {
//                tileMap.setTranslateX(tileMap.getTranslateX() + 10);
//                break;
//            }
//            case RIGHT: {
//                tileMap.setTranslateX(tileMap.getTranslateX() - 10);
//                break;
//            }
//            case UP: {
//                tileMap.setTranslateY(tileMap.getTranslateY() + 10);
//                break;
//            }
//            case DOWN: {
//                tileMap.setTranslateY(tileMap.getTranslateY() - 10);
//                break;
//            }
//            default:
//                break;
//        }
    }


}