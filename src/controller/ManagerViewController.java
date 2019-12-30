package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import view.AlertBox;

import java.util.ArrayList;
import java.util.Map;

public class ManagerViewController implements Viewable{

    Manager user;

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
    private TextArea messageBox;

    @FXML
    private ComboBox<String> receiverComboBox;

    @FXML
    private Button sendButton;

    @FXML
    private TableView<?> table;

    @FXML
    void logOutPress() throws Exception {
        ViewNavigator.getInstance().goToLogInView();

    }

    @FXML
    void sendPress(){
        String msg = messageBox.getText();
        MySqlDatabase.getInstance().sendMessage(user.getUserKey(), DataMaps.getInstance().getNameKeyMap().get(receiverComboBox.getValue()), msg, new TimeAndDateHelper().getTimeAndDate());
        new AlertBox("Message Sent.", 0);
        messageBox.clear();
    }

    @FXML
    void onReceiverComboBox(){
        sendButton.setDisable(false);
    }

    @FXML
    public void initialize() {
        user = (Manager) ViewNavigator.getInstance().getLoggedInUser();
        timeController();
        receiverComboBox.setItems(DataMaps.getInstance().getWorkerNames());
        userLabel.setText(user.getUserName());
        userTextField.setDisable(true);
        keyTextField.setDisable(true);
        sendButton.setDisable(true);
        showNotifications();
    }



    private void showNotifications(){
        ArrayList<String> senders = MySqlDatabase.getInstance().getNotifications(user.getUserKey());
        IOHelper helper = new IOHelper();
        Map<String, Employee> employeeMap = DataMaps.getInstance().getEmployeeMap();
        String stringToBeShown = "";

        for(String sender : senders){
            stringToBeShown += helper.capitalizeFirsChar(employeeMap.get(sender).getUserName()) + " Needs Assistance.\n";
        }

        if(!stringToBeShown.equals("")) {
            new AlertBox(stringToBeShown, "Notification", 0);
            MySqlDatabase.getInstance().deleteNotifications(user.getUserKey());
        }

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
