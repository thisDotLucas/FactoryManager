package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import view.AlertBox;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class WorkerViewController implements Viewable, Initializable {

    private Worker user;  //Logged in user.
    private LinkedList<Message> messages; //Message from database, set on initialization.
    private int messageBoxPointer; //Points on currently shown message.

    private ObservableList<TableRowData> rowData = FXCollections.observableArrayList(); //Data displayed in table.

    @FXML
    private TextField userTextField;

    @FXML
    private Label userLabel;

    @FXML
    private Button previousMsgButton;

    @FXML
    private Button nextMsgButton;

    @FXML
    private Button deleteMsgButton;

    @FXML
    private TextField keyTextField;

    @FXML
    private Label timeLabel;

    @FXML
    private Label msgSenderLabel;

    @FXML
    private Label msgAmountLabel;

    @FXML
    private Label dateTimeReceivedLabel;

    @FXML
    private TextArea messageBox;

    @FXML
    private TableView<TableRowData> table;

    @FXML
    private ComboBox<String> reasonComboBox;

    @FXML
    private Button endButton;

    @FXML
    private TextField trashTextField;

    @FXML
    private Button startButton;

    @FXML
    private Button checkOutButton;

    @FXML
    private Button checkInButton;

    @FXML
    private TableColumn<TableRowData, Integer> amountColumn;

    @FXML
    private TextField workNrTextField;

    @FXML
    private TextField amountTextField;

    @FXML
    private TableColumn<TableRowData, String> productivityColumn;

    @FXML
    private TableColumn<TableRowData, String> workNameColumn;

    @FXML
    private TableColumn<TableRowData, String> startColumn;

    @FXML
    private TableColumn<TableRowData, String> workerColumn;

    @FXML
    private TableColumn<TableRowData, String> trashColumn;

    @FXML
    private TableColumn<TableRowData, String> workNrColumn;

    @FXML
    private TableColumn<TableRowData, String> dateColumn;

    @FXML
    private TableColumn<TableRowData, String> reasonColumn;


    /**
     * This method is called when the user logs out. The stage will switch to the log in view.
     * Fires on Log out button press.
     */
    @FXML
    void logOutPress() throws Exception { //User logs out, view goes to log in view.

        ViewNavigator.getInstance().goToLogInView();

    }

    /**
     * This method is called when the user clicks the delete message button. The currently shown message will be deleted
     * from the database. After deletion the following message is shown if the deleted message was the last one the previous
     * message will be shown. If there is no messages left after deletion messageSender and dateTime labels and message box will
     * be set to an empty string. Message amount label is always updated to the amount of messages.
     * Fires on Delete button press.
     */
    @FXML
    void deleteMsgPress() {

        MySqlDatabase.getInstance().deleteMessage(messages.get(messageBoxPointer));
        messages.remove(messageBoxPointer);
        msgAmountLabel.setText(Integer.toString(messages.size()));

        if(messages.size() == 0) {

            messageBox.setText("");
            msgSenderLabel.setText("");
            dateTimeReceivedLabel.setText("");

        } else if(messageBoxPointer >= messages.size()) {

            messageBoxPointer = messages.size();
            previousMsgPress();

        } else
            showMessage();

        updateMessageButtons();
    }

    /**
     * This method is called when the user clicks the previous message button. If the first message is showing nothing will
     * happen else the previous message will be shown.
     * Fires on >> button press.
     */
    @FXML
    void previousMsgPress() {

        messageBoxPointer--;

        if (messageBoxPointer >= 0) {

            showMessage();

        } else {
            messageBoxPointer = 0;
        }
        updateMessageButtons();
    }

    /**
     * This method is called when the user clicks the next message button. If the last message is showing nothing will
     * happen else the following message will be shown.
     * Fires on >> button press.
     */
    @FXML
    void nextMsgPress() {

        messageBoxPointer++;

        if (messageBoxPointer < messages.size()) {

            showMessage();

        } else {
            messageBoxPointer = messages.size() - 1;
        }
        updateMessageButtons();
    }

    /**
     * A notification is added to the database.
     * Fires on Call manager button press.
     */
    @FXML
    void callManagerPress() {

        MySqlDatabase.getInstance().addNotification(user.getUserKey(), "0389");
        new AlertBox("Notification Sent.", 0);

    }


    /**
     * This method is called when the currently logged in user checks in. This will create a TableRowData object that
     * is added to the database. The view layout is updated to the "not working view".
     * Fires on Check in button press.
     */
    @FXML
    void checkinPress() {

        user.logIn(); //Updates user status
        user.setCurrentWorkStep(new TableRowData(user.getUserKey()));
        user.getCurrentWorkStep().setTime(new TimeAndDateHelper().getTime());
        user.getCurrentWorkStep().setWork_step_name("Check in");
        user.getCurrentWorkStep().setWork_id("00000");

        addRow();
        setNotWorkingView();

    }

    /**
     * This method is called when the currently logged in user checks out. This will create a TableRowData object that
     * is added to the database. The view Layout is updated to the "not checked in view".
     * Fires on Check out button press.
     */
    @FXML
    void checkOutPress() {

        user.logOut();
        user.stopWork();
        user.setCurrentWorkStep(new TableRowData(user.getUserKey()));
        user.getCurrentWorkStep().setTime(new TimeAndDateHelper().getTime());
        user.getCurrentWorkStep().setWork_step_name("Check out");
        user.getCurrentWorkStep().setWork_id("99999");

        addRow();
        setNotCheckedInView();

    }

    /**
     * This method is called when the user starts a new work step. The view layout is updated to the "working view".
     * The user inputted work number is fetched from the work steps map that is located in the class DataMaps.
     * If the work number corresponds to a work step it is created to a TableRowData object that is added to the
     * database and set to the users current work step.
     * Fires on start button press.
     */
    @FXML
    void startPress() {

        String workNumber = workNrTextField.getText();
        String stepName = DataMaps.getInstance().getWorkStepsMap().get(workNumber);

        if (stepName != null) {

            user.setCurrentWorkStep(new TableRowData(user.getUserKey()));
            user.getCurrentWorkStep().setTime(new TimeAndDateHelper().getTime());
            user.getCurrentWorkStep().setWork_step_name(stepName);
            user.getCurrentWorkStep().setWork_id(workNumber);
            user.setCurrentWorkNr(workNumber);
            user.startWork();
            setWorkingView();
            addRow();

        } else {

            new AlertBox("Invalid work number.", 2);

        }

    }

    /**
     * This method is called when the user ends their current work step. The view layout is updated to the "not working view".
     * The user inputted amount and trash amount is checked to make sure they are not left empty and if the trash amount is not
     * 0 the reason combo box is also checked to make sure that a reason is chosen. If all these conditions are checked we create
     * a TableRowData object that is added to the database and the users current work step is set to null.
     * Fires on end button press.
     */
    @FXML
    void endPress() {

        String amount = amountTextField.getText();
        String trashAmount = trashTextField.getText();

        if(amount.equals(""))
            new AlertBox("Fill in amount field.", 3);
        else if (trashAmount.equals(""))
            new AlertBox("Fill in trash field.", 3);
        else if(!trashAmount.equals("0") && reasonComboBox.getValue() == null)
            new AlertBox("Choose a reason for the trash amount.", 3);
        else {

                //TableRowData object is created.
                TableRowData endStep = new TableRowData(user.getUserKey());
                endStep.setWork_id(user.getCurrentWorkStep().getWork_id());
                endStep.setWork_step_name(user.getCurrentWorkStep().getWork_step_name());
                endStep.setTime(new TimeAndDateHelper().getTime());
                endStep.setAmount_done(amount);
                endStep.setTrash_amount(trashAmount);
                endStep.setProductivity(user.calculateProductivity(user.getCurrentWorkStep().getWork_id(), Integer.parseInt(amount), user.getCurrentWorkStep().getTime(), endStep.getTime()));
                endStep.setReason((reasonComboBox.getValue() == null) ? "" : reasonComboBox.getValue());
                user.setCurrentWorkStep(endStep);

                //View is updated and the row is added to the database.
                setNotWorkingView();
                addRow();

                //Text fields are emptied and user variables updated.
                workNrTextField.setText("");
                amountTextField.setText("");
                trashTextField.setText("");
                reasonComboBox.setValue("");
                user.setCurrentWorkNr("");
                user.setCurrentWorkStep(null);
                user.stopWork();

        }
    }

    /**
     * This method is called on initialization. The logged in user is stored into a variable. JavaFx
     * control properties are set and/or given listeners.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) { //Initializes everything.

        user = (Worker) ViewNavigator.getInstance().getLoggedInUser();

        messageBox.setEditable(false);
        messageBox.getStylesheets().add("view/DisabledMessageBox.css");
        messages = MySqlDatabase.getInstance().getMessages(user.getUserKey());
        msgAmountLabel.setText(Integer.toString(messages.size()));

        if (messages.size() > 0) { //If messages exist in database for logged in user.
            messageBox.setText(messages.getFirst().getMessage());
            msgSenderLabel.setText(DataMaps.getInstance().getEmployeeMap().get(messages.getFirst().getSender()).getUserName());
            dateTimeReceivedLabel.setText(messages.getFirst().getTimeStamp());
            messageBoxPointer = 0;
        }

        checkTrashAmountListener(trashTextField);

        table.getStylesheets().add("view/hideScrollbar.css");
        rowData.addAll(MySqlDatabase.getInstance().getWorkSteps(new TimeAndDateHelper().getDate(), user.getUserKey()));
        table.setItems(rowData);

        updateMessageButtons();
        timeController();
        customizeView();

        TextFieldHelper helper = new TextFieldHelper();
        helper.setCharLimit(workNrTextField, 5);
        helper.setCharLimit(amountTextField, 3);
        helper.setCharLimit(trashTextField, 3);
        helper.onlyNumbers(workNrTextField);
        helper.onlyNumbers(amountTextField);
        helper.onlyNumbers(trashTextField);

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        workNrColumn.setCellValueFactory(new PropertyValueFactory<>("work_id"));
        workerColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount_done"));
        trashColumn.setCellValueFactory(new PropertyValueFactory<>("trash_amount"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        productivityColumn.setCellValueFactory(new PropertyValueFactory<>("productivity"));
        workNameColumn.setCellValueFactory(new PropertyValueFactory<>("work_step_name"));

    }

    /**
     * This method is called on during initialization. It checks which view should be set on
     * startup and disables the text fields used for logging in which are not needed.
     */
    private void customizeView() {

        userLabel.setText(user.getUserName());
        userTextField.setDisable(true);
        workNrTextField.setText(user.getCurrentWorkNr());
        keyTextField.setDisable(true);

        if (!user.isLoggedIn()) { //Not checked in.
            setNotCheckedInView();
        } else if (!user.isWorking()) { //Not currently working on anything.
            setNotWorkingView();
        } else { //Currently working on something.
            setWorkingView();
        }

    }

    /**
     * This method updates which of the 3 message buttons should be enabled. If there are no messages all buttons will
     * be disabled (0), if current message is the last message the next message button will be disabled (1), if current
     * message is the first message the previous message will be disabled (2), if only one message is left both next and
     * previous button will be disabled (3) else all buttons enabled.
     * Fires on next, previous and delete button press.
     */
    private void updateMessageButtons() {

        if (messages.size() <= 0) { //(0)
            previousMsgButton.setDisable(true);
            deleteMsgButton.setDisable(true);
            nextMsgButton.setDisable(true);
        } else if (messageBoxPointer == messages.size() - 1 && messageBoxPointer > 0) { //(1)
            previousMsgButton.setDisable(false);
            deleteMsgButton.setDisable(false);
            nextMsgButton.setDisable(true);
        } else if (messageBoxPointer - 1 < 0 && messages.size() > 1) { //(2)
            previousMsgButton.setDisable(true);
            deleteMsgButton.setDisable(false);
            nextMsgButton.setDisable(false);
        } else if (messages.size() == 1) { //(3)
            previousMsgButton.setDisable(true);
            deleteMsgButton.setDisable(false);
            nextMsgButton.setDisable(true);
        } else {
            previousMsgButton.setDisable(false);
            deleteMsgButton.setDisable(false);
            nextMsgButton.setDisable(false);
        }

    }


    /**
     * This view will be set when the logged in user is not checked in.
     */
    private void setNotCheckedInView() {
        checkOutButton.setDisable(true);
        startButton.setDisable(true);
        endButton.setDisable(true);
        workNrTextField.setDisable(true);
        amountTextField.setDisable(true);
        trashTextField.setDisable(true);
        reasonComboBox.setDisable(true);
        checkInButton.setDisable(false);

    }

    /**
     * This view will be set when the user does not have a active work step.
     */
    private void setNotWorkingView() { //Sets the view.
        checkInButton.setDisable(true);
        amountTextField.setDisable(true);
        endButton.setDisable(true);
        trashTextField.setDisable(true);
        reasonComboBox.setDisable(true);
        checkOutButton.setDisable(false);
        startButton.setDisable(false);
        workNrTextField.setDisable(false);
    }

    /**
     * This view will be set when the user is working on a work step.
     */
    private void setWorkingView() { //Sets the view.
        checkInButton.setDisable(true);
        startButton.setDisable(true);
        workNrTextField.setDisable(true);
        endButton.setDisable(false);
        checkOutButton.setDisable(false);
        amountTextField.setDisable(false);
        trashTextField.setDisable(false);
        reasonComboBox.setDisable(false);
    }

    /**
     * This method adds users current work step to the database and adds it to the JavaFx
     * table view.
     */
    private void addRow() {

        rowData.add(user.getCurrentWorkStep());
        table.setItems(rowData);
        MySqlDatabase.getInstance().workerAddWorkStep(user.getCurrentWorkStep());

    }

    /**
     * This method updates the message that is shown in the JavaFx text area + the messageSender and dateTimeReceived label.
     */
    private void showMessage() {

        messageBox.setText(messages.get(messageBoxPointer).getMessage());
        msgSenderLabel.setText(DataMaps.getInstance().getEmployeeMap().get(messages.get(messageBoxPointer).getSender()).getUserName());
        dateTimeReceivedLabel.setText(messages.get(messageBoxPointer).getTimeStamp());

    }

    /**
     * This listener is applied to the trash amount text field to set the values inside of the reason combo box to null if
     * the text field is empty or the value is 0.
     */
    private void checkTrashAmountListener(TextField textField){
        textField.textProperty().addListener(c -> {

                if (textField.getText().length() > 0 && !textField.getText().equals("0")) {
                    reasonComboBox.setItems(FXCollections.observableArrayList(
                            "Broken part", "Accident", "Failed test", "Other"
                    ));
                } else {
                    reasonComboBox.setItems(null);
                }

        });
    }

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


