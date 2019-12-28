package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Clock;
import model.TableRowData;
import model.TimeAndDateHelper;
import model.Worker;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkerViewController implements Viewable, Initializable {

    private Worker user;
    private FxWorkerTableController userTable;
    private TableRowData step;

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
    private TableColumn<TableRowData, String> amountColumn;

    @FXML
    private TableColumn<TableRowData, String> endColumn;

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
    private TableColumn<TableRowData, String> trashColumn;

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
        step = new TableRowData(user.getUserKey());
        step.setStart_time(new TimeAndDateHelper().getTime());
        step.setWork_step_name("Checked In");
        addRow();

    }

    @FXML
    void checkOutPress() {
        user.logOut();
        user.stopWork();
        setNotLoggedInView();
    }

    @FXML
    void startPress() {
        user.startWork();
        user.setWorkStartTime(new TimeAndDateHelper().getTime());
        setWorkingView();
    }

    @FXML
    void endPress() {
        user.stopWork();
        user.setWorkStartTime(null);
        setNotWorkingView();
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
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end_time"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        trashColumn.setCellValueFactory(new PropertyValueFactory<>("trash"));
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

       rowData.add(step);
       table.setItems(rowData);

    }

}


