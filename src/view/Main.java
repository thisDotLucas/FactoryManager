package view;

import controller.ViewNavigator;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ViewNavigator.getInstance().setStage(primaryStage);
        ViewNavigator.getInstance().goToLogInView(); //Log in view on startup

    }


    public static void main(String[] args) {

        Locale.setDefault(Locale.ENGLISH); //Language English
        launch(args);

    }
}
