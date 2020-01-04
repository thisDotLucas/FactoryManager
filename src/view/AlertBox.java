package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class AlertBox {

    private AlertType type;

    /**
     * This constructor creates a alert box. It takes the message as a string and a int for the alert
     * type as arguments.
     * 0 = info alert
     * 1 = confirmation alert
     * 2 = warning alert
     * 3 = error alert
     */
    public AlertBox(String message, int type) {

        assert type < 4 && type > 0 : "type only takes int between 0-3 as arguments.";

        this.type = setType(type);

        Alert alert = new Alert(this.type);
        alert.setContentText(message);
        alert.show();

    }

    /**
     * Same as base constructor + custom title.
     */
    public AlertBox(String message, String title, int type) {

        assert type < 4 && type > 0 : "type only takes int between 0-3 as arguments.";

        this.type = setType(type);

        Alert alert = new Alert(this.type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();

    }

    /**
     * This method sets the alert type.
     */
    private AlertType setType(int type) {

        switch (type) {
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
