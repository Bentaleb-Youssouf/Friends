package ti.dam.bentaleb.benali.friends.Database;

/**
 * Created by Bentaleb Youssouf on 11/26/2017.
 */

public class Friend {
    public int friendID, senderID, confirmed;


    public Friend(int friendID, int senderID, int confirmed) {
        this.friendID = friendID;
        this.senderID = senderID;

        this.confirmed = confirmed;
    }
}


//int friendImg;
//String friendLastName ,friendFirstName ;
//ArrayList<Message> messages;
        /*this.id=id;
        this.friendImg=friendImg;
        this.friendFirstName= friendFirstName;
        this.friendLastName = friendLastName;
        */
//this.messages=messages;