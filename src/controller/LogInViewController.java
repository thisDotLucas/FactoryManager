package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import view.AlertBox;


public class LogInViewController {

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

        System.out.println(user);
        System.out.println(key);

    }
}
