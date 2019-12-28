package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Clock;
import model.Worker;

public class WorkerViewController implements Viewable{

    private Worker user;

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
    private TableView<?> table;

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
    private TextField workNrTextField;

    @FXML
    private TextField amountTextField;


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
    }

    @FXML
    void checkOutPress() {
        user.logOut();
        setNotLoggedInView();
    }

    @FXML
    void startPress() {
        user.startWork();
        setWorkingView();
    }

    @FXML
    void endPress() {
        user.stopWork();
        setNotWorkingView();
    }

    @FXML
    public void initialize(){
        user = (Worker)ViewNavigator.getInstance().getLoggedInUser();
        messageBox.setEditable(false);
        messageBox.getStylesheets().add("view/DisabledMessageBox.css");
        timeController();
        customizeView();
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

}
