package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Clock;

public class ManagerViewController implements Viewable{

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
    private TextArea messeageBox;

    @FXML
    private TableView<?> table;

    @FXML
    void logOutPress() throws Exception {
        ViewNavigator.getInstance().goToLogInView();

    }

    @FXML
    public void initialize(){
        userLabel.setText(ViewNavigator.getInstance().getLoggedInUser().getUserName());
        timeController();
        userTextField.setDisable(true);
        keyTextField.setDisable(true);
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
