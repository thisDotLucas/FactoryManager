package model;

public class Message {

    private final String sender;
    private final String message;
    private final String receiver;
    private final String timeStamp;

    public Message(String sender, String receiver, String message, String timeStamp){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timeStamp = timeStamp;
    }


    public String getSender() {
        return sender;
    }

    public String getReceiver(){
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
