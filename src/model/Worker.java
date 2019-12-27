package model;

public class Worker implements Employee {

    String userName;
    String userKey;

    public Worker(String userName, String userKey) {
        this.userName = userName;
        this.userKey = userKey;
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
