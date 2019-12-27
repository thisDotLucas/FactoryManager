package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ManagerViewController {

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
        userLabel.setText(ViewNavigator.getInstance().getLoggedInUser());
        userTextField.setDisable(true);
        keyTextField.setDisable(true);
    }


}
