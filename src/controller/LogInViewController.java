package controller;

import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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


    /**
     * Message and notification maps gets updated in DataMaps.
     */
    @FXML
    private void onUserTextFieldClick(){
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                DataMaps.getInstance().prepareNotificationsAndMessages();
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    private Button logInButton;


    /**
     * The user and key inputs gets checked that they match with each other in the LogInCheck object and
     * the view will be loaded according to the employees access.
     */
    @FXML
    public void logInPress() throws Exception {

        String user = userTextField.getText();
        String key = keyTextField.getText();

        LogInCheck security = new LogInCheck(user, key);

        try {
            if (security.isEmployee() && security.status() == 1) { //Worker view

                ViewNavigator.getInstance().setCurrentUser(DataMaps.getInstance().getEmployeeMap().get(key));
                ViewNavigator.getInstance().goToWorkerView();

            } else if (security.isEmployee() && security.status() == 0) { //Manager view

                ViewNavigator.getInstance().setCurrentUser(DataMaps.getInstance().getEmployeeMap().get(key));
                ViewNavigator.getInstance().goToManagerView();

            } else { //Invalid user name or/and key
                new AlertBox("Invalid Credentials", 3);
            }
        } catch (Exception e){
            e.printStackTrace();
            new AlertBox("Unexpected error! Try again.", 3);
        }
    }
    /**
     * This method is called on initialization. It disables the message box and sets handler to the
     * user and key text fields. Work step map in DataMaps gets updated.
     */
    @FXML
    public void initialize() {
        messageBox.getStylesheets().add("view/DisabledMessageBox.css");
        userTextField.setFocusTraversable(false);
        keyTextField.setFocusTraversable(false);
        logInButton.setFocusTraversable(false);
        userTextField.addEventFilter(KeyEvent.ANY, handler);
        keyTextField.addEventFilter(KeyEvent.ANY, handler);
        timeController();
        Task task = new Task(){
            @Override
            protected Object call() throws Exception {
                DataMaps.getInstance().prepareWorkSteps();
                return null;
            }
        };
        new Thread(task).start();
    }

    /**
     * This handler prevents whitespaces from the user and key text fields.
     */
    private EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {

        private boolean willConsume = false;

        @Override
        public void handle(KeyEvent event) {

            if(willConsume)
                event.consume();


            if(event.getCode().isWhitespaceKey())
                willConsume = true;
            else
                willConsume = false;
        }

    };

    /**
     * Activates the clock.
     */
    private void timeController() {
        new Clock(this);
    }


    public void setTimeLabel(String time) {
        timeLabel.setText(time);
    }

}
