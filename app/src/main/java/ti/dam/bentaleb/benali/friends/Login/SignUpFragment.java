package ti.dam.bentaleb.benali.friends.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import ti.dam.bentaleb.benali.friends.Database.MyHelper;
import ti.dam.bentaleb.benali.friends.Database.User;
import ti.dam.bentaleb.benali.friends.FriendActivity;
import ti.dam.bentaleb.benali.friends.R;

/**
 * Created by Bentaleb Youssouf on 12/16/2017.
 */

public class SignUpFragment extends Fragment implements View.OnClickListener {
    View view;
    Button signUpBtn;
    EditText usernameEd, passwordEd, firstNameEd, lastNameEd, emailEd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        signUpBtn = (Button) view.findViewById(R.id.signUpBtn);
        usernameEd = (EditText) view.findViewById(R.id.usernameET);
        passwordEd = (EditText) view.findViewById(R.id.passwordET);
        firstNameEd = (EditText) view.findViewById(R.id.firstNameET);
        lastNameEd = (EditText) view.findViewById(R.id.lastNameET);
        emailEd = (EditText) view.findViewById(R.id.emailET);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signUpBtn.setOnClickListener(this);

    }

    public int randomProfileImg() {
        Random r = new Random();
        int imgID = r.nextInt(4) + 1;
        switch (imgID) {
            case 1:
                return R.drawable.avatar_01;
            case 2:
                return R.drawable.avatar_02;
            case 3:
                return R.drawable.avatar_03;
            case 4:
                return R.drawable.avatar_04;
            default:
                return R.drawable.avatar_01;
        }
    }

    @Override
    public void onClick(View view) {
        MyHelper myHelper = new MyHelper(getContext());
        User user = new User(
                usernameEd.getText().toString(),
                passwordEd.getText().toString(),
                firstNameEd.getText().toString(),
                lastNameEd.getText().toString(),
                emailEd.getText().toString(),
                randomProfileImg()
        );

        boolean res = myHelper.checkUserData(usernameEd.getText().toString(), passwordEd.getText().toString());
        if (res == false) {
            myHelper.addUser(user);
            int userID = myHelper.getUserID(usernameEd.getText().toString());
            Toast.makeText(getContext(), "USER IS ADDED !!", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPref;
            sharedPref = getActivity().getSharedPreferences("FRIEND_APP", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("USER_ID", userID);
            editor.commit();
            Intent intent = new Intent(getActivity(), FriendActivity.class);
            startActivity(intent);
            getActivity().finish();


        } else {
            Toast.makeText(getContext(), "USER IS ALREADY EXIST !!", Toast.LENGTH_SHORT).show();
        }

    }
}
