package com.ktar5.mapeditor.gui.centerview.sidebars.tileset;

import com.ktar5.mapeditor.gui.utils.PixelatedImageView;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class SelectedTileView extends AnchorPane {
    private Pane viewport;
    private PixelatedImageView imageView;

    public SelectedTileView() {
        viewport = new Pane();

        viewport.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.setMinHeight(75);

        AnchorPane.setLeftAnchor(viewport, 10d);
        AnchorPane.setRightAnchor(viewport, 10d);
        AnchorPane.setTopAnchor(viewport, 10d);
        AnchorPane.setBottomAnchor(viewport, 10d);

        viewport.prefWidthProperty().bind(this.widthProperty());
        viewport.prefHeightProperty().bind(this.widthProperty());

        this.getChildren().add(viewport);
    }

    public void setTile(PixelatedImageView imageView) {
        this.imageView = imageView;
        imageView.fitWidthProperty().bind(Bindings.when(viewport.widthProperty().lessThan(viewport.heightProperty()))
                .then(viewport.widthProperty()).otherwise(viewport.heightProperty())
        );
        imageView.fitHeightProperty().bind(Bindings.when(viewport.widthProperty().lessThan(viewport.heightProperty()))
                .then(viewport.widthProperty()).otherwise(viewport.heightProperty())
        );

        viewport.getChildren().clear();
        viewport.getChildren().add(imageView);
    }

}
