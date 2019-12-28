package model;

import controller.FxWorkerTableController;
import javafx.scene.control.TableView;

public class Worker implements Employee {

    private String userName;
    private String userKey;
    private FxWorkerTableController tableController;
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


    public void setTableController(TableView<?> table){
        tableController = new FxWorkerTableController(table);
    }

    public FxWorkerTableController getTableController(){
        return tableController;
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
