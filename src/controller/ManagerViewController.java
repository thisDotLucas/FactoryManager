package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.*;
import view.AlertBox;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class ManagerViewController implements Viewable {

    Manager user;

    @FXML
    private TextField userTextField;

    @FXML
    private Label userLabel;

    @FXML
    private Label accessLabel;

    @FXML
    private Button logOutButton;

    @FXML
    private TextField keyTextField;

    @FXML
    private ComboBox<String> workerComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label timeLabel;

    @FXML
    private TextArea messageBox;

    @FXML
    private ComboBox<String> receiverComboBox;

    @FXML
    private Button sendButton;

    @FXML
    private TableView<TableRowData> table;

    @FXML
    private TableColumn<TableRowData, String> productivityColumn;

    @FXML
    private TableColumn<TableRowData, String> workNameColumn;

    @FXML
    private TableColumn<TableRowData, String> startColumn;

    @FXML
    private TableColumn<TableRowData, String> workerColumn;

    @FXML
    private TableColumn<TableRowData, String> amountColumn;

    @FXML
    private TableColumn<TableRowData, String> trashColumn;

    @FXML
    private TableColumn<TableRowData, String> workNrColumn;

    @FXML
    private TableColumn<TableRowData, String> dateColumn;

    @FXML
    private TableColumn<TableRowData, String> reasonColumn;


    @FXML
    void logOutPress() throws Exception {
        ViewNavigator.getInstance().goToLogInView();

    }

    @FXML
    void onDateChanged(){
        updateTable();
    }

    @FXML
    void onWorkerChanged(){
        updateTable();
    }

    private void updateTable() {

        ObservableList<TableRowData> rowData;
        rowData = MySqlDatabase.getInstance().getWorkSteps(new TimeAndDateHelper().formatDate(datePicker.getValue()), DataMaps.getInstance().getNameKeyMap().get(workerComboBox.getValue()));
        table.setItems(rowData);

    }

    @FXML
    void sendPress() {

        if (!messageBox.getText().equals("")) {
            String msg = messageBox.getText();
            MySqlDatabase.getInstance().sendMessage(user.getUserKey(), DataMaps.getInstance().getNameKeyMap().get(receiverComboBox.getValue()), msg, new TimeAndDateHelper().getTimeAndDate());
            new AlertBox("Message Sent.", 0);
            messageBox.clear();
        } else
            new AlertBox("Message box is empty.", 3);
    }

    @FXML
    void onReceiverComboBox() {
        sendButton.setDisable(false);
    }

    @FXML
    public void initialize() {
        user = (Manager) ViewNavigator.getInstance().getLoggedInUser();
        timeController();
        receiverComboBox.setItems(DataMaps.getInstance().getWorkerNames());
        workerComboBox.setItems(DataMaps.getInstance().getWorkerNames());
        setDatePickerFormat();
        datePicker.setValue(LocalDate.from(LocalDateTime.now()));
        userLabel.setText(user.getUserName());
        userTextField.setDisable(true);
        keyTextField.setDisable(true);
        sendButton.setDisable(true);
        showNotifications();

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        workNrColumn.setCellValueFactory(new PropertyValueFactory<>("work_id"));
        workerColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount_done"));
        trashColumn.setCellValueFactory(new PropertyValueFactory<>("trash_amount"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        productivityColumn.setCellValueFactory(new PropertyValueFactory<>("productivity"));
        workNameColumn.setCellValueFactory(new PropertyValueFactory<>("work_step_name"));
    }

    private void setDatePickerFormat() {

        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

    }


    private void showNotifications() {
        ArrayList<String> senders = MySqlDatabase.getInstance().getNotifications(user.getUserKey());
        IOHelper helper = new IOHelper();
        Map<String, Employee> employeeMap = DataMaps.getInstance().getEmployeeMap();
        String stringToBeShown = "";

        for (String sender : senders) {
            stringToBeShown += helper.capitalizeFirsChar(employeeMap.get(sender).getUserName()) + " Needs Assistance.\n";
        }

        if (!stringToBeShown.equals("")) {
            new AlertBox(stringToBeShown, "Notification", 0);
            MySqlDatabase.getInstance().deleteNotifications(user.getUserKey());
        }

    }

    //Handles the clock.
    private void timeController() {
        new Clock(this);
    }


    //To set time label.
    public void setTimeLabel(String time) {
        timeLabel.setText(time);
    }



}
