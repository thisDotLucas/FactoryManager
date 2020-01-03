package model;

public class IOHelper {

    /**
     * Capitalizes the first character of the given string.
     */
    public String capitalizeFirsChar(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
    
}
