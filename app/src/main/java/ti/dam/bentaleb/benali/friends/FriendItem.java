package ti.dam.bentaleb.benali.friends;

/**
 * Created by Bentaleb Youssouf on 12/19/2017.
 */

public class FriendItem {
    int friendImg,friendID;
    String friendName , lastMsg , lastMsgTime;

    public FriendItem(int friendID,int friendImg,String friendName,String lastMsg,String lastMsgTime){
        this.friendID=friendID;
        this.friendImg=friendImg;
        this.friendName= friendName;
        this.lastMsg=lastMsg;
        this.lastMsgTime=lastMsgTime;
    }
}
