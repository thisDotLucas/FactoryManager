package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewController{

    private static ViewController ourInstance;
    private static Stage primaryStage;

    public static ViewController getInstance() {

        if(ourInstance == null){
            ourInstance = new ViewController();
        }

        return ourInstance;
    }

    public void setStage(Stage primaryStage){

        if(this.primaryStage == null){
            this.primaryStage = primaryStage;
        }

    }

    public void goToLogInView() throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/logInView.fxml"));
        primaryStage.close();
        primaryStage.setTitle("FactoryManager");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1188, 788));
        primaryStage.show();

    }

    void goToWorkerView() throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/workerView.fxml"));
        primaryStage.close();
        primaryStage.setTitle("Worker");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1188, 788));
        primaryStage.show();

    }

    void goToManagerView() throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/managerView.fxml"));
        primaryStage.close();
        primaryStage.setTitle("Manager");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1188, 788));
        primaryStage.show();

    }

}
