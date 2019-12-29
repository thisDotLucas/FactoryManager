package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.*;
import view.AlertBox;


public class LogInViewController implements Viewable {

    @FXML
    private TextField userTextField;

    @FXML
    private TextField keyTextField;

    @FXML
    private Label timeLabel;

    @FXML
    private TextArea messageBox;


    //The user name and key are checked and the view will switch accordingly.
    @FXML
    public void logInPress() throws Exception {

        String user = userTextField.getText();
        String key = formatKey(keyTextField.getText());

        LogInCheck security = new LogInCheck(user, key);

            if(security.isEmployee() && security.status() == 1){ //Worker

                ViewNavigator.getInstance().setCurrentUser(DataMaps.getInstance().getEmployeeMap().get(key));
                ViewNavigator.getInstance().goToWorkerView();

            } else if(security.isEmployee() && security.status() == 0){ //Manager

                ViewNavigator.getInstance().setCurrentUser(DataMaps.getInstance().getEmployeeMap().get(key));
                ViewNavigator.getInstance().goToManagerView();

            } else { //Invalid user name or/and key
                new AlertBox("Invalid Credentials", 3);
            }

    }

    @FXML
    public void initialize(){
        messageBox.getStylesheets().add("view/DisabledMessageBox.css");
        userTextField.addEventFilter(KeyEvent.ANY, handler);
        keyTextField.addEventFilter(KeyEvent.ANY, handler);
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


    EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {

        private boolean willConsume = false;

        @Override
        public void handle(KeyEvent event) {

            if (willConsume)
                event.consume();


            if (event.getCode().isWhitespaceKey())
                willConsume = true;
            else
                willConsume = false;
        }

    };
}
