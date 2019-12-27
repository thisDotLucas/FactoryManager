package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class AlertBox {

    private AlertType type;

    public AlertBox(String message, int type){ //Constructor

        assert type < 4 && type > 0 : "type only takes int between 0-3 as arguments."; //checks the input.

        this.type = setType(type);

        Alert alert = new Alert(this.type);
        alert.setContentText(message);
        alert.show();

    }


    public AlertBox(String message, String title, int type){ //constructor with custom title.

        assert type < 4 && type > 0 : "type only takes int between 0-3 as arguments."; //checks the input.

        this.type = setType(type);

        Alert alert = new Alert(this.type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();

    }


    private AlertType setType(int type) { //sets the alert type.

        switch (type){
            case 0:
                return this.type = AlertType.INFORMATION;
            case 1:
                return this.type = AlertType.CONFIRMATION;
            case 2:
                return this.type = AlertType.WARNING;
            case 3:
                return this.type = AlertType.ERROR;
        }
        return AlertType.NONE;
    }

}
