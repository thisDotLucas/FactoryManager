package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Employee;


/**
 * SINGLETON OBJECT
 * This class is used to switch between the available views.
 */
public class ViewNavigator {

    private static ViewNavigator ourInstance;
    private Stage primaryStage;
    private Employee loggedInUser; //Currently logged in user, remember to update this after changing views.


    public static ViewNavigator getInstance() {

        if (ourInstance == null) {
            ourInstance = new ViewNavigator();
        }

        return ourInstance;
    }


    /**
     * This method loads and shows the log in view, logged in user is always set to null.
     */
    public void goToLogInView() throws Exception {

        loggedInUser = null;

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/logInView.fxml"));
        primaryStage.setTitle("FactoryManager");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    /**
     * This method loads and shows the worker view.
     */
    void goToWorkerView() throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/workerView.fxml"));
        finishStage(root);

    }

    /**
     * This method loads and shows the manager view.
     */
    void goToManagerView() throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/managerView.fxml"));
        finishStage(root);

    }

    /**
     * This method sets the properties for the stage.
     */
    private void finishStage(Parent root) {

        primaryStage.setTitle(loggedInUser.getUserName());
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    /**
     * This method sets the JavaFx Stage at start up, cannot be changed later.
     */
    public void setStage(Stage primaryStage) {

        if (this.primaryStage == null) {
            this.primaryStage = primaryStage;
        }

    }


    void setCurrentUser(Employee user) {
        loggedInUser = user;
    }


    Employee getLoggedInUser() {
        return loggedInUser;
    }

}
