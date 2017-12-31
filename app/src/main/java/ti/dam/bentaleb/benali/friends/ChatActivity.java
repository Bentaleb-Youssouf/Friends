package ti.dam.bentaleb.benali.friends;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ti.dam.bentaleb.benali.friends.Database.Message;
import ti.dam.bentaleb.benali.friends.Database.MyHelper;

public class ChatActivity extends AppCompatActivity {

    int userID, friendID;
    RecyclerView recycle;
    List<Message> messageList = new ArrayList<>();
    ChatAdapter adapter;
    ImageButton send;
    MyHelper helper;
    Toolbar toolbar;
    EditText messageED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        helper = new MyHelper(getBaseContext());
        messageED = (EditText) findViewById(R.id.messageED);
        send = (ImageButton) findViewById(R.id.sendMsg);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("FRIEND_NAME"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("FRIEND_APP", 0);
        userID = sharedPreferences.getInt("USER_ID", -1);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        friendID = bundle.getInt("FRIEND_ID");
        Toast.makeText(this, "USER_ID = " + userID + " FRIEND_ID = " + friendID, Toast.LENGTH_SHORT).show();

        messageList = helper.getAllMessage(userID, friendID);

        recycle = (RecyclerView) findViewById(R.id.recycle);

        /*messageList.add(new Message(01, "BlablaBlablaBlablaBlabla", "2017",userID));
        messageList.add(new Message(02, "BlablaBlablaBlablaBlablBlablaBlablaBlablaBlablaaBlablaBlablaBlablaBlablaBlabla", "2017",friendID));
        messageList.add(new Message(03, "Ba", "2017",userID));
        messageList.add(new Message(03, "BlabBlablala", "2017",userID));
        messageList.add(new Message(04, "BlaBlablaBlablabla", "2017",friendID));
        messageList.add(new Message(04, "Blabla", "2017",friendID));
        messageList.add(new Message(04, "BlBlablaabla", "2017",friendID));*/
        adapter = new ChatAdapter(messageList, ChatActivity.this);

        // 2. set layoutManger
        recycle.setLayoutManager(new LinearLayoutManager(this));

        // 5. set item animator to DefaultAnimator
        recycle.setItemAnimator(new DefaultItemAnimator());

        // 4. set adapter
        recycle.setAdapter(adapter);


    }


    public void sendMessage(View view) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        Toast.makeText(getBaseContext(), "Sent at = " + date, Toast.LENGTH_SHORT).show();

        Message message = new Message(0, messageED.getText().toString(), date, userID, friendID);
        helper.createMessage(message);
        //messageList.clear();
        //messageList = helper.getAllMessage(userID,friendID);
        messageList.add(message);
        adapter.notifyItemInserted(messageList.size()-1);
        recycle.scrollToPosition(adapter.getItemCount() - 1);
        //adapter.notifyItemInserted(messageList.indexOf(message)-1);
        //adapter.notifyItemRangeInserted(messageList.indexOf(message),1);
        messageED.setText("");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static String diffDates(String dateStart, String dateStop) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = df.parse(dateStart);
            d2 = df.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d2.getTime() - d1.getTime();
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);

        if (diffHours == 0) {
            if (diffMinutes == 0) {
                return "A l'instant";
            } else {
                return "Il ya " + diffMinutes + " min";
            }
        } else {
            return "Il ya " + diffHours + " h";
        }

    }
}
