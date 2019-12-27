package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class WorkerViewController {

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
    private TextArea messeageBox;

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
        userLabel.setText(ViewNavigator.getInstance().getLoggedInUser().getUserName());
        userTextField.setDisable(true);
        keyTextField.setDisable(true);
    }

}
