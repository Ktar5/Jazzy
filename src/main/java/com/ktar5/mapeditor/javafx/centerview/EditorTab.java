package com.ktar5.mapeditor.javafx.centerview;

import com.ktar5.mapeditor.tilemap.MapManager;
import com.ktar5.mapeditor.tilemap.Tilemap;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import lombok.Getter;

import java.util.UUID;

public class EditorTab extends Tab {
    UUID tilemap;
    boolean hasEdits;
    boolean isSelected;

    public EditorTab(UUID tilemap) {
        Tilemap map = MapManager.get().getMap(tilemap);
        this.tilemap = map.getId();
        this.setText(map.getMapName());
        this.setContent(new EditorPane(map.getCanvas()));
    }

    //TODO actions

    @Getter
    public class EditorPane extends ScrollPane {

        public EditorPane(Canvas canvas) {
            super();

            this.setFitToHeight(true);
            this.setFitToWidth(true);
            this.prefHeight(-1);
            this.prefWidth(-1);

            this.setContent(canvas);
        }
    }

}
