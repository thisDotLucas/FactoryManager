package model;

public class IOHelper {


    public boolean isConvertibleToInteger(String stringInput){
        try{
            Integer.parseInt(stringInput);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public boolean isNegative(int intInput){

        if(intInput >= 0){
            return false;
        } else {
          return true;
        }
    }
}
