package com.ktar5.mapeditor.gui.bottombar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BottomBar extends HBox {
    private Label left;
    private Label right;

    public BottomBar() {
        super();

        left = new Label("Left status");
        left.maxWidth(-1.0);
        left.maxHeight(Double.MAX_VALUE);
        HBox.setHgrow(left, Priority.ALWAYS);
        left.setTextFill(new Color(.625, .625, .625, 1));
        left.setFont(Font.font(11));

        Pane center = new Pane();
        HBox.setHgrow(center, Priority.ALWAYS);
        center.prefHeight(-1);
        center.prefWidth(-1);

        right = new Label("Right status");
        right.maxWidth(-1.0);
        // ? right.maxHeight(Double.MAX_VALUE);
        HBox.setHgrow(right, Priority.NEVER);
        right.setTextFill(new Color(.625, .625, .625, 1));
        right.setFont(Font.font(11));

        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(5.0);

        VBox.setVgrow(this, Priority.NEVER);

        this.setPadding(new Insets(3, 3, 3, 3));
    }

    public void setLeftText(String text) {
        left.setText(text);
    }

    public void setRightText(String text) {
        right.setText(text);
    }


}
