package com.ktar5.mapeditor.javafx;

import com.ktar5.mapeditor.javafx.bottombar.BottomBar;
import com.ktar5.mapeditor.javafx.centerview.CenterView;
import com.ktar5.mapeditor.javafx.topmenu.TopMenu;
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

}
