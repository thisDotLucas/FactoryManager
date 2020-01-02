package view;

import controller.ViewNavigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import model.EditAddRowHelper;

import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMaxHeight(832);
        primaryStage.setMaxWidth(1205);
        //EditAddRowHelper.getInstance();
        ViewNavigator.getInstance().setStage(primaryStage);
        ViewNavigator.getInstance().goToLogInView(); //Log in view on startup

    }


    public static void main(String[] args) {

        Locale.setDefault(Locale.ENGLISH); //Language English
        launch(args);

    }
}
