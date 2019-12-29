package model;

import javafx.beans.property.SimpleStringProperty;


public class TableRowData {

    private SimpleStringProperty work_id;
    private final SimpleStringProperty user_id;
    private final SimpleStringProperty date;
    private SimpleStringProperty time;
    private SimpleStringProperty amount_done;
    private SimpleStringProperty trash_amount;
    private SimpleStringProperty reason;
    private SimpleStringProperty productivity;
    private SimpleStringProperty work_step_name;


    public TableRowData(String userId){
        this.user_id = new SimpleStringProperty(userId);
        this.date = new SimpleStringProperty(new TimeAndDateHelper().getDate());
        this.work_id = new SimpleStringProperty("");
        this.time = new SimpleStringProperty("");
        this.amount_done = new SimpleStringProperty("");
        this.trash_amount = new SimpleStringProperty("");
        this.reason = new SimpleStringProperty("");
        this.productivity = new SimpleStringProperty("");
        this.work_step_name = new SimpleStringProperty("");

    }



    public void setTime(String start_time) {
        this.time = new SimpleStringProperty(start_time);
    }

    public void setWork_id(String work_id){
        this.work_id = new SimpleStringProperty(work_id);
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

    public void setReason(String reason) {
        this.reason = new SimpleStringProperty(reason);
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


    public String getTime() {
        return time.get();
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

    public String getReason() {
        return reason.get();
    }
}
