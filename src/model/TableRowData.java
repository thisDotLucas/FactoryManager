package model;

import javafx.beans.property.SimpleStringProperty;

import java.util.SplittableRandom;

public class TableRowData {

    private final SimpleStringProperty user_id;
    private final SimpleStringProperty user_name;
    private final SimpleStringProperty date;
    private SimpleStringProperty start_time;
    private SimpleStringProperty end_time;
    private SimpleStringProperty amount_done;
    private SimpleStringProperty trash_amount;
    private SimpleStringProperty work_step_name;


    public TableRowData(String userId, String userName){
        this.user_id = new SimpleStringProperty(userId);
        this.user_name = new SimpleStringProperty(userName);
        this.date = new SimpleStringProperty(new TimeAndDateHelper().getDate());
    }
}
