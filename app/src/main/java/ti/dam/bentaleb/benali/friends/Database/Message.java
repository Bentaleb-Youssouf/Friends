package ti.dam.bentaleb.benali.friends.Database;

import java.util.Date;

/**
 * Created by djame on 09/12/2017.
 */

public class Message {
    public int id;
    public String content;
    public String date;
    public int senderID;


    public Message(int id, String content, String date , int senderID ) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.senderID = senderID;

    }






}
