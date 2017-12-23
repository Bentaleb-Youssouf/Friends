package ti.dam.bentaleb.benali.friends.Database;

/**
 * Created by Bentaleb Youssouf on 12/16/2017.
 */

public class User {
    public String username,password,firstName,lastName,email;
    public int profile_img;

    public User(  String username, String password, String firstName, String lastName, String email, int profile_img ) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profile_img=profile_img;

    }



}
