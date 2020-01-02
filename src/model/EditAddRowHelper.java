package model;

import controller.ManagerViewController;

import java.io.IOException;

public class EditAddRowHelper {

    private static EditAddRowHelper ourInstance;
    private ManagerViewController controller;
    private TableRowData row;
    boolean isEdit;


    public static EditAddRowHelper getInstance() {

        if (ourInstance == null) {
            ourInstance = new EditAddRowHelper();
        }
        return ourInstance;
    }


    public void editRow(TableRowData row) {
        this.row = row;
        this.isEdit = true;
    }

    public void addRow(String user_id, String date) {
        this.row = new TableRowData(user_id);
        this.row.setDate(date);
        this.isEdit = false;
    }


    public boolean isEdit() {
        return isEdit;
    }

    public TableRowData getRow() {
        return row;
    }

    public void reset(){
        ourInstance = null;
    }

    public void setController(ManagerViewController controller){
        this.controller = controller;
    }

    public ManagerViewController getController(){
        return controller;
    }

}
