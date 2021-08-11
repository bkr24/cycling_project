package aston.rogalskb.cyclingproject_v3.socialComponents;

import com.google.firebase.database.PropertyName;

public class chatModel {

    String message,receiver, sender, timeStamp;
    boolean wasSeen;

    public chatModel() {
    }


    public chatModel(String message, String receiver, String sender, String timeStamp, boolean wasSeen) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.wasSeen = wasSeen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @PropertyName("wasSeen")
    public boolean isWasSeen() {
        return wasSeen;
    }


    @PropertyName("wasSeen")
    public void setWasSeen(boolean wasSeenVar) {
          wasSeen = wasSeenVar;
    }
}
