package com.ktar5.mapeditor.gui.centerview.sidebars.tileset;

import com.ktar5.mapeditor.gui.PixelatedImageView;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class SelectedTileView extends Pane {
    private Pane viewport;
    private PixelatedImageView imageView;

    public SelectedTileView() {
        viewport = new Pane();

        viewport.setVisible(true);
        viewport.setMaxSize(250, 250);
        viewport.setPrefSize(250, 250);

        this.setMaxSize(250, 250);
        this.setPrefSize(250, 250);

        viewport.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.getChildren().add(viewport);
    }

    public void setTile(PixelatedImageView imageView) {
        this.imageView = imageView;
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);
        viewport.getChildren().clear();
        viewport.getChildren().add(imageView);
    }

}
