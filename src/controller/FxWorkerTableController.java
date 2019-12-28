package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import model.TableRowData;

public class FxWorkerTableController {

    TableView<?> table;

    public FxWorkerTableController(TableView<?> table){
        this.table = table;
    }

    public void addRow(TableRowData row){
        //ObservableList<String> list = new FXCollections().observableArrayList();

        //table.getItems().add();
    }

}
