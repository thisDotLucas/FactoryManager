package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


class LogInView {

    LogInView(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/logInView.fxml"));
        primaryStage.setTitle("FactoryManager");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1188, 788));
        primaryStage.show();

    }

}
