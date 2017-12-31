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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
        final ArrayList<FriendItem> friendsList;
        MyHelper myHelper = new MyHelper(getContext());
        SharedPreferences preferences;
        preferences = getActivity().getSharedPreferences("FRIEND_APP", 0);
        userID = preferences.getInt("USER_ID", -1);
        currentUser = myHelper.getUserData(userID);

        friendsList = myHelper.getAllFriends(String.valueOf(userID));
        //friendsList.add(new FriendItem(2,R.drawable.avatar_01, "Bentaleb Youssouf", "Bonsoire ! ", "9 min ago"));
        //friendsList.add(new FriendItem(R.drawable.avatar_01, "Bentaleb Youssouf", "Bonsoire ! ", "9 min ago"));
        //friendsList.add(new FriendItem(R.drawable.avatar_01, "Bentaleb Youssouf", "Bonsoire ! ", "9 min ago"));
        adapter = new FriendAdapter(getActivity(), R.layout.friend_item, friendsList);
        friendLV.setAdapter(adapter);


        friendLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FRIEND_NAME", friendsList.get(i).friendName);
                bundle.putInt("FRIEND_ID", friendsList.get(i).friendID);
                Toast.makeText(getContext(), " FRIEND_ID = " + friendsList.get(i).friendID, Toast.LENGTH_SHORT).show();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDialog addDialog = new AddDialog(getActivity());
                addDialog.show();
            }
        });

    }
}
