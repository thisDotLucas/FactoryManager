package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private Label timeLabel;

    @FXML
    public void logInPress() {

        String user = userTextField.getText();
        String key = keyTextField.getText();

        LogInCheck security = new LogInCheck(user, key);

        try {
            if(security.isEmployee() && security.status() == 1){
                ViewNavigator.getInstance().setCurrentUser(user);
                ViewNavigator.getInstance().goToWorkerView();
            } else if(security.isEmployee() && security.status() == 0){
                ViewNavigator.getInstance().setCurrentUser(user);
                ViewNavigator.getInstance().goToManagerView();
            } else {
                new AlertBox("Invalid Credentials", 3);
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

    public void setTimeLabel(String time){
        timeLabel.setText(time);
    }
}
