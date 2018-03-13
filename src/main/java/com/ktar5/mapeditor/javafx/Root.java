package com.ktar5.mapeditor.javafx;

import com.ktar5.mapeditor.javafx.bottombar.BottomBar;
import com.ktar5.mapeditor.javafx.centerview.CenterView;
import com.ktar5.mapeditor.javafx.topmenu.TopMenu;
import javafx.scene.layout.VBox;

public class Root extends VBox {

    public Root(){
        super();
        this.getChildren().addAll(new TopMenu(), new CenterView(), new BottomBar());
    }

}
