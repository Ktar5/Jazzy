package com.ktar5.mapeditor.gui.centerview.sidebars.tileset;

import com.ktar5.mapeditor.coordination.EditorCoordinator;
import com.ktar5.mapeditor.coordination.EventCoordinator;
import com.ktar5.mapeditor.gui.utils.ZoomablePannablePane;
import com.ktar5.mapeditor.tilemaps.whole.WholeTileset;
import com.ktar5.mapeditor.tileset.BaseTileset;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import lombok.Getter;

@Getter
public class TilesetSidebarViewPane extends Pane {
    private Pane viewport;

    private BaseTileset tileset;

    public TilesetSidebarViewPane() {
        viewport = new Pane();

        VBox.setVgrow(this, Priority.ALWAYS);

        AnchorPane set = new ZoomablePannablePane().set(viewport);
        set.prefHeightProperty().bind(this.heightProperty());
        set.prefWidthProperty().bind(this.widthProperty());

        this.getChildren().add(set);

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
