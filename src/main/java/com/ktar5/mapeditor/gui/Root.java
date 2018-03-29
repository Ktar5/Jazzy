package com.ktar5.mapeditor.gui;

import com.ktar5.mapeditor.gui.bottombar.BottomBar;
import com.ktar5.mapeditor.gui.centerview.TabHoldingPane;
import com.ktar5.mapeditor.gui.topmenu.TopMenu;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class Root extends VBox {
    private TopMenu topMenu;
    private TabHoldingPane centerView;
    private BottomBar bottomBar;

    public Root() {
        super();
        this.getChildren().addAll(
                topMenu = new TopMenu(),
                centerView = new TabHoldingPane(),
                bottomBar = new BottomBar());
    }

}
