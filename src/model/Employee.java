package model;

abstract class Employee {

    String userId;
    String userKey;


   Employee(String userId, String userKey){

       this.userId = userId;
       this.userKey = userKey;

   }

    String getuserId() {
        return userId;
    }

    String getUserKey() {
        return userKey;
    }

}
