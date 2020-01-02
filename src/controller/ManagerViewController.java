package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
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

    private ManagerViewController controller;
    Manager user;
    TableRowData selectedRow;


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


    @FXML
    void logOutPress() throws Exception {
        ViewNavigator.getInstance().goToLogInView();
    }

    @FXML
    void onDeleteRowPress(){

        MySqlDatabase.getInstance().deleteWorkStep(selectedRow);
        selectedRow = null;
        updateTable();
    }

    @FXML
    void onEditRowPress() throws IOException {
        EditAddRowHelper.getInstance().editRow(selectedRow);
        EditAddRowHelper.getInstance().setController(this);
        setEditStage("Edit");

    }

    @FXML
    void onAddRowPress() throws IOException {
        EditAddRowHelper.getInstance().addRow(DataMaps.getInstance().getNameKeyMap().get(workerComboBox.getValue()), new TimeAndDateHelper().formatDate(datePicker.getValue()));
        EditAddRowHelper.getInstance().setController(this);
        setEditStage("Add");
    }

    @FXML
    void onDateChanged(){
        updateTable();
    }

    @FXML
    void onWorkerChanged(){
        updateTable();
    }

    public void updateTable() {

        ObservableList<TableRowData> rowData;
        rowData = MySqlDatabase.getInstance().getWorkSteps(new TimeAndDateHelper().formatDate(datePicker.getValue()), DataMaps.getInstance().getNameKeyMap().get(workerComboBox.getValue()));
        table.setItems(rowData);
        updateButtons();

    }

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
        initDatePicker();
        initTable();
        datePicker.setValue(LocalDate.from(LocalDateTime.now()));
        datePicker.setEditable(false);

        userLabel.setText(user.getUserName());
        userTextField.setDisable(true);
        keyTextField.setDisable(true);
        sendButton.setDisable(true);
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
        messageBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (messageBox.getText().length() > 800) {
                    String s = messageBox.getText().substring(0, 800);
                    messageBox.setText(s);
                }
            }
        });
    }

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

    private void initDatePicker() {

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

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
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

    private void updateButtons(){

        if(workerComboBox.getValue() == null){
            deleteRowButton.setDisable(true);
            editRowButton.setDisable(true);
            addRowButton.setDisable(true);
        } else if(workerComboBox.getValue() != null && selectedRow == null) {
            deleteRowButton.setDisable(true);
            editRowButton.setDisable(true);
            addRowButton.setDisable(false);
        } else {
            deleteRowButton.setDisable(false);
            editRowButton.setDisable(false);
            addRowButton.setDisable(false);
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

    private void setEditStage(String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/editView.fxml"));
        Stage stage = new Stage();
        stage.setMaxWidth(316);
        stage.setMaxHeight(480);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        EditAddRowHelper.getInstance().reset();
                    }
                });

            }
        });
        stage.show();
    }

    public ManagerViewController getController(){
        return controller;
    }

    public TableRowData getSelectedRow(){
        return selectedRow;
    }

}
