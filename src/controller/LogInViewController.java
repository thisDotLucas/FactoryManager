package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Clock;
import model.LogInCheck;
import model.Manager;
import model.Worker;
import view.AlertBox;


public class LogInViewController implements Viewable {

    @FXML
    private TextField userTextField;

    @FXML
    private TextField keyTextField;

    @FXML
    private Label timeLabel;


    //The user name and key are checked and the view will switch accordingly.
    @FXML
    public void logInPress() throws Exception {

        String user = userTextField.getText();
        String key = formatKey(keyTextField.getText());

        LogInCheck security = new LogInCheck(user, key);

            if(security.isEmployee() && security.status() == 1){ //Worker

                ViewNavigator.getInstance().setCurrentUser(new Worker(user, key));
                ViewNavigator.getInstance().goToWorkerView();

            } else if(security.isEmployee() && security.status() == 0){ //Manager

                ViewNavigator.getInstance().setCurrentUser(new Manager(user, key));
                ViewNavigator.getInstance().goToManagerView();

            } else { //Invalid user name or/and key
                new AlertBox("Invalid Credentials", 3);
            }

    }

    @FXML
    public void initialize(){
        timeController();
    }


    //Handles the clock.
    private void timeController(){
        new Clock(this);
    }


    //To set time label.
    public void setTimeLabel(String time){
        timeLabel.setText(time);
    }


    private String formatKey(String key) {
        try{
            return Integer.toString(Integer.parseInt(key));
        } catch (NumberFormatException e){
            return null;
        }
    }
}
