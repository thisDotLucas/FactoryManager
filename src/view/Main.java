package view;

import controller.ViewController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ViewController viewController = new ViewController();
        viewController.setStage(primaryStage);
        viewController.goToLogInView(); //Log in view on startup

    }


    public static void main(String[] args) {

        Locale.setDefault(Locale.ENGLISH); //Language English
        launch(args);

    }
}
