package ti.dam.bentaleb.benali.friends.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ti.dam.bentaleb.benali.friends.Database.MyHelper;
import ti.dam.bentaleb.benali.friends.FriendActivity;
import ti.dam.bentaleb.benali.friends.R;

/**
 * Created by Bentaleb Youssouf on 12/15/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
/*    private static final String USERNAME = "dam";
    private static final String PASSWORD = "123";*/

    private View view;
    private EditText userEd, passEd;
    private Button loginBtn, signUpBtn;
    MyHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        userEd = (EditText) view.findViewById(R.id.usernameET);
        passEd = (EditText) view.findViewById(R.id.passwordET);
        loginBtn = (Button) view.findViewById(R.id.loginBtn);
        signUpBtn = (Button) view.findViewById(R.id.signUpBtn);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        helper = new MyHelper(getContext());

        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String userText = userEd.getText().toString(),
                passText = passEd.getText().toString();

        boolean res = helper.checkUserData(userText, passText);

        if (userText.equals("") || passText.equals("")) {
            Toast.makeText(getActivity(), "Insérer des valeurs S.V.P !", Toast.LENGTH_SHORT).show();
            Log.e("LoginActivity", "Les champs sont vide !");
        } else {
            if (res == true) {
                Toast.makeText(getContext(), "USER DATA TRUE !!", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(getActivity(), FriendActivity.class);
                        Bundle bundle = new Bundle();
                        */
                int userID = helper.getUserID(userText);
                SharedPreferences sharedPref;
                sharedPref = getActivity().getSharedPreferences("FRIEND_APP", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("USER_ID", userID);
                editor.commit();
                Intent intent = new Intent(getActivity(), FriendActivity.class);
                startActivity(intent);
                getActivity().finish();
                        /*
                        intent.putExtras(bundle);
                        startActivity(intent);*/
            } else {
                Toast.makeText(getActivity(), "Les informations sont incorrect !", Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Les informations sont incorrect !");
            }

        }
    }

    /*
    private void setListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userText = userEd.getText().toString(),
                        passText = passEd.getText().toString();

                if(userText.equals("") || passText.equals("")){
                    Toast.makeText(getActivity(),"Insérer des valeurs S.V.P !" , Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity","Les champs vide !");
                }else{
                    if(userText.equals(USERNAME) && passText.equals(PASSWORD)){
                        Intent intent = new Intent(getActivity(), FriendActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("USERNAME",USERNAME);
                        bundle.putString("PASSWORD",PASSWORD);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(),"Les informations sont incorrect !" , Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity","Les informations sont incorrect !");
                    }

                }

            }
        });

    }
    */
}
