package model;

import controller.FxWorkerTableController;
import javafx.scene.control.TableView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Worker implements Employee {

    private String userName;
    private String userKey;
    private String currentWorkNr;
    private FxWorkerTableController tableController;
    private TableRowData currentWorkStep;
    private boolean online = false;
    private boolean working = false;

    public Worker(String userName, String userKey) {
        this.userName = userName;
        this.userKey = userKey;
    }


    public void startWork(){
        working = true;
    }

    public void stopWork(){
        working = false;
    }

    public void logIn(){
        online = true;
    }

    public void logOut(){
        online = false;
    }

    public boolean isWorking(){
        return working;
    }

    public boolean isLoggedIn(){
        return online;
    }

    public void setCurrentWorkStep(TableRowData step){
        currentWorkStep = step;
    }

    public TableRowData getCurrentWorkStep(){
        return currentWorkStep;
    }


    public String calculateProductivity(String workId, int amount, String startTime, String endTime){
        Map<Integer, Float> map = DataMaps.getInstance().getProductivityScoresMap();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date start;
        Date end;
        try {
            start = formatter.parse(startTime);
            end = formatter.parse(endTime);
        } catch (ParseException e){
            e.printStackTrace();
            return "120%";
        }

        long diff = Math.abs(end.getTime() - start.getTime());
        int diffInMin = (int)(diff/60*1000)/1000000;

        int id = Integer.parseInt(workId);

        float productivityMultiplier;
        if(map.containsKey(id)){
            productivityMultiplier = map.get(id);
        } else {
            return "120%";
        }
        NumberFormat decFormatter = new DecimalFormat("#0");

        return decFormatter.format((amount/((diffInMin + 0.1)*productivityMultiplier))*300) + "%";
    }

    public void setTableController(TableView<?> table){
        tableController = new FxWorkerTableController(table);
    }

    public FxWorkerTableController getTableController(){
        return tableController;
    }

    public String getCurrentWorkNr() {
        return currentWorkNr;
    }

    public void setCurrentWorkNr(String currentWorkNr) {
        this.currentWorkNr = currentWorkNr;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getUserKey() {
        return userKey;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
