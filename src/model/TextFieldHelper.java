package model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextFieldHelper {

    /**
     * This listener makes the text field only accept numbers.
     */
    public void onlyNumbers(TextField textField){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * This listener sets a limit for the amount of characters in a text field.
     */
    public void setCharLimit(TextField textField, int limit){
        textField.textProperty().addListener(l -> {
                if (textField.getText().length() > limit) {
                    String s = textField.getText().substring(0, limit);
                    textField.setText(s);
                }
        });
    }

}
