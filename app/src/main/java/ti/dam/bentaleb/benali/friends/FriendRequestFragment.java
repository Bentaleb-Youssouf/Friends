package ti.dam.bentaleb.benali.friends;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import ti.dam.bentaleb.benali.friends.Database.MyHelper;


/**
 * Created by Bentaleb Youssouf on 12/20/2017.
 */

public class FriendRequestFragment extends Fragment {
    ImageView profileImg;
    TextView friendName;
    Button confirmBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FRIEND_APP", 0);
        int userID = sharedPreferences.getInt("USER_ID", -1);
        MyHelper myHelper = new MyHelper(getContext());

        TextView textView = view.findViewById(R.id.requestNone);
        ListView listView = view.findViewById(R.id.friendRequestLV);
        ArrayList friendRequests = myHelper.getFriendRequests(userID);

        /*
        friendRequests.add(new FriendRequest(R.drawable.avatar_01,"Bentaleb Youssouf"));
        friendRequests.add(new FriendRequest(R.drawable.avatar_01,"Bentaleb Youssouf"));
        friendRequests.add(new FriendRequest(R.drawable.avatar_01,"Bentaleb Youssouf"));*/

        if (friendRequests.isEmpty()) {
            listView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            FriendRequestAdapter adapter = new FriendRequestAdapter(getActivity(), R.layout.friend_request_row, friendRequests, "RECEIVE");
            listView.setAdapter(adapter);

        }


        // get the list view and set the adapter
        return view;
    }
}
