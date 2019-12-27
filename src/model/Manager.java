package model;

public class Manager implements Employee {

    String userName;
    String userKey;

    public Manager(String userName, String userKey) {
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
