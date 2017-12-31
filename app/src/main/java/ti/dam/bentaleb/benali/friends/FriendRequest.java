package ti.dam.bentaleb.benali.friends;

/**
 * Created by Bentaleb Youssouf on 12/23/2017.
 */

public class FriendRequest {
    public int friendID;
    public int profileImg;
    public String name;

    public FriendRequest(int friendID, int profileImg, String name) {
        this.friendID = friendID;
        this.profileImg = profileImg;
        this.name = name;
    }
}
