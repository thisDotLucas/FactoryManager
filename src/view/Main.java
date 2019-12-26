package view;

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        new LogInView(primaryStage); //Log in view on startup

    }


    public static void main(String[] args) {

        Locale.setDefault(Locale.ENGLISH);
        launch(args);

    }
}
