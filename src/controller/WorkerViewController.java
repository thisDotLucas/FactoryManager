package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import view.AlertBox;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class WorkerViewController implements Viewable, Initializable {

    private Worker user;  //Logged in user.
    private FxWorkerTableController userTable; //not in use.
    private LinkedList<Message> messages;
    private int messageBoxPointer;

    private ObservableList<TableRowData> rowData = FXCollections.observableArrayList(); //Row data to add on init.

    @FXML
    private TextField userTextField;

    @FXML
    private Label userLabel;

    @FXML
    private Label accessLabel;

    @FXML
    private Button logOutButton;

    @FXML
    private Button previousMsgButton;

    @FXML
    private Button nextMsgButton;

    @FXML
    private Button deleteMsgButton;

    @FXML
    private TextField keyTextField;

    @FXML
    private Label timeLabel;

    @FXML
    private Label msgSenderLabel;

    @FXML
    private Label msgAmountLabel;

    @FXML
    private Label dateTimeRecivedLabel;

    @FXML
    private TextArea messageBox;

    @FXML
    private TableView<TableRowData> table;

    @FXML
    private ComboBox<String> reasonComboBox;

    @FXML
    private Button endButton;

    @FXML
    private TextField trashTextField;

    @FXML
    private Button startButton;

    @FXML
    private Button checkOutButton;

    @FXML
    private Button checkInButton;

    @FXML
    private TableColumn<TableRowData, Integer> amountColumn;

    @FXML
    private TextField workNrTextField;

    @FXML
    private TextField amountTextField;

    @FXML
    private TableColumn<TableRowData, String> productivityColumn;

    @FXML
    private TableColumn<TableRowData, String> workNameColumn;

    @FXML
    private TableColumn<TableRowData, String> startColumn;

    @FXML
    private TableColumn<TableRowData, String> workerColumn;

    @FXML
    private TableColumn<TableRowData, Integer> trashColumn;

    @FXML
    private TableColumn<TableRowData, String> workNrColumn;

    @FXML
    private TableColumn<TableRowData, String> dateColumn;

    @FXML
    private TableColumn<TableRowData, String> reasonColumn;


    @FXML
    void logOutPress() throws Exception{ //User logs out, view goes to log in view.

        ViewNavigator.getInstance().goToLogInView();

    }

    @FXML
    void previousMsgPress() {

        messageBoxPointer--;

        if(messageBoxPointer >= 0) {

            showMessage();

        } else {
            messageBoxPointer = 0;
        }
        setButtons();
    }

    @FXML
    void deleteMsgPress() {

        if(messages.size() != 0){
            MySqlDatabase.getInstance().deleteMessage(messages.get(messageBoxPointer));
            messages.remove(messageBoxPointer);
            msgAmountLabel.setText(Integer.toString(messages.size()));
        } else {
            return;
        }

        if(messages.size() == 0){
            messageBox.setText("");
            msgSenderLabel.setText("");
            dateTimeRecivedLabel.setText("");
            messageBoxPointer = -1;
        } else if(messageBoxPointer - 1 >= 0){
            messageBox.setText(messages.get(--messageBoxPointer).getMessage());
            msgSenderLabel.setText(DataMaps.getInstance().getEmployeeMap().get(messages.get(messageBoxPointer).getSender()).getUserName());
            dateTimeRecivedLabel.setText(messages.get(messageBoxPointer).getTimeStamp());
        } else {
            showMessage();
        }
        setButtons();
    }

    private void setButtons(){
        if(messages.size() <= 0){
            previousMsgButton.setDisable(true);
            deleteMsgButton.setDisable(true);
            nextMsgButton.setDisable(true);
        } else if(messageBoxPointer == messages.size() - 1 && messageBoxPointer > 0){
            previousMsgButton.setDisable(false);
            deleteMsgButton.setDisable(false);
            nextMsgButton.setDisable(true);
        } else if(messageBoxPointer - 1 < 0 && messages.size() > 1){
            previousMsgButton.setDisable(true);
            deleteMsgButton.setDisable(false);
            nextMsgButton.setDisable(false);
        } else if(messageBoxPointer + 1 < messages.size() && messageBoxPointer - 1 >= 0){
            previousMsgButton.setDisable(false);
            deleteMsgButton.setDisable(false);
            nextMsgButton.setDisable(false);
        } else {
            previousMsgButton.setDisable(true);
            deleteMsgButton.setDisable(false);
            nextMsgButton.setDisable(true);
        }
    }

    @FXML
    void nextMsgPress() {

        messageBoxPointer++;

        if(messageBoxPointer < messages.size()) {

          showMessage();

        } else {
            messageBoxPointer = messages.size() - 1;
        }
        setButtons();
    }

    private void showMessage(){
        messageBox.setText(messages.get(messageBoxPointer).getMessage());
        msgSenderLabel.setText(DataMaps.getInstance().getEmployeeMap().get(messages.get(messageBoxPointer).getSender()).getUserName());
        dateTimeRecivedLabel.setText(messages.get(messageBoxPointer).getTimeStamp());
    }

    @FXML
    void callManagerPress() { //Call manager button is pressed, notification is created in data base
        MySqlDatabase.getInstance().addNotification(user.getUserKey(), "0389");
        new AlertBox("Notification Sent.", 0);
    }

    @FXML
    void checkinPress() { //User checks in for the day, not working view will be set. Row data also added to table and database.
        user.logIn(); //Updates user status
        setNotWorkingView();
        user.setCurrentWorkStep(new TableRowData(user.getUserKey()));
        user.getCurrentWorkStep().setTime(new TimeAndDateHelper().getTime());
        user.getCurrentWorkStep().setWork_step_name("Check in");
        user.getCurrentWorkStep().setWork_id("00000");
        addRow();
        user.setCurrentWorkStep(null);
    }

    @FXML
    void checkOutPress() { //User checks out, not logged in view will be set. Row data also added to table and database.
        user.logOut();
        user.stopWork();
        setNotLoggedInView();
        user.setCurrentWorkStep(new TableRowData(user.getUserKey()));
        user.getCurrentWorkStep().setTime(new TimeAndDateHelper().getTime());
        user.getCurrentWorkStep().setWork_step_name("Check out");
        user.getCurrentWorkStep().setWork_id("99999");
        addRow();
        user.setCurrentWorkStep(null);
    }

    @FXML
    void startPress() { //User starts work step, row is added in table and database.
        IOHelper helper = new IOHelper(); //Checks work number input.
        String stepName = null;
        String workNumber = workNrTextField.getText();
        if(helper.isConvertibleToInteger(workNumber))
            stepName = DataMaps.getInstance().getWorkStepsMap().get(Integer.parseInt(workNumber));

        if(stepName != null){
            user.setCurrentWorkStep(new TableRowData(user.getUserKey()));
            user.getCurrentWorkStep().setTime(new TimeAndDateHelper().getTime());
            user.getCurrentWorkStep().setWork_step_name(stepName);
            user.getCurrentWorkStep().setWork_id(workNumber);
            user.setCurrentWorkNr(workNumber);
            user.startWork();
            setWorkingView();
            addRow();
        } else {
            new AlertBox("Invalid work number.", 2);
        }


    }

    @FXML
    void endPress() { //User ends current work step, row is added to table and database.
        IOHelper helper = new IOHelper();
        String amount = amountTextField.getText();
        String trashAmount = trashTextField.getText();
        if (helper.isConvertibleToInteger(amount) && helper.isConvertibleToInteger(trashAmount) && !helper.isNegative(Integer.parseInt(amount)) && !helper.isNegative(Integer.parseInt(trashAmount))) {
            if (reasonComboBox.getValue() != null) {

                TableRowData endStep = new TableRowData(user.getUserKey());
                endStep.setWork_id(user.getCurrentWorkStep().getWork_id());
                endStep.setWork_step_name(user.getCurrentWorkStep().getWork_step_name());
                endStep.setTime(new TimeAndDateHelper().getTime());
                endStep.setAmount_done(amount);
                endStep.setTrash_amount(trashAmount);
                endStep.setProductivity(user.calculateProductivity(user.getCurrentWorkStep().getWork_id(), Integer.parseInt(amount), user.getCurrentWorkStep().getTime(), endStep.getTime()));
                if(Integer.parseInt(trashAmount) == 0) {
                    endStep.setReason("");
                } else
                    endStep.setReason(reasonComboBox.getValue());
                user.setCurrentWorkStep(endStep);
                user.stopWork();
                setNotWorkingView();
                addRow();
                workNrTextField.setText("");
                amountTextField.setText("");
                trashTextField.setText("");
                reasonComboBox.setValue("");

                user.setCurrentWorkNr("");
                user.setCurrentWorkStep(null);
            } else {
                new AlertBox("Choose a reason for the trash amount.", 2);
                return;
            }
        } else {
            new AlertBox("Enter a valid number in the amount and trash field.", 2);
            return;
        }

    }

    @FXML
    void onReasonComboBox(){

    }


    private void onReasonComboBoxClicked(){ //Gives only empty options whne trash amount is 0
        IOHelper helper = new IOHelper();

        if(helper.isConvertibleToInteger(trashTextField.getText()) && !helper.isNegative(Integer.parseInt(trashTextField.getText())) && Integer.parseInt(trashTextField.getText()) > 0) {
            reasonComboBox.setItems(FXCollections.observableArrayList(
                    "Broken part", "Accident", "Failed test", "Other"
            ));

        } else {
            reasonComboBox.setItems(FXCollections.observableArrayList("","","",""));
        }

    }



    @FXML
    public void initialize(URL location, ResourceBundle resources){ //Initializes everything.
        user = (Worker)ViewNavigator.getInstance().getLoggedInUser();
        user.setTableController(table);
        userTable = user.getTableController();
        messageBox.setEditable(false);
        messageBox.getStylesheets().add("view/DisabledMessageBox.css");
        messages = MySqlDatabase.getInstance().getMessages(user.getUserKey());
        msgAmountLabel.setText(Integer.toString(messages.size()));
        if(messages.size() > 0) {
            messageBox.setText(messages.getFirst().getMessage());
            msgSenderLabel.setText(DataMaps.getInstance().getEmployeeMap().get(messages.getFirst().getSender()).getUserName());
            dateTimeRecivedLabel.setText(messages.getFirst().getTimeStamp());
            messageBoxPointer = 0;
        }
        setButtons();
        //table.setMouseTransparent(true);
        table.getStylesheets().add("view/hideScrollbar.css");
        reasonComboBox.setOnMouseClicked(event -> onReasonComboBoxClicked());
        timeController();
        customizeView();
        rowData.addAll(MySqlDatabase.getInstance().getWorkSteps(new TimeAndDateHelper().getDate(), user.getUserKey()));
        table.setItems(rowData);

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

    private void customizeView(){ //Checks what view is needed based on booleans from loggen in user.

        userLabel.setText(user.getUserName());
        userTextField.setDisable(true);
        workNrTextField.setText(user.getCurrentWorkNr());
        keyTextField.setDisable(true);

        if(!user.isLoggedIn()){ //Not logged in.
            setNotLoggedInView();
        } else if(!user.isWorking()){ //Not currently working on anything.
            setNotWorkingView();
        } else { //Currently working.
            setWorkingView();
        }

    }


    private void setNotLoggedInView(){ //Sets the view.
        checkOutButton.setDisable(true);
        startButton.setDisable(true);
        endButton.setDisable(true);
        workNrTextField.setDisable(true);
        amountTextField.setDisable(true);
        trashTextField.setDisable(true);
        reasonComboBox.setDisable(true);
        checkInButton.setDisable(false);

    }


    private void setNotWorkingView(){ //Sets the view.
        checkInButton.setDisable(true);
        amountTextField.setDisable(true);
        endButton.setDisable(true);
        trashTextField.setDisable(true);
        reasonComboBox.setDisable(true);
        checkOutButton.setDisable(false);
        startButton.setDisable(false);
        workNrTextField.setDisable(false);
    }


    private void setWorkingView(){ //Sets the view.
        checkInButton.setDisable(true);
        startButton.setDisable(true);
        workNrTextField.setDisable(true);
        endButton.setDisable(false);
        checkOutButton.setDisable(false);
        amountTextField.setDisable(false);
        trashTextField.setDisable(false);
        reasonComboBox.setDisable(false);
    }


    //Handles the clock.
    private void timeController(){
        new Clock(this);
    }


    //To set time label.
    public void setTimeLabel(String time){
        timeLabel.setText(time);
    }

    private void addRow(){ //Adds a row, always called when the worker does something.

       rowData.add(user.getCurrentWorkStep());
       table.setItems(rowData);
       MySqlDatabase.getInstance().addWorkStep(user.getCurrentWorkStep());

    }





}


