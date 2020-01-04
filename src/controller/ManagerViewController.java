package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.*;
import view.AlertBox;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class ManagerViewController implements Viewable {

    private Manager user; //Logged in user.
    private TableRowData selectedRow; //TableDataRow currently selected by user.


    @FXML
    private TextField userTextField;

    @FXML
    private Label userLabel;

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
    private Button deleteRowButton;

    @FXML
    private Button editRowButton;

    @FXML
    private Button addRowButton;

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


    /**
     * This method is called when the user logs out. The stage will switch to the log in view.
     * Fires on Log out button press.
     */
    @FXML
    void logOutPress() throws Exception {
        ViewNavigator.getInstance().goToLogInView();
    }

    /**
     * This method is called when the user clicks the Del row button. The selected row will
     * be deleted from the database and the table view will be updated.
     * Fires on Del row button press.
     */
    @FXML
    void onDeleteRowPress(){

        MySqlDatabase.getInstance().deleteWorkStep(selectedRow);
        updateTable();
        selectedRow = null;

    }

    /**
     * This method is called when the user clicks the Edit row button. This will open the editAddView
     * where the user then is able to edit values in the selected row. The database and table view will update.
     * Fires on Edit row press.
     */
    @FXML
    void onEditRowPress() throws IOException {

        EditAddRowHelper.getInstance().editRow(selectedRow);
        EditAddRowHelper.getInstance().setController(this);
        setEditStage("Edit");

    }

    /**
     * This method is called when the user clicks the Add row button. This will open the editAddView
     * where the user can create a new row to be added to the database and table view.
     * Fires on Add row press.
     */
    @FXML
    void onAddRowPress() throws IOException {

        EditAddRowHelper.getInstance().addRow(DataMaps.getInstance().getNameKeyMap().get(workerComboBox.getValue()), new TimeAndDateHelper().formatDate(datePicker.getValue()));
        EditAddRowHelper.getInstance().setController(this);
        setEditStage("Add");

    }

    /**
     * This method is called when the date in the date picker is changed. The correct data will be fetched from
     * the database and inserted into the table view.
     */
    @FXML
    void onDateChanged(){
        updateTable();
    }

    /**
     * This method is called when the date in the date picker is changed. The correct data will be fetched from
     * the database and inserted into the table view.
     */
    @FXML
    void onWorkerChanged(){
        updateTable();
    }

    /**
     * This method sends a message to the chosen receiver and stores it in the database. Messages cannot be empty.
     * The message box and the receiver combo box will be emptied.
     */
    @FXML
    void sendPress() {

        if (!messageBox.getText().equals("")) {

            String msg = messageBox.getText();
            MySqlDatabase.getInstance().sendMessage(user.getUserKey(), DataMaps.getInstance().getNameKeyMap().get(receiverComboBox.getValue()), msg, new TimeAndDateHelper().getTimeAndDate());
            new AlertBox("Message Sent.", 0);
            messageBox.clear();
            receiverComboBox.setValue("");

        } else
            new AlertBox("Message box is empty.", 3);
    }

    /**
     * This method enables the send button when a receiver is chosen.
     */
    @FXML
    void onReceiverComboBox() {
        sendButton.setDisable(false);
    }

    /**
     * This method is called on initialization. The logged in user is stored into a variable. JavaFx
     * control properties are set and/or given listeners.
     */
    @FXML
    public void initialize() {

        user = (Manager) ViewNavigator.getInstance().getLoggedInUser();

        receiverComboBox.setItems(DataMaps.getInstance().getWorkerNames());
        workerComboBox.setItems(DataMaps.getInstance().getWorkerNames());

        datePicker.setValue(LocalDate.from(LocalDateTime.now()));
        datePicker.setEditable(false);

        userLabel.setText(user.getUserName());

        userTextField.setDisable(true);
        keyTextField.setDisable(true);
        sendButton.setDisable(true);

        initDatePicker();
        initTable();
        timeController();
        showNotifications();
        updateButtons();

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        workNrColumn.setCellValueFactory(new PropertyValueFactory<>("work_id"));
        workerColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount_done"));
        trashColumn.setCellValueFactory(new PropertyValueFactory<>("trash_amount"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        productivityColumn.setCellValueFactory(new PropertyValueFactory<>("productivity"));
        workNameColumn.setCellValueFactory(new PropertyValueFactory<>("work_step_name"));

        messageBox.setWrapText(true);
        messageBox.textProperty().addListener(l -> {  //Character amount limit is set to 800.
                if (messageBox.getText().length() > 800) {
                    String s = messageBox.getText().substring(0, 800);
                    messageBox.setText(s);
                }
        });
    }

    /**
     * This makes the selectedRow variable update to the clicked on row in the table view.
     */
    private void initTable() {

        this.table.setRowFactory(e -> {
            TableRow<TableRowData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY && event.getClickCount() > 0) {

                    selectedRow = row.getItem();
                    updateButtons();

                }
            });
            return row;
        });

    }

    /**
     * This sets the format for the dates and makes future dates unavailable.
     */
    private void initDatePicker() {

        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        });

    }


    /**
     * This method fetches work step data from the database according to the date picker value and the chosen worker.
     */
    void updateTable() {

        ObservableList<TableRowData> rowData;
        rowData = MySqlDatabase.getInstance().getWorkSteps(new TimeAndDateHelper().formatDate(datePicker.getValue()), DataMaps.getInstance().getNameKeyMap().get(workerComboBox.getValue()));
        table.setItems(rowData);
        updateButtons();

    }

    /**
     * This method gets the notifications for the current user from the database and shows them in an alert box
     * and then deleted from the database.
     */
    private void showNotifications() {
        ArrayList<String> senders = MySqlDatabase.getInstance().getNotifications(user.getUserKey());
        IOHelper helper = new IOHelper();
        Map<String, Employee> employeeMap = DataMaps.getInstance().getEmployeeMap();
        StringBuilder stringToBeShown = new StringBuilder();

        for (String sender : senders) {
            stringToBeShown.append(helper.capitalizeFirsChar(employeeMap.get(sender).getUserName()));
            stringToBeShown.append(" Needs Assistance.\n");
        }

        if (!stringToBeShown.toString().equals("")) {
            new AlertBox(stringToBeShown.toString(), "Notification", 0);
            MySqlDatabase.getInstance().deleteNotifications(user.getUserKey());
        }

    }

    /**
     * This method updates which of the 3 row buttons should be enabled. If no worker is chosen all buttons are disabled (0),
     * if no row is selected only the add row button is enabled (1) else all buttons are enabled (2).
     */
    private void updateButtons(){

        if(workerComboBox.getValue() == null){ //(0)
            deleteRowButton.setDisable(true);
            editRowButton.setDisable(true);
            addRowButton.setDisable(true);
        } else if(workerComboBox.getValue() != null && selectedRow == null) { //(1)
            deleteRowButton.setDisable(true);
            editRowButton.setDisable(true);
            addRowButton.setDisable(false);
        } else { //(2)
            deleteRowButton.setDisable(false);
            editRowButton.setDisable(false);
            addRowButton.setDisable(false);
        }
    }

    /**
     * Loads the editAddView and sets the stage properties.
     */
    private void setEditStage(String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/editAddView.fxml"));
        Stage stage = new Stage();
        stage.setMaxWidth(316);
        stage.setMaxHeight(480);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setOnHiding(e -> Platform.runLater( () -> EditAddRowHelper.getInstance().reset()));
        stage.show();
    }


    /**
     * Activates the clock.
     */
    private void timeController() {
        new Clock(this);
    }

    public void setTimeLabel(String time) {
        timeLabel.setText(time);
    }

    public ManagerViewController getController(){
        return this;
    }

    public TableView<TableRowData> getTable(){
        return table;
    }


    public TableRowData getSelectedRow(){
        return selectedRow;
    }

}
