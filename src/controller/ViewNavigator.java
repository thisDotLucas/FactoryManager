package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//SINGLETON OBJECT

public class ViewNavigator {

    private static ViewNavigator ourInstance;
    private Stage primaryStage;
    private String loggedInUser;

    public static ViewNavigator getInstance() {

        if(ourInstance == null){
            ourInstance = new ViewNavigator();
        }

        return ourInstance;
    }


    public void goToLogInView() throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/logInView.fxml"));
        //primaryStage.close();
        primaryStage.setTitle("FactoryManager");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1188, 788));
        primaryStage.show();

    }

    void goToWorkerView() throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/workerView.fxml"));
        setStage(root);

    }

    void goToManagerView() throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/managerView.fxml"));
        setStage(root);

    }

    private void setStage(Parent root) {

        //primaryStage.close();
        primaryStage.setTitle(loggedInUser);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1188, 788));
        primaryStage.show();

    }

    public void setStage(Stage primaryStage){

        if(this.primaryStage == null){
            this.primaryStage = primaryStage;
        }

    }

    public void setCurrentUser(String user) {
        loggedInUser = user;
    }

}
