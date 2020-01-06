package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;
import view.AlertBox;
import java.util.Map;


public class EditAddViewController {

    private TableRowData row;
    private Map<String, String> workIdMap;
    private boolean isEdit;
    private String seconds;

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


    /**
     * This method is called when the user presses the save button. A copy of the original row is created for deletion if
     * we are in edit mode to be able to delete it from the database before adding the new updated version. If we are in add
     * mode only one new row needs to be created. We access the manager view controller from the EditAddRowHelper to update
     * table view.
     * Fires on Save button press.
     */
    @FXML
    void onSavePress() {

        TableRowData originalRow = EditAddRowHelper.getInstance().getController().getSelectedRow();

        if(hourTextField.getText().length() == 2 && minuteTextField.getText().length() == 2 && Integer.parseInt(hourTextField.getText()) < 24 && Integer.parseInt(minuteTextField.getText()) < 60){
            if(!isEdit)
                row.setTime(hourTextField.getText() + ":" + minuteTextField.getText() + ":00");
            else
                row.setTime(hourTextField.getText() + ":" + minuteTextField.getText() + ":" + seconds);
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
            EditAddRowHelper.getInstance().getController().onDeleteRowPress();
        }

        MySqlDatabase.getInstance().workerAddWorkStep(row);
        EditAddRowHelper.getInstance().getController().addToMap(row.getUser_id(), row);

        onCancelPress();
    }

    /**
     * This method is called to close the EditAddView. The EditAddRowHelper is reset
     * and the stage is closed.
     */
    @FXML
    void onCancelPress() {
        EditAddRowHelper.getInstance().reset();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * This method is called on initialization. The TableRowData to be added or edited is stored into a variable.
     * JavaFx control properties are set and/or given listeners.
     */
    @FXML
    public void initialize(){

        this.row = EditAddRowHelper.getInstance().getRow();
        this.isEdit = EditAddRowHelper.getInstance().isEdit();
        workIdMap = DataMaps.getInstance().getWorkStepsMap();

        dateTextField.setText(row.getDate());
        workerTextField.setText(row.getUser_id());

        workNrTextField.setText(row.getWork_id());
        workNameTextField.setText(row.getWork_step_name());
        amountTextField.setText(row.getAmount_done());
        trashTextField.setText(row.getTrash_amount());
        reasonTextField.setText(row.getReason());

        if(isEdit) {
            hourTextField.setText(row.getTime().substring(0, 2));
            minuteTextField.setText(row.getTime().substring(3, 5));
            seconds = row.getTime().substring(Math.max(row.getTime().length() - 2, 0));
            workNrTextField.setDisable(true);
            row = new TableRowData(row.getUser_id());
        }

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

    /**
     * This listener updates the work step name text field when a fifth character is inserted into
     * the work number field.
     */
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
