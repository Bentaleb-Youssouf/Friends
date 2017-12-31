package ti.dam.bentaleb.benali.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FriendAdapter extends ArrayAdapter<FriendItem> {

    Activity activity;
    int resource;
    ArrayList<FriendItem> friends;

    public FriendAdapter(Activity activity, int resource, ArrayList<FriendItem> friends) {
        super(activity, resource, friends);
        this.activity=activity;
        this.resource=resource;
        this.friends=friends;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(resource, parent, false);
        }

        final TextView profileName = view.findViewById(R.id.profile_name);
        final TextView lastMsg = view.findViewById(R.id.lastMsg);
        final TextView lastMsgTime = view.findViewById(R.id.lastMsgTime);
        final ImageView profileImg = view.findViewById(R.id.profile_image);

        profileName.setText(friends.get(position).friendName);
        lastMsg.setText(friends.get(position).lastMsg);
        lastMsgTime.setText(friends.get(position).lastMsgTime);
        profileImg.setImageResource(friends.get(position).friendImg);


        return view;
    }
}

