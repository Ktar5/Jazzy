package com.ktar5.mapeditor.util;

import javafx.scene.input.MouseEvent;

public interface Interactable {

    public default void onClick(MouseEvent event){

    }

    public default void onMove(MouseEvent event){

    }

    public default void onDrag(MouseEvent event){

    }

    public default void onDragStart(MouseEvent event){

    }

    public default void onDragEnd(MouseEvent event){

    }

}
