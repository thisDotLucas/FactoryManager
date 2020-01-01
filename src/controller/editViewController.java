package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.TableRowData;

public class editViewController {

    TableRowData row;
    boolean isEdit;


    @FXML
    private TextField workNrTextField;

    @FXML
    private TextField amountTextField;

    @FXML
    private TextField hourTextField;

    @FXML
    private TextField minuteTextField;

    @FXML
    private TextField trashTextField;

    @FXML
    private ComboBox<String> reasonComboBox;

    @FXML
    void viewClicked() {
        System.out.println("Lmao");
    }

    @FXML
    void onViewEntered(){
        System.out.println("XD");
    }

    @FXML
    void onSavePress() {

    }

    @FXML
    void onCancelPress() {

    }

    @FXML
    public void initialize(){

    }

    public editViewController(TableRowData row){
        this.row = row;
        this.isEdit = true;
    }

    public editViewController(){
        this.isEdit = false;
    }

}
