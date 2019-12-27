package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Clock;
import model.LogInCheck;
import view.AlertBox;


public class LogInViewController implements Viewable {

    @FXML
    private TextField userTextField;

    @FXML
    private TextField keyTextField;

    @FXML
    private Button logInButton;

    @FXML
    private Label timeLabel;

    @FXML
    private TextArea messageBox;

    @FXML
    private TableView<?> table;

    @FXML
    public void logInPress(ActionEvent event) {

        String user = userTextField.getText();
        String key = keyTextField.getText();

        LogInCheck security = new LogInCheck(user, key);

        try {
            if(security.isEmployee() && security.status() == 1){
                    ViewController.getInstance().goToWorkerView();
            } else if(security.isEmployee() && security.status() == 0){
                ViewController.getInstance().goToManagerView();
            } else {
                new AlertBox("Invalid credentials", 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize(){
        timeController();
    }

    private void timeController(){
        new Clock(this);
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(String time){
        timeLabel.setText(time);
    }
}
