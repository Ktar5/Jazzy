package com.ktar5.mapeditor.gui.centerview.tabs;

import com.ktar5.mapeditor.gui.centerview.EditorPane;
import com.ktar5.mapeditor.util.Tabbable;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.UUID;

import static com.ktar5.mapeditor.gui.dialogs.QuitSaveConfirmation.quitSaveConfirmation;

public abstract class AbstractTab extends Tab {
    private final UUID tabId;
    private boolean hasEdits = false;
    protected EditorPane pane;

    public AbstractTab(UUID uuid) {
        this.tabId = uuid;

        this.setText(getTabbable().getName());

        pane = getEditorPane();

        this.setOnCloseRequest(e -> {
            if (this.hasEdits) quitSaveConfirmation(e, getTabbable());
        });

        pane.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        this.setOnClosed(e -> getTabbable().remove());

        pane.getViewport().addEventFilter(MouseEvent.MOUSE_CLICKED, getTabbable()::onClick);
        pane.getViewport().addEventFilter(MouseEvent.MOUSE_DRAGGED, getTabbable()::onDrag);
    }

    protected abstract EditorPane getEditorPane();

    public abstract void draw();

    public abstract Tabbable getTabbable();

    public abstract void onSelect();

    public Pane getViewport() {
        return pane.getViewport();
    }

    public boolean getEdits() {
        return hasEdits;
    }

    public UUID getTabId() {
        return tabId;
    }

    public void setEdit(boolean value) {
        if (value == hasEdits) {
            return;
        }
        hasEdits = value;
        if (hasEdits && !this.getText().startsWith("* ")) {
            this.setText("* " + this.getText());
        } else {
            this.setText(getTabbable().getName());
        }
    }

}
