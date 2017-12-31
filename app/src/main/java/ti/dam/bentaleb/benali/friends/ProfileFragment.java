package ti.dam.bentaleb.benali.friends;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ti.dam.bentaleb.benali.friends.Database.MyHelper;
import ti.dam.bentaleb.benali.friends.Database.User;

/**
 * Created by Bentaleb Youssouf on 12/20/2017.
 */

public class ProfileFragment extends Fragment {

    Button updateInfoBtn;
    EditText usernameEd, passwordEd, firstNameEd, lastNameEd, emailEd;
    ImageView profileImg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_profile, container, false);
        updateInfoBtn = view.findViewById(R.id.updateInfo);
        usernameEd = view.findViewById(R.id.usernameET);
        passwordEd = view.findViewById(R.id.passwordET);
        firstNameEd = view.findViewById(R.id.firstNameET);
        lastNameEd = view.findViewById(R.id.lastNameET);
        emailEd = view.findViewById(R.id.emailET);
        profileImg = view.findViewById(R.id.profile_image);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MyHelper myHelper = new MyHelper(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("FRIEND_APP", 0);

        int userID = sharedPreferences.getInt("USER_ID", -1);
        final User thisUser = myHelper.getUserData(userID);

        usernameEd.setText(thisUser.username);
        passwordEd.setText(thisUser.password);
        firstNameEd.setText(thisUser.firstName);
        lastNameEd.setText(thisUser.lastName);
        emailEd.setText(thisUser.email);
        profileImg.setImageResource(thisUser.profile_img);


        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usernameEd.getText().toString().isEmpty())
                    usernameEd.setError("Empty !");

                if (passwordEd.getText().toString().isEmpty())
                    passwordEd.setError("Empty !");

                if (firstNameEd.getText().toString().isEmpty())
                    firstNameEd.setError("Empty !");


                if (lastNameEd.getText().toString().isEmpty())
                    lastNameEd.setError("Empty !");

                if (emailEd.getText().toString().isEmpty())
                    emailEd.setError("Empty !");


                if (firstNameEd.getText().toString().isEmpty() || passwordEd.getText().toString().isEmpty()
                        || usernameEd.getText().toString().isEmpty() || passwordEd.getText().toString().isEmpty()
                        || emailEd.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Fill the fields !", Toast.LENGTH_SHORT).show();
                } else {


                    User updatedUser = new User(
                            thisUser.userID,
                            usernameEd.getText().toString(),
                            passwordEd.getText().toString(),
                            firstNameEd.getText().toString(),
                            lastNameEd.getText().toString(),
                            emailEd.getText().toString(),
                            thisUser.profile_img
                    );
                    myHelper.updateUserData(updatedUser);
                    Toast.makeText(getContext(), "Profile updated successfully !", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
