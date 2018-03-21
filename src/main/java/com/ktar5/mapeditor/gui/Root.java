package com.ktar5.mapeditor.gui;

import com.ktar5.mapeditor.gui.bottombar.BottomBar;
import com.ktar5.mapeditor.gui.centerview.CenterView;
import com.ktar5.mapeditor.gui.centerview.editor.tabs.EditorTab;
import com.ktar5.mapeditor.gui.topmenu.TopMenu;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class Root extends VBox {
    private TopMenu topMenu;
    private CenterView centerView;
    private BottomBar bottomBar;

    public Root() {
        super();
        this.getChildren().addAll(topMenu = new TopMenu(), centerView = new CenterView(),
                bottomBar = new BottomBar());
    }

    public EditorTab getCurrentTab() {
        return centerView.getEditorViewPane().getCurrentTab();
    }

}
