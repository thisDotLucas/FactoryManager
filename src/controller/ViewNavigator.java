package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Employee;
import model.IOHelper;

//SINGLETON OBJECT

public class ViewNavigator {

    private static ViewNavigator ourInstance;
    private Stage primaryStage;
    private Employee loggedInUser; //Currently logged in user, OBS remember to update this ALWAYS after changing views.


    //Returns this object.
    public static ViewNavigator getInstance() {

        if(ourInstance == null){
            ourInstance = new ViewNavigator();
        }

        return ourInstance;
    }


    //Switches to log in view.
    public void goToLogInView() throws Exception{

        loggedInUser = null;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/logInView.fxml"));
        primaryStage.setTitle("FactoryManager");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    //Switches to worker view.
    void goToWorkerView() throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/workerView.fxml"));
        finishStage(root);

    }


    //Switches to manager view.
    void goToManagerView() throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/managerView.fxml"));
        finishStage(root);

    }


    private void finishStage(Parent root) {

        primaryStage.setTitle(loggedInUser.getUserName());
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    //Sets the JavaFx Stage at start up, cant be changed later.
    public void setStage(Stage primaryStage){

        if(this.primaryStage == null){
            this.primaryStage = primaryStage;
        }

    }


    //Updates the current logged in user, always update this after switching views.
    void setCurrentUser(Employee user) {
        loggedInUser = user;
    }


    Employee getLoggedInUser() {
        return loggedInUser;
    }

}
