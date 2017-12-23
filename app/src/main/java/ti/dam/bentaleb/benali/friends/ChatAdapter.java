package ti.dam.bentaleb.benali.friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ti.dam.bentaleb.benali.friends.Database.Message;

/**
 * Created by djame on 09/12/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> message;
    Context context;
    public TextView rightContent, leftContent,leftTime,rightTime;
    public LinearLayout leftLinearLayout, rightLinearLayout;


    public ChatAdapter(List<Message> message, Context context) {
        this.message = message;
        this.context = context;

    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            leftLinearLayout = itemLayoutView.findViewById(R.id.leftMsg);
            rightLinearLayout = itemLayoutView.findViewById(R.id.rightMsg);
            rightContent = itemLayoutView.findViewById(R.id.messageRight);
            leftContent = itemLayoutView.findViewById(R.id.messageLeft);
            leftTime= itemLayoutView.findViewById(R.id.timeLeft);
            rightTime= itemLayoutView.findViewById(R.id.timeRight);

        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemLayoutView = inflater.inflate(R.layout.pattren_msg, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        //this option is to resolve the error of recycling the views and messed up the adapter
        //not a correct way but it did the work for now
        viewHolder.setIsRecyclable(false);

        Message item = message.get(position);
        int senderID = message.get(position).senderID;
        SharedPreferences sharedPreferences = context.getSharedPreferences("FRIEND_APP", 0);
        int userID = sharedPreferences.getInt("USER_ID", -1);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        String timeNow = df.format(Calendar.getInstance().getTime());
        String diffTime = ChatActivity.diffDates(item.date,timeNow);

        if (userID == senderID) {
            leftLinearLayout.setVisibility(View.INVISIBLE);
            rightLinearLayout.setVisibility(View.VISIBLE);
            rightContent.setText(item.content);
            rightTime.setText(diffTime);

        } else {
            leftLinearLayout.setVisibility(View.VISIBLE);
            rightLinearLayout.setVisibility(View.INVISIBLE);
            leftContent.setText(item.content);
            leftTime.setText(diffTime);
        }


        /*
        final String txt = message.get(position).content;
        rightContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
            }
        });
        */

    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return message.size();
    }

}
