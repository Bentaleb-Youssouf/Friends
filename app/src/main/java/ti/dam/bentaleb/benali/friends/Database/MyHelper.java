package ti.dam.bentaleb.benali.friends.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.support.v7.util.AsyncListUtil;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import ti.dam.bentaleb.benali.friends.ChatActivity;
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
        //username is unique per user
        sqLiteDatabase.execSQL("CREATE TABLE " + users_table +
                " (id_user INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "username TEXT UNIQUE , password TEXT," +
                "first_name TEXT, last_name TEXT," +
                "email TEXT, " +
                "profile_img INTEGER )");


        /*
        id_sender   => the sender of the friend request
        id_friend => the user that i want to be friend with him
        confirmed    => mean the request is accepted or not (boolean 0/1)
         */
        sqLiteDatabase.execSQL("CREATE TABLE " + friend_table +
                " (id_friend INTEGER  , " +
                "sender_id INTEGER , " +
                "confirmed INTEGER , " +
                "PRIMARY KEY (id_friend, sender_id)," +
                "FOREIGN KEY (sender_id) REFERENCES " + users_table + " (id_user) )");


        sqLiteDatabase.execSQL("CREATE TABLE " + msg_table +
                " (id_msg INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content TEXT, " +
                "date TEXT, " +
                "sender_id INTEGER," +
                "receiver_id INTEGER," +
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


    public void addFriendRequest(Friend f) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_friend", f.friendID);
        values.put("sender_id", f.senderID);
        values.put("confirmed", 0);


        db.insert(friend_table, null, values);

    }


    //this method confirm the friend request => make the confirmed attr = 1
    public void confirmFriendRequest(Friend f) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("confirmed", 1);
        db.update(friend_table, values,
                "sender_id = ? and id_friend = ? ",
                new String[]{
                        String.valueOf(f.senderID),
                        String.valueOf(f.friendID)}
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
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FriendItem> friends = new ArrayList<>();

        //this query select all friends that they are already friends with the current user
        String selectAllFriends = "SELECT id_friend , sender_id FROM " + friend_table + " WHERE sender_id = " + userID + " AND confirmed=1 OR id_friend = " + userID + " AND confirmed=1";
        Cursor friendsCursor = db.rawQuery(selectAllFriends, null);
        friendsCursor.moveToFirst();
        if (friendsCursor != null) {

            while (friendsCursor.isAfterLast() == false) {
                int friendID = friendsCursor.getInt(friendsCursor.getColumnIndex("id_friend"));

                //this check verify if the currentUser isn't the sender of the request but he accept the request before
                //so he is friend_id = currentUser and confirmed = 1
                //so we don't need his info but we need the sender information
                if (Integer.valueOf(userID) == friendID)
                    friendID = friendsCursor.getInt(friendsCursor.getColumnIndex("sender_id"));

                String selectFriendDetail = "SELECT * FROM " + users_table + " WHERE id_user = ?";
                Cursor usersCursor = db.rawQuery(selectFriendDetail, new String[]{String.valueOf(friendID)});
                usersCursor.moveToFirst();
                while (usersCursor.isAfterLast() == false) {
                    String selectMsgQuery = "SELECT * FROM " + msg_table +
                            " WHERE sender_id = " + userID + " AND  receiver_id = " + friendID +
                            " OR sender_id=" + friendID + " AND receiver_id= " + userID + " ORDER BY date DESC Limit 1";
                    Cursor msgCursor = db.rawQuery(selectMsgQuery, null);
                    msgCursor.moveToFirst();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
                    String timeNow = df.format(Calendar.getInstance().getTime());
                    if (msgCursor.isFirst()) {
                        friends.add(new FriendItem(
                                friendID,
                                usersCursor.getInt(usersCursor.getColumnIndex("profile_img")),
                                usersCursor.getString(usersCursor.getColumnIndex("first_name")) + " " +
                                        usersCursor.getString(usersCursor.getColumnIndex("last_name")),
                                msgCursor.getString(msgCursor.getColumnIndex("content")),
                                ChatActivity.diffDates(msgCursor.getString(msgCursor.getColumnIndex("date")), timeNow)
                        ));
                    } else {
                        //this case is when you add a friend for the first time & u don't have any messages with him
                        friends.add(new FriendItem(
                                friendID,
                                usersCursor.getInt(usersCursor.getColumnIndex("profile_img")),
                                usersCursor.getString(usersCursor.getColumnIndex("first_name")) + " " +
                                        usersCursor.getString(usersCursor.getColumnIndex("last_name")),
                                "Tap here to start conversation",
                                " -- "
                        ));
                    }
                    msgCursor.close();
                    usersCursor.moveToNext();
                }
                usersCursor.close();
                friendsCursor.moveToNext();
            }
        }
        friendsCursor.close();

        return friends;
    }


    // create message
    // TODO: pass  senderID to create method and  pass id by intent
    public void createMessage(Message m) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", m.content);
        values.put("date", m.date);
        values.put("sender_id", m.senderID);
        values.put("receiver_id", m.receiverID);
        db.insert(msg_table, null, values);

    }

    // update the spciefed message
    public int updateMessage(Message m) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", m.content);
        values.put("date", m.date);
        values.put("sender_id", m.senderID);
        values.put("receiver_id", m.senderID);

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


        /*db.execSQL("INSERT INTO " + users_table + " VALUES ( " +
                "null ," +
                '"' + user.username + '"' +
                "," + '"' + user.password + '"' +
                "," + '"' + user.firstName + '"' +
                "," + '"' + user.lastName + '"' +
                "," + '"' + user.email + '"' +
                "," + '"' + user.profile_img + '"' + ")");*/

        db.insert(users_table, null, values);
         /*
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

    public User getUserData(int userID) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT username, password, first_name, last_name, email, profile_img FROM "
                + users_table + " WHERE id_user = " + userID;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.isFirst()) {
            User user = new User(
                    userID,
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            );
            cursor.close();

            return user;
        } else {
            cursor.close();

            return null;
        }
    }

    public void updateUserData(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.username);
        contentValues.put("password", user.password);
        contentValues.put("first_name", user.firstName);
        contentValues.put("last_name", user.lastName);
        contentValues.put("email", user.email);

        db.update(users_table, contentValues, "id_user = ?", new String[]{String.valueOf(user.userID)});


    }

    public boolean checkUserExist(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT username , password FROM " + users_table + " WHERE username = '" + username + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.isAfterLast()) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }

    }

    //This method return true when the user exist
    public boolean checkUserData(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT username , password FROM " + users_table + " WHERE username = '" + username + "' AND password ='" + password + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.isAfterLast()) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }

    }

    public ArrayList getAllMessage(int userID, int friendID) {

        ArrayList<Message> messages = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + msg_table +
                " WHERE sender_id =" + userID + " AND receiver_id =" + friendID +
                " OR sender_id =" + friendID + " AND receiver_id=" + userID + " ORDER BY date ASC";

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
                        cursor.getInt(cursor.getColumnIndex("sender_id")),
                        cursor.getInt(cursor.getColumnIndex("receiver_id"))
                ));
                cursor.moveToNext();
            }
        }
        cursor.close();

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


    //this method accept the current user id as parameter and return the list of unconfirmed requests
    public ArrayList<User> getFriendRequests(int userID) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<User> friendRequests = new ArrayList<>();

        //this request select all the friend request that are not confirmed yet
        String selectQuery = "SELECT id_friend , sender_id FROM " + friend_table +
                " WHERE " + " id_friend = " + userID +
                " AND confirmed = 0";

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null) {
            while (cursor.isAfterLast() == false) {
                String friendInfo = "SELECT  id_user ,username ,first_name,last_name,email,profile_img FROM " + users_table + " WHERE id_user = " + cursor.getInt(cursor.getColumnIndex("sender_id"));
                Cursor friendCursor = db.rawQuery(friendInfo, null);
                friendCursor.moveToFirst();

                friendRequests.add(new User(
                        friendCursor.getInt(friendCursor.getColumnIndex("id_user")),
                        friendCursor.getString(friendCursor.getColumnIndex("username")),
                        "",
                        friendCursor.getString(friendCursor.getColumnIndex("first_name")),
                        friendCursor.getString(friendCursor.getColumnIndex("last_name")),
                        friendCursor.getString(friendCursor.getColumnIndex("email")),
                        friendCursor.getInt(friendCursor.getColumnIndex("profile_img"))
                ));

                cursor.moveToNext();
            }
        }
        cursor.close();


        return friendRequests;
    }

    //This method return all the friends with the current user (confirmed requests)
    public ArrayList<User> myFriends(int userID) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<FriendItem> myFriends = getAllFriends(String.valueOf(userID));
        ArrayList<User> friendRequests = new ArrayList<>();

        for (int i = 0; i < myFriends.size(); i++) {
            int friendID = myFriends.get(i).friendID;
            String selectQuery = "SELECT id_user, username, first_name, last_name, email, profile_img FROM " + users_table
                    + " WHERE id_user = " + friendID;
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            friendRequests.add(new User(
                    friendID,
                    cursor.getString(cursor.getColumnIndex("username")), "",
                    cursor.getString(cursor.getColumnIndex("first_name")),
                    cursor.getString(cursor.getColumnIndex("last_name")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getInt(cursor.getColumnIndex("profile_img"))
            ));
        }
        return friendRequests;
    }

    //this method check if you the other friend is already sent a request
    public boolean checkRequestExist(int userID, int friendID) {
        SQLiteDatabase db = getReadableDatabase();
        String checkQuery = "SELECT * FROM " + friend_table + " WHERE sender_id =" + userID + " AND id_friend =" + friendID
                + " OR sender_id= " + friendID + " AND id_friend=" + userID + " AND confirmed = 0";
        //Toast.makeText(context, checkQuery, Toast.LENGTH_LONG).show();

        Cursor cursor = db.rawQuery(checkQuery, null);
        cursor.moveToFirst();

        if (cursor.isFirst())
            return true;
        else
            return false;

    }
    /*
    public ArrayList<User> getFriendRequestsConfirmed(int userID) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<User> friendRequests = new ArrayList<>();

        //this request select all the friend request that are confirmed
        String selectQuery = "SELECT id_friend , sender_id FROM " + friend_table +
                " WHERE " + " sender_id =" + userID +
                " AND confirmed = 1";

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor != null) {
            while (cursor.isAfterLast() == false) {
                String friendInfo = "SELECT id_user ,profile_img, first_name,last_name, email FROM " + users_table +
                        " WHERE id_user = " + cursor.getInt(cursor.getColumnIndex("id_friend"));
                Cursor friendCursor = db.rawQuery(friendInfo, null);
                friendCursor.moveToFirst();
                friendRequests.add(new User(
                        friendCursor.getInt(friendCursor.getColumnIndex("id_user")),
                        "", "",
                        friendCursor.getString(friendCursor.getColumnIndex("first_name")),
                        friendCursor.getString(friendCursor.getColumnIndex("last_name")),
                        friendCursor.getString(friendCursor.getColumnIndex("email")),
                        friendCursor.getInt(friendCursor.getColumnIndex("profile_img"))
                ));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return friendRequests;
    }
    */

    //This method get all the users and subtract from them already friend
    //so it returns only the users i can send them an invitation
    public ArrayList getRequests(int userID) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<User> myFriends = myFriends(userID);
        ArrayList<User> allUsers = new ArrayList<>();

        String selectAllUsers = "SELECT  id_user,username, profile_img, first_name, last_name,email FROM " + users_table;
        Cursor cursor = db.rawQuery(selectAllUsers, null);
        cursor.moveToFirst();

        if (cursor != null) {
            while (cursor.isAfterLast() == false) {

                allUsers.add(new User(
                        cursor.getInt(cursor.getColumnIndex("id_user")),
                        cursor.getString(cursor.getColumnIndex("username")), "",
                        cursor.getString(cursor.getColumnIndex("first_name")),
                        cursor.getString(cursor.getColumnIndex("last_name")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getInt(cursor.getColumnIndex("profile_img"))
                ));
                cursor.moveToNext();
            }
        }
        cursor.close();


        //this to delete the current user's friends from the all users list
        boolean loop = true;
        while (loop == true) {
            loop = false;
            for (int i = 0; i < allUsers.size(); i++) {
                for (int j = 0; j < myFriends.size(); j++) {
//                Toast.makeText(context, "FRIEND = " + allUsers.get(i).userID + " USER ID = " + userID, Toast.LENGTH_SHORT).show();
                    if (allUsers.get(i).userID == myFriends.get(j).userID) {
                        allUsers.remove(i);
                        loop = true;
                    }
                }
            }
        }

        //this is to delete the users that they're send already a request before
        boolean ok = true;
        while (ok == true) {
            ok = false;
            for (int i = 0; i < allUsers.size(); i++) {
                if (checkRequestExist(allUsers.get(i).userID, userID)) {
                    ok = true;
                    allUsers.remove(i);
                }
            }
        }

        //this is for delete the current user from the all users list
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).userID == userID)
                allUsers.remove(i);
        }


        //currentUserCursor.close();
        //closeDB();
        return allUsers;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

/*    public void confirmFriendRequest(int userID,int friendID){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("confirmed",1);
        db.update(friend_table,values,"user_id = ? AND friendID = ?",new String[]{
                    String.valueOf(userID),String.valueOf(friendID)
        });

    }*/

}
