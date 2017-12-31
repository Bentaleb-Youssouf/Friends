package ti.dam.bentaleb.benali.friends;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ti.dam.bentaleb.benali.friends.Database.Friend;
import ti.dam.bentaleb.benali.friends.Database.MyHelper;
import ti.dam.bentaleb.benali.friends.Database.User;

/**
 * Created by Bentaleb Youssouf on 12/3/2017.
 */

public class AddDialog extends Dialog {

    Activity activity;


    public AddDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_dialog);
        MyHelper myHelper = new MyHelper(activity);

        TextView noFriendAvailable = findViewById(R.id.noFriendAvailable);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("FRIEND_APP", 0);
        int userID = sharedPreferences.getInt("USER_ID", -1);
        ListView friendRequestsLV = findViewById(R.id.friendRequestLV);
        ArrayList<User> friendRequests = new ArrayList<>();

       /* friendRequests.add(new FriendRequest(R.drawable.avatar_01,"Bentaleb Youssouf"));
        friendRequests.add(new FriendRequest(R.drawable.avatar_01,"Bentaleb Youssouf"));
        friendRequests.add(new FriendRequest(R.drawable.avatar_01,"Bentaleb Youssouf"));
        friendRequests.add(new FriendRequest(R.drawable.avatar_01,"Bentaleb Youssouf"));
        friendRequests.add(new FriendRequest(R.drawable.avatar_01,"Bentaleb Youssouf"));*/

        friendRequests = myHelper.getRequests(userID);
        if (friendRequests.isEmpty()) {
            friendRequestsLV.setVisibility(View.GONE);
        } else {
            noFriendAvailable.setVisibility(View.GONE);
            FriendRequestAdapter adapter = new FriendRequestAdapter(activity, R.layout.friend_request_row, friendRequests, "SEND");
            friendRequestsLV.setAdapter(adapter);
        }
        Button cancelBtn = findViewById(R.id.cancelBtn);

        /*
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyHelper helper = new MyHelper(getContext());
                //helper.createFriend(new FriendItem(R.drawable.avatar_02,name.getText().toString()
                        //,"winta tjini","2 min ago"));
                Toast.makeText(getContext(),"Button add click",Toast.LENGTH_SHORT).show();
                //FriendActivity.adapter.notifyDataSetChanged();
                dismiss();
            }
        });
        */

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
