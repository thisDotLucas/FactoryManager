package model;

import controller.ManagerViewController;

/**
 * SINGLETON OBJECT
 * This class is used to communicate between the editAddView and the managerView
 * whenever we want to edit or add a row.
 */
public class EditAddRowHelper {

    private static EditAddRowHelper ourInstance;
    private ManagerViewController controller; //Used to be able to update the table view.
    private TableRowData row; //Row to be edited or added.
    private boolean isEdit; //Edit mode if true and add mode if false.


    public static EditAddRowHelper getInstance() {

        if (ourInstance == null) {
            ourInstance = new EditAddRowHelper();
        }
        return ourInstance;
    }


    public void reset(){
        ourInstance = null;
    }

    /**
     * This method is called when we want to edit a row.
     */
    public void editRow(TableRowData row) {
        this.row = row;
        this.isEdit = true;
    }

    /**
     * This method is called when we want to add a row.
     */
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

    public void setController(ManagerViewController controller){
        this.controller = controller;
    }

    public ManagerViewController getController(){
        return controller;
    }

}
