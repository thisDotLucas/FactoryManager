package model;

public class Manager implements Employee {

    private String userName;
    private String userKey;

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

}
