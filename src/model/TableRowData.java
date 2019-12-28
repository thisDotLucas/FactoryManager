package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


public class TableRowData {

    private SimpleStringProperty work_id;
    private final SimpleStringProperty user_id;
    private final SimpleStringProperty date;
    private SimpleStringProperty start_time;
    private SimpleStringProperty end_time;
    private SimpleStringProperty amount_done;
    private SimpleStringProperty trash_amount;
    private SimpleStringProperty productivity;
    private SimpleStringProperty work_step_name;


    public TableRowData(String userId){
        this.user_id = new SimpleStringProperty(userId);
        this.date = new SimpleStringProperty(new TimeAndDateHelper().getDate());
        this.work_id = new SimpleStringProperty("");
        this.start_time = new SimpleStringProperty("");
        this.end_time = new SimpleStringProperty("");
        this.amount_done = new SimpleStringProperty("");
        this.trash_amount = new SimpleStringProperty("");
        this.productivity = new SimpleStringProperty("");
        this.work_step_name = new SimpleStringProperty("");

    }



    public void setStart_time(String start_time) {
        this.start_time = new SimpleStringProperty(start_time);
    }

    public void setWork_id(String work_id){
        this.work_id = new SimpleStringProperty(work_id);
    }

    public void setEnd_time(String end_time) {
        this.end_time = new SimpleStringProperty(end_time);
    }

    public void setAmount_done(String amount_done) {
        this.amount_done = new SimpleStringProperty(amount_done);
    }

    public void setTrash_amount(String trash_amount) {
        this.trash_amount = new SimpleStringProperty(trash_amount);
    }

    public void setProductivity(String productivity) {
        this.productivity = new SimpleStringProperty(productivity);
    }

    public void setWork_step_name(String work_step_name) {
        this.work_step_name = new SimpleStringProperty(work_step_name);
    }

    public String getUser_id() {
        return user_id.get();
    }


    public String getWork_id() {
        return work_id.get();
    }

    public String getDate() {
        return date.get();
    }


    public String getStart_time() {
        return start_time.get();
    }


    public String getEnd_time() {
        return end_time.get();
    }


    public String getAmount_done() {
        return amount_done.get();
    }


    public String getTrash_amount() {
        return trash_amount.get();
    }

    public String getProductivity() {
        return productivity.get();
    }

    public String getWork_step_name() {
        return work_step_name.get();
    }


}
