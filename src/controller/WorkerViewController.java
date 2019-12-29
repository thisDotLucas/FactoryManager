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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class WorkerViewController implements Viewable, Initializable {

    private Worker user;
    private FxWorkerTableController userTable;

    private ObservableList<TableRowData> rowData = FXCollections.observableArrayList();

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
    private ComboBox<?> reasonComboBox;

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
    void logOutPress() throws Exception{
        ViewNavigator.getInstance().goToLogInView();

    }

    @FXML
    void previousMsgPress() {

    }

    @FXML
    void deleteMsgPress() {

    }

    @FXML
    void nextMsgPress() {

    }

    @FXML
    void callManagerPress() {

    }

    @FXML
    void checkinPress() {
        user.logIn();
        setNotWorkingView();
        user.setCurrentWorkStep(new TableRowData(user.getUserKey()));
        user.getCurrentWorkStep().setTime(new TimeAndDateHelper().getTime());
        user.getCurrentWorkStep().setWork_step_name("Check in");
        user.getCurrentWorkStep().setWork_id("00000");
        addRow();
        user.setCurrentWorkStep(null);
    }

    @FXML
    void checkOutPress() {
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
    void startPress() {
        IOHelper helper = new IOHelper();
        String stepName = null;
        String workNumber = workNrTextField.getText();
        if(helper.isConvertibleToInteger(workNumber))
            stepName = DataMaps.getInstance().getWorkStepsMap().get(Integer.parseInt(workNumber));

        if(stepName != null){
            user.setCurrentWorkStep(new TableRowData(user.getUserKey()));
            user.getCurrentWorkStep().setTime(new TimeAndDateHelper().getTime());
            user.getCurrentWorkStep().setWork_step_name(stepName);
            user.getCurrentWorkStep().setWork_id(workNumber);
            user.startWork();
            setWorkingView();
            addRow();
        } else {
            new AlertBox("Invalid work number.", 2);
        }


    }

    @FXML
    void endPress() {
        IOHelper helper = new IOHelper();
        String amount = amountTextField.getText();
        String trashAmount = trashTextField.getText();
        if (helper.isConvertibleToInteger(amount) && helper.isConvertibleToInteger(trashAmount) && !helper.isNegative(Integer.parseInt(amount)) && !helper.isNegative(Integer.parseInt(trashAmount))) {
            TableRowData endStep = new TableRowData(user.getUserKey());
            endStep.setWork_id(user.getCurrentWorkStep().getWork_id());
            endStep.setWork_step_name(user.getCurrentWorkStep().getWork_step_name());
            endStep.setTime(new TimeAndDateHelper().getTime());
            endStep.setAmount_done(amount);
            endStep.setTrash_amount(trashAmount);
            endStep.setProductivity(user.calculateProductivity(user.getCurrentWorkStep().getWork_id(), Integer.parseInt(amount), user.getCurrentWorkStep().getTime(), endStep.getTime()));
            user.setCurrentWorkStep(endStep);
            user.stopWork();
            setNotWorkingView();
            addRow();
            workNrTextField.setText("");
            amountTextField.setText("");
            trashTextField.setText("");

            user.setCurrentWorkStep(null);
        } else {
            new AlertBox("Enter a valid number in the amount and trash field.", 2);
            return;
        }

    }



    @FXML
    public void initialize(URL location, ResourceBundle resources){
        user = (Worker)ViewNavigator.getInstance().getLoggedInUser();
        user.setTableController(table);
        userTable = user.getTableController();
        messageBox.setEditable(false);
        messageBox.getStylesheets().add("view/DisabledMessageBox.css");
        timeController();
        customizeView();

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        workNrColumn.setCellValueFactory(new PropertyValueFactory<>("work_id"));
        workerColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount_done"));
        trashColumn.setCellValueFactory(new PropertyValueFactory<>("trash_amount"));
        productivityColumn.setCellValueFactory(new PropertyValueFactory<>("productivity"));
        workNameColumn.setCellValueFactory(new PropertyValueFactory<>("work_step_name"));
    }

    private void customizeView(){

        userLabel.setText(user.getUserName());
        userTextField.setDisable(true);
        keyTextField.setDisable(true);

        if(!user.isLoggedIn()){ //Not logged in.
            setNotLoggedInView();
        } else if(!user.isWorking()){ //Not currently working on anything.
            setNotWorkingView();
        } else { //Currently working.
            setWorkingView();
        }

    }


    private void setNotLoggedInView(){
        checkOutButton.setDisable(true);
        startButton.setDisable(true);
        endButton.setDisable(true);
        workNrTextField.setDisable(true);
        amountTextField.setDisable(true);
        trashTextField.setDisable(true);
        reasonComboBox.setDisable(true);
        checkInButton.setDisable(false);

    }


    private void setNotWorkingView(){
        checkInButton.setDisable(true);
        amountTextField.setDisable(true);
        endButton.setDisable(true);
        trashTextField.setDisable(true);
        reasonComboBox.setDisable(true);
        checkOutButton.setDisable(false);
        startButton.setDisable(false);
        workNrTextField.setDisable(false);
    }


    private void setWorkingView(){
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

    private void addRow(){

       rowData.add(user.getCurrentWorkStep());
       table.setItems(rowData);

    }





}


