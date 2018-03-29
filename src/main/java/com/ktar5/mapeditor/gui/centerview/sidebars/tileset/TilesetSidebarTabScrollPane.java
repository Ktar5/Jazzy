package com.ktar5.mapeditor.gui.centerview.sidebars.tileset;

import com.ktar5.mapeditor.coordination.EditorCoordinator;
import com.ktar5.mapeditor.coordination.EventCoordinator;
import com.ktar5.mapeditor.gui.Scrollable;
import com.ktar5.mapeditor.tilemaps.whole.WholeTileset;
import com.ktar5.mapeditor.tileset.BaseTileset;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class TilesetSidebarTabScrollPane extends ScrollPane {
    private Scrollable scrollable;
    private Pane viewport;

    private BaseTileset tileset;


    public TilesetSidebarTabScrollPane() {
        this.scrollable = new Scrollable();

        viewport = new Pane();

        this.setContent(this.viewport);

        viewport.setVisible(true);
        viewport.setMaxSize(250, 250);
        viewport.setPrefSize(250, 250);

        viewport.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        this.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        this.setVbarPolicy(ScrollBarPolicy.ALWAYS);

        scrollable.setAll(this, viewport);

        viewport.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;
            Node node = event.getPickResult().getIntersectedNode();
            if (node == null || !(node instanceof WholeTileset.WholeTilesetImageView)) return;

            WholeTileset.WholeTilesetImageView view = ((WholeTileset.WholeTilesetImageView) node);
            EventCoordinator.get().fireEvent(new TileSelectEvent(EditorCoordinator.get().getCurrentTab().getTabId(),
                    view.getTileId(), view.getTileset()));
        });

    }

    public void setTileset(BaseTileset tileset) {
        this.tileset = tileset;
        viewport.setPrefSize(tileset.getDimensionX(), tileset.getDimensionY());
        redraw();
    }

    private void redraw() {
        tileset.draw(viewport);
    }

}
