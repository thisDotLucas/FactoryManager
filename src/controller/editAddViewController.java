package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;
import view.AlertBox;
import java.util.Map;


public class editAddViewController {

    private TableRowData row;
    private Map<String, String> workIdMap;
    private boolean isEdit;

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
    private TextField dateTextField;

    @FXML
    private TextField workerTextField;

    @FXML
    private TextField workNameTextField;

    @FXML
    private TextField reasonTextField;

    @FXML
    private Button cancelButton;



    @FXML
    void onSavePress() {
        TableRowData OriginalRow = new TableRowData(row.getUser_id());
        OriginalRow.setTime(row.getTime());
        OriginalRow.setDate(row.getDate());
        OriginalRow.setWork_id(row.getWork_id());
        OriginalRow.setWork_step_name(row.getWork_step_name());
        OriginalRow.setReason(row.getReason());
        OriginalRow.setAmount_done(row.getAmount_done());
        OriginalRow.setTrash_amount(row.getTrash_amount());
        OriginalRow.setProductivity(row.getProductivity());

        if(hourTextField.getText().length() == 2 && minuteTextField.getText().length() == 2 && Integer.parseInt(hourTextField.getText()) < 24 && Integer.parseInt(minuteTextField.getText()) < 60){
                row.setTime(hourTextField.getText() + ":" + minuteTextField.getText() + ":00");
        } else {
            new AlertBox("Invalid Time.", 3);
            return;
        }

        if(DataMaps.getInstance().getWorkStepsMap().get(workNrTextField.getText()) == null){
            new AlertBox("Invalid work number.", 3);
            return;
        }
        row.setAmount_done(amountTextField.getText());
        row.setTrash_amount(trashTextField.getText());
        row.setReason(reasonTextField.getText());
        row.setDate(dateTextField.getText());
        row.setWork_step_name(workNameTextField.getText());
        row.setWork_id(workNrTextField.getText());

        if(isEdit) {
            MySqlDatabase.getInstance().deleteWorkStep(OriginalRow);
        }

        MySqlDatabase.getInstance().addWorkStep(row);
        EditAddRowHelper.getInstance().getController().updateTable();

        onCancelPress();
    }

    @FXML
    void onCancelPress() {
        EditAddRowHelper.getInstance().reset();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void initialize(){

        this.row = EditAddRowHelper.getInstance().getRow();
        this.isEdit = EditAddRowHelper.getInstance().isEdit();
        workIdMap = DataMaps.getInstance().getWorkStepsMap();

        dateTextField.setText(row.getDate());
        workerTextField.setText(row.getUser_id());

        if(isEdit) {
            hourTextField.setText(row.getTime().substring(0, 2));
            minuteTextField.setText(row.getTime().substring(3, 5));
            workNrTextField.setDisable(true);
        }

        workNrTextField.setText(row.getWork_id());
        workNameTextField.setText(row.getWork_step_name());
        amountTextField.setText(row.getAmount_done());
        trashTextField.setText(row.getTrash_amount());
        reasonTextField.setText(row.getReason());

        if(workNrTextField.getText().equals("00000") || workNrTextField.getText().equals("99999") || amountTextField.getText().equals("") && isEdit){
            amountTextField.setDisable(true);
            trashTextField.setDisable(true);
            reasonTextField.setDisable(true);
        }

        TextFieldHelper helper = new TextFieldHelper();

        helper.setCharLimit(hourTextField, 2);
        helper.setCharLimit(minuteTextField, 2);
        helper.setCharLimit(workNrTextField, 5);
        helper.setCharLimit(amountTextField, 3);
        helper.setCharLimit(trashTextField, 3);

        helper.onlyNumbers(hourTextField);
        helper.onlyNumbers(minuteTextField);
        helper.onlyNumbers(workNrTextField);
        helper.onlyNumbers(amountTextField);
        helper.onlyNumbers(trashTextField);

        checkWorkStepNameListener(workNrTextField);
    }



    private void checkWorkStepNameListener(TextField textField){
        textField.textProperty().addListener(l -> {
                if (textField.getText().length() == 5) {
                    if(!workNrTextField.getText().equals("") && workIdMap.get(workNrTextField.getText()) != null){
                        workNameTextField.setText(workIdMap.get(workNrTextField.getText()));
                    } else if(!workNrTextField.getText().equals("") && workIdMap.get(workNrTextField.getText()) == null){
                        workNameTextField.setText("Work step does not exist.");
                    }
                }
        });
    }

}
