package ti.dam.bentaleb.benali.friends.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ti.dam.bentaleb.benali.friends.FriendItem;
import ti.dam.bentaleb.benali.friends.FriendRequest;


public class MyHelper extends SQLiteOpenHelper {
    final String friend_table = "friend";
    final String msg_table = "message";
    final String users_table = "user";


    public static final String DATABASE_NAME = "friend.db";
    public static final int VERSION = 1;
    Context context;

    public MyHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
        //sqLiteDatabase = getWritableDatabase();
        // sqLiteDatabase=getWritableDatabase();
        //onUpgrade(sqLiteDatabase, VERSION, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + users_table +
                " (id_user INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "username TEXT , password TEXT," +
                "first_name TEXT, last_name TEXT," +
                "email TEXT, " +
                "profile_img INTEGER )");


        /*
        id_user   => the sender of the friend request
        id_friend => the user that i want to be friend with him
        confirmed    => mean the request is accepted or not
        confirmed 0
         */
        sqLiteDatabase.execSQL("CREATE TABLE " + friend_table +
                " (id_friend INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_user INTEGER , " +
                "confirmed INTEGER , " +
                "FOREIGN KEY (id_user) REFERENCES " + users_table + " (id_user) )");


        sqLiteDatabase.execSQL("CREATE TABLE " + msg_table +
                " (id_msg INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content TEXT, " +
                "date TEXT, " +
                "sender_id INTEGER," +
                "FOREIGN KEY (sender_id) REFERENCES friend(id_friend))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olVersion, int newVersion) {


        if (newVersion > olVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + friend_table);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + msg_table);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + users_table);
            onCreate(sqLiteDatabase);
            Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
        }
    }


    public void createFriend(Friend f) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_friend", f.friend_id);
        values.put("id_user", f.user_id);
        values.put("confirmed", f.confirmed);

        db.insert("friend", null, values);
    }

    //this method confirm the friend request => make the confirmed attr = 1
    public void confirmeRequest(Friend f) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("confirmed", 1);
        db.update(friend_table, values,
                "user_id = ? and friend_id = ? ",
                new String[]{
                        String.valueOf(f.user_id),
                        String.valueOf(f.friend_id)}
        );
    }


    /*
    public int updateFriend(Friend f) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", f.friendName);
        values.put("last_name", f.friendName);
        return db.update(friend_table, values, "first_name=" + f.friendName, null);
    }

    public int deleteFriend(FriendItem f) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(friend_table, "id =" + f.friendName, null);
    }
    */


    public ArrayList getAllFriends(String userID) {

        ArrayList<FriendItem> friends = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //this query select all friends that they are already friends with the current user
        String selectAllFriends = "SELECT id_friend , id_user FROM " + friend_table + " WHERE id_user = ? AND confirmed = 1";
        Cursor friendsCursor = db.rawQuery(selectAllFriends, new String[]{userID});
        friendsCursor.moveToFirst();
        while (friendsCursor.isAfterLast() == false) {
            int friendID = friendsCursor.getInt(friendsCursor.getColumnIndex("user_id"));
            String selectFriendDetail = "SELECT * FROM " + users_table + " WHERE id_user = ?";
            Cursor usersCursor = db.rawQuery(selectFriendDetail, new String[]{String.valueOf(friendID)});
            usersCursor.moveToFirst();
            while (usersCursor.isAfterLast() == false) {
                String selectMsgQuery = "SELECT * FROM " + msg_table + " WHERE sender_id = " + userID +
                        " OR  sender_id = " + friendID + " ORDER BY date DESC Limit 1";
                Cursor msgCursor = db.rawQuery(selectMsgQuery, null);
                msgCursor.moveToFirst();
                if (msgCursor.isFirst()) {
                    friends.add(new FriendItem(
                            Integer.valueOf(friendsCursor.getColumnIndex("id_friend")),
                            Integer.valueOf(usersCursor.getString(usersCursor.getColumnIndex("profile_img"))),
                            usersCursor.getString(usersCursor.getColumnIndex("first_name")) +
                                    usersCursor.getString(usersCursor.getColumnIndex("last_name")),
                            msgCursor.getString(msgCursor.getColumnIndex("content")),
                            msgCursor.getString(msgCursor.getColumnIndex("date"))
                    ));
                }
            }
        }
        return friends;
    }


    // create message
    // TODO: pass  sender_id to create method and  pass id by intent
    public void createMessage(Message m) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", m.content);
        values.put("date", m.date);
        values.put("sender_id", m.senderID);
        db.insert(msg_table, null, values);

    }

    // update the spciefed message
    public int updateMessage(Message m) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", m.content);
        values.put("date", m.date);
        values.put("sender_id", m.senderID);

        return db.update(msg_table, values, "id=" + m.id, null);
    }

    // delete the spceifed message
    public int deleteMessage(Message m) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(msg_table, "id =" + m.id, null);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.putNull("id_user");
        values.put("username", user.username);
        values.put("password", user.password);
        values.put("first_name", user.firstName);
        values.put("last_name", user.lastName);
        values.put("email", user.email);
        values.put("profile_img", user.profile_img);


        db.execSQL("INSERT INTO " + users_table + " VALUES ( " +
                "null ," +
                '"' + user.username + '"' +
                "," + '"' + user.password + '"' +
                "," + '"' + user.firstName + '"' +
                "," + '"' + user.lastName + '"' +
                "," + '"' + user.email + '"' +
                "," + '"' + user.profile_img + '"' + ")");

        /*long chk = db.insert(users_table, null, values);
        if(chk!=0){
            Toast.makeText(context, "Record added successfully",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "Record added failed...! ",Toast.LENGTH_LONG).show();
        }
        */
        //Toast.makeText(context,"User created in DB !" ,Toast.LENGTH_SHORT).show();

    }

    public int getUserID(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT id_user FROM " + users_table + " WHERE username =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{username});
        cursor.moveToFirst();
        if (cursor.isFirst()) {
            return cursor.getInt(cursor.getColumnIndex("id_user"));
        }
        return -1;
    }

    public User getUserData(String userID) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT username, password, first_name, last_name, email, profile_img FROM "
                + users_table + " WHERE id_user = " + userID;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.isFirst()) {
            User user = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            );
            return user;
        } else
            return null;
    }

    //This method return true when the user exist
    public boolean checkUserData(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT username , password FROM " + users_table + " WHERE username = '" + username + "' AND PASSWORD = '" + password + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.isAfterLast())
            return false;
        else
            return true;
    }

    public ArrayList getAllMessage(int userID, int friendID) {

        ArrayList<Message> messages = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + msg_table +
                " WHERE sender_id = " + userID + " OR sender_id = " + friendID + " ORDER BY date ASC";

        //Toast.makeText(context, "USER_ID = " + userID + " FRIEND_ID = " + friendID, Toast.LENGTH_SHORT).show();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null) {


            cursor.moveToFirst();
            //Toast.makeText(context,cursor.getInt(0)+ cursor.getString(1)+ cursor.getString(2) + cursor.getInt(3),Toast.LENGTH_SHORT).show();

            while (cursor.isAfterLast() == false && cursor != null) {
                messages.add(new Message(
                        cursor.getInt(cursor.getColumnIndex("id_msg")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getInt(cursor.getColumnIndex("sender_id"))
                ));
                cursor.moveToNext();
            }
        }

        return messages;
    }

    public void DeleteAllData() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase database = this.getReadableDatabase();
        database.execSQL("delete from  " + friend_table);
    }

    public int getMsgCount() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + msg_table;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public List getFriendRequests(Friend f) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<FriendRequest> friendRequests = new ArrayList<FriendRequest>() ;
        String selectQuery = "SELECT friend_id , user_id FROM " + friend_table +
                            " WHERE " + " friend_id =" + f.friend_id + " OR user_id =" + f.user_id ;
        Cursor cursor= db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        if ( cursor != null){
            while(cursor.isAfterLast() == false){

                friendRequests.add(new FriendRequest(1,""));
            }
        }
    }
}
