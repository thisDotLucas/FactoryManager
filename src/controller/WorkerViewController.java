package controller;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import model.Clock;
import model.Worker;

public class WorkerViewController implements Viewable{

    Worker user;

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

    }

    @FXML
    void checkOutPress() {

    }

    @FXML
    void startPress() {

    }

    @FXML
    void endPress() {

    }

    @FXML
    public void initialize(){
        user = (Worker)ViewNavigator.getInstance().getLoggedInUser();
        messageBox.setEditable(false);
        messageBox.getStylesheets().add("view/MessageBox.css");
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

    }


    private void setNotWorkingView(){

    }


    private void setWorkingView(){

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
