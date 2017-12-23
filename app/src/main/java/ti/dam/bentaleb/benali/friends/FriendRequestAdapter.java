package ti.dam.bentaleb.benali.friends;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bentaleb Youssouf on 12/23/2017.
 */

public class FriendRequestAdapter extends ArrayAdapter<FriendRequest> {
    Context activity;
    int resource;
    ArrayList<FriendRequest> friends;
    ImageView profileImg;
    TextView friendName;
    Button confirmBtn;

    public FriendRequestAdapter(Activity activity, int resource, ArrayList<FriendRequest> friends) {
        super(activity, resource, friends);
        this.activity = activity;
        this.resource = resource;
        this.friends = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            view = inflater.inflate(resource, parent, false);
        }
        profileImg = view.findViewById(R.id.profile_image);
        friendName = view.findViewById(R.id.friend_name);
        confirmBtn = view.findViewById(R.id.confirm_btn);

        friendName.setText(friends.get(position).name);
        profileImg.setImageResource(friends.get(position).profileImg);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add the friend
                Toast.makeText(activity,"Confirm Btn clicked !!" ,Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }
}
