package com.ktar5.mapeditor.gui;

import com.ktar5.mapeditor.gui.bottombar.BottomBar;
import com.ktar5.mapeditor.gui.centerview.TabHoldingPane;
import com.ktar5.mapeditor.gui.topmenu.TopMenu;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

@Getter
public class Root extends BorderPane {
    private TopMenu topMenu;

    private TabHoldingPane centerView;

    private BottomBar bottomBar;

    public Root() {
        super();
        this.setTop(topMenu = new TopMenu());
        this.setCenter(centerView = new TabHoldingPane());
        this.setBottom(bottomBar = new BottomBar());
    }

}
