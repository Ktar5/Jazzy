package com.ktar5.mapeditor.gui.centerview.sidebars.properties;

import com.ktar5.mapeditor.properties.ParentProperty;
import com.ktar5.mapeditor.properties.Property;
import com.ktar5.mapeditor.properties.RootProperty;
import com.ktar5.mapeditor.properties.StringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class PropertiesSidebar extends Pane {
    private RootProperty root;
    
    public PropertiesSidebar(RootProperty root) {
        super();
        this.root = root;
        
        this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setMaxWidth(500);
        this.setPrefWidth(250);
        
        TreeItem<Property> rootNode = new TreeItem<>(root);
        for (Property property : root.getChildren().values()) {
            addNode(rootNode, property);
        }
        rootNode.setExpanded(true);
        
        //Creating a column
        TreeTableColumn<Property, String> column = new TreeTableColumn<>("Property");
        column.setPrefWidth(150);
        //Defining cell content
        column.setCellValueFactory(p -> p.getValue().getValue().nameProperty);
        
        
        //data column
        TreeTableColumn<Property, String> dataColumn = new TreeTableColumn<>("Value");
        dataColumn.setPrefWidth(120);
        dataColumn.setCellFactory(p -> new EditingCell());
        dataColumn.setOnEditCommit((p) -> {
            TreeTablePosition<Property, ?> editingCell = p.getTreeTableView().getEditingCell();
            if (editingCell == null) {
                System.out.println("editing cell is null");
                return;
            }
            if (p.getRowValue().getValue() instanceof ParentProperty) return;
            
            ((StringProperty) editingCell.getTreeItem().getValue()).valueProperty.set(p.getNewValue());
        });
        dataColumn.setCellValueFactory(param -> {
            if (param.getValue().getValue() instanceof ParentProperty)
                return new ReadOnlyStringWrapper("");
            else
                return ((StringProperty) param.getValue().getValue()).valueProperty;
        });
        
        //Creating a tree table view
        final TreeTableView<Property> treeTableView = new TreeTableView<>(rootNode);
        treeTableView.getColumns().add(column);
        treeTableView.getColumns().add(dataColumn);
        treeTableView.setShowRoot(false);
        treeTableView.setEditable(true);
        
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setBottomAnchor(treeTableView, 0d);
        AnchorPane.setTopAnchor(treeTableView, 0d);
        AnchorPane.setLeftAnchor(treeTableView, 0d);
        AnchorPane.setRightAnchor(treeTableView, 0d);
        anchorPane.getChildren().add(treeTableView);
        this.getChildren().add(anchorPane);
    }
    
    public void addNode(TreeItem<Property> parent, Property property) {
        TreeItem<Property> childNode = new TreeItem<>(property);
        parent.getChildren().add(childNode);
        if (property instanceof ParentProperty) {
            for (Property descendant : ((ParentProperty) property).getChildren().values()) {
                addNode(childNode, descendant);
            }
        }
    }
    
}
