package com.ktar5.mapeditor.gui.centerview.tabs;

import com.ktar5.mapeditor.gui.centerview.EditorPane;
import com.ktar5.mapeditor.gui.centerview.sidebars.DetailsSidebar;
import com.ktar5.mapeditor.tileset.TilesetManager;
import com.ktar5.mapeditor.util.Tabbable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TilesetTab extends AbstractTab {
    private HBox viewportLayout;
    private DetailsSidebar detailsSidebar;

    public TilesetTab(UUID tilemap) {
        super(tilemap);
        viewportLayout = new HBox();
        VBox.setVgrow(viewportLayout, Priority.ALWAYS);
        viewportLayout.getChildren().addAll(
                detailsSidebar = new DetailsSidebar(),
                this.pane
        );
        this.setContent(viewportLayout);
    }

    @Override
    protected EditorPane getEditorPane() {
        return new EditorPane(getTabbable().getDimensions());
    }

    @Override
    public void draw() {
        getTabbable().draw(getViewport());
    }

    @Override
    public Tabbable getTabbable() {
        return TilesetManager.get().getTileset(getTabId());
    }

    @Override
    public void onSelect() {

    }


}
