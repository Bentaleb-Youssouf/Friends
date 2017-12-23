package ti.dam.bentaleb.benali.friends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ti.dam.bentaleb.benali.friends.Database.MyHelper;
import ti.dam.bentaleb.benali.friends.Database.User;


/**
 * Created by Bentaleb Youssouf on 12/20/2017.
 */

public class HomeFragment extends Fragment {

    User currentUser;
    ListView friendLV;
    int userID;
    FriendAdapter adapter;
    FloatingActionButton fab;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        friendLV =view.findViewById(R.id.friendList);
        fab = view.findViewById(R.id.fab_add_friend);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<FriendItem> friendsList = new ArrayList<>();
        MyHelper myHelper = new MyHelper(getContext());
        SharedPreferences preferences;
        preferences = getActivity().getSharedPreferences("FRIEND_APP", 0);
        userID = preferences.getInt("USER_ID", -1);
        currentUser = myHelper.getUserData(String.valueOf(userID));

        friendsList.add(new FriendItem(2,R.drawable.avatar_01, "Bentaleb Youssouf", "Bonsoire ! ", "9 min ago"));
        //friendsList.add(new FriendItem(R.drawable.avatar_01, "Bentaleb Youssouf", "Bonsoire ! ", "9 min ago"));
        //friendsList.add(new FriendItem(R.drawable.avatar_01, "Bentaleb Youssouf", "Bonsoire ! ", "9 min ago"));
        adapter = new FriendAdapter(getActivity(), R.layout.friend_item, friendsList);
        friendLV.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : change this with the correspond fragment
                Intent intent = new Intent(getActivity(), FriendRequestActivity.class);
                startActivity(intent);
            }
        });

    }
}
