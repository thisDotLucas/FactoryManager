package model;

public class IOHelper {


    public boolean isConvertibleToInteger(String stringInput) {
        try {
            Integer.parseInt(stringInput);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isNegative(int intInput) {

        if (intInput >= 0) {
            return false;
        } else {
            return true;
        }
    }

    public String capitalizeFirsChar(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
