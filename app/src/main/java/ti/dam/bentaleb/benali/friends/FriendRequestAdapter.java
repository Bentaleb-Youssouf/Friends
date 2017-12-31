package ti.dam.bentaleb.benali.friends;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ti.dam.bentaleb.benali.friends.Database.Friend;
import ti.dam.bentaleb.benali.friends.Database.MyHelper;
import ti.dam.bentaleb.benali.friends.Database.User;


/**
 * Created by Bentaleb Youssouf on 12/23/2017.
 */

public class FriendRequestAdapter extends ArrayAdapter<User> {
    Context activity;
    int resource;
    ArrayList<User> friends;
    ImageView profileImg;
    TextView friendName;
    ImageButton confirmBtn;
    String type;

    public FriendRequestAdapter(Activity activity, int resource, ArrayList<User> friends, String type) {
        super(activity, resource, friends);
        this.activity = activity;
        this.resource = resource;
        this.friends = friends;
        this.type = type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            view = inflater.inflate(resource, parent, false);
        }
        profileImg = view.findViewById(R.id.profile_image);
        friendName = view.findViewById(R.id.friend_name);
        confirmBtn = view.findViewById(R.id.confirm_btn);

        friendName.setText(friends.get(position).firstName + " " + friends.get(position).lastName);
        profileImg.setImageResource(friends.get(position).profile_img);

        final MyHelper myHelper = new MyHelper(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("FRIEND_APP", 0);
        final int userID = sharedPreferences.getInt("USER_ID", -1);

        //This method when the adapter is for send requests
        //We put the Button to "SEND" and implement onClickListener

        if (type == "SEND") {
            confirmBtn.setImageResource(R.drawable.ic_add_friend);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myHelper.addFriendRequest(new Friend(friends.get(position).userID, userID, 0));
                    Toast.makeText(activity, "SEND BTN CLicked , POS = " + position, Toast.LENGTH_SHORT).show();
                    friends.remove(position);
                    notifyDataSetChanged();

                }
            });
        } else {
            confirmBtn.setImageResource(R.drawable.ic_accept);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add the friend
                    myHelper.confirmFriendRequest(new Friend(userID, friends.get(position).userID, 0));
                    friends.remove(position);
                    Toast.makeText(activity, "Confirm Btn clicked , POS = " + position, Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
        }


        return view;
    }
}
