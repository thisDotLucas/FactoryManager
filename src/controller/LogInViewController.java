package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
     * The user and key inputs gets checked that they match with each other in the LogInCheck object and
     * the view will be loaded according to the employees access.
     */
    @FXML
    public void logInPress() throws Exception {

        String user = userTextField.getText();
        String key = keyTextField.getText();

        LogInCheck security = new LogInCheck(user, key);

        if (security.isEmployee() && security.status() == 1) { //Worker view

            ViewNavigator.getInstance().setCurrentUser(DataMaps.getInstance().getEmployeeMap().get(key));
            ViewNavigator.getInstance().goToWorkerView();

        } else if (security.isEmployee() && security.status() == 0) { //Manager view

            ViewNavigator.getInstance().setCurrentUser(DataMaps.getInstance().getEmployeeMap().get(key));
            ViewNavigator.getInstance().goToManagerView();

        } else { //Invalid user name or/and key
            new AlertBox("Invalid Credentials", 3);
        }

    }

    /**
     * This method is called on initialization. It disables the message box and sets handler to the
     * user and key text fields.
     */
    @FXML
    public void initialize() {
        messageBox.getStylesheets().add("view/DisabledMessageBox.css");
        userTextField.addEventFilter(KeyEvent.ANY, handler);
        keyTextField.addEventFilter(KeyEvent.ANY, handler);
        timeController();
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
