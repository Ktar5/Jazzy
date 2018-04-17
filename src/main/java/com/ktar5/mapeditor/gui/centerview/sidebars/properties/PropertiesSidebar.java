package com.ktar5.mapeditor.gui.centerview.sidebars.properties;

import com.ktar5.mapeditor.Main;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PropertiesSidebar extends Pane {
    private RootProperty root;

    public PropertiesSidebar(RootProperty root) {
        super();
        this.root = root;

        this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setMaxWidth(500);

        TreeItem<Property> rootNode = new TreeItem<>(root);
        for (Property property : root.getChildren().values()) {
            addNode(rootNode, property);
        }
        rootNode.setExpanded(true);

        //name column
        TreeTableColumn<Property, String> nameColumn = new TreeTableColumn<>("Property");
        nameColumn.setCellFactory(p -> new EditingCell());
        nameColumn.setOnEditCommit((p) -> {
            TreeTablePosition<Property, ?> editingCell = p.getTreeTableView().getEditingCell();
            editingCell.getTreeItem().getValue().nameProperty.set(p.getNewValue());
        });
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty);


        //data column
        TreeTableColumn<Property, String> dataColumn = new TreeTableColumn<>("Value");
        dataColumn.setCellFactory(p -> new EditingCell());
        dataColumn.setOnEditCommit((p) -> {
            TreeTablePosition<Property, ?> editingCell = p.getTreeTableView().getEditingCell();
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
        treeTableView.getColumns().add(nameColumn);
        treeTableView.getColumns().add(dataColumn);
        treeTableView.setShowRoot(false);
        treeTableView.setEditable(true);

        treeTableView.prefWidthProperty().bind(this.widthProperty());
        treeTableView.prefHeightProperty().bind(this.heightProperty());

        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            setPrefWidths(newValue.intValue(), nameColumn, dataColumn, .55);
        });
        System.out.println("Style: " + getClass().getResource("/tableview.css").toExternalForm());
        final String cssUrl1 = getClass().getResource("/tableview.css").toExternalForm();
        Main.root.getScene().getStylesheets().add(cssUrl1);

        this.getChildren().add(treeTableView);
    }

    public void setPrefWidths(int newWidth, TreeTableColumn smaller, TreeTableColumn larger, double smallerRatio) {
        final int smallerWidth = (int) (newWidth * smallerRatio);
        final int largerWidth = newWidth - smallerWidth - 2;
        smaller.setPrefWidth(smallerWidth);
        larger.setPrefWidth(largerWidth);
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
