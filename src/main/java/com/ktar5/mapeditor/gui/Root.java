package com.ktar5.mapeditor.gui;

import com.ktar5.mapeditor.gui.bottombar.BottomBar;
import com.ktar5.mapeditor.gui.centerview.CenterView;
import com.ktar5.mapeditor.gui.topmenu.TopMenu;
import javafx.scene.layout.VBox;

public class Root extends VBox {

    public Root(){
        super();
        this.getChildren().addAll(new TopMenu(), new CenterView(), new BottomBar());
    }

}
