package ti.dam.bentaleb.benali.friends.Login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import ti.dam.bentaleb.benali.friends.FriendActivity;
import ti.dam.bentaleb.benali.friends.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        //LinearLayout signUpLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutSignUp);
        //signUpLinearLayout.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SharedPreferences preferences;
        preferences = getSharedPreferences("FRIEND_APP", 0);
        int userID = preferences.getInt("USER_ID", -1);

        if (userID == -1) {
            setContentView(R.layout.activity_login);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();

                }
            });

            Toast.makeText(this, "USER DOESN'T EXIST BEFORE !!", Toast.LENGTH_SHORT).show();
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, loginFragment)
                    .setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
                    .commit();
        } else {

            Toast.makeText(this, "USER EXIST !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, FriendActivity.class);
            startActivity(intent);
            finish();
        }


        /*fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, loginFragment);
        fragmentTransaction.commit();*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }



    public void signUpBtn(View view) {
        SignUpFragment signUpFragment = new SignUpFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, signUpFragment)
                .addToBackStack(null)
                .commit();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //LinearLayout signUpLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutSignUp);
        //signUpLinearLayout.setVisibility(View.GONE);

        /*fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        fragmentTransaction.replace(R.id.fragment_container,signUpFragment);
        //fragmentTransaction.commit();**
        */
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




   /* public void loginCheck(View view){
        String userText = userEd.getText().toString(),
               passText = passEd.getText().toString();

        if(userText.equals("") || passText.equals("")){
            Toast.makeText(this,"Insérer des valeurs S.V.P !" , Toast.LENGTH_SHORT).show();
            Log.e("LoginActivity","Les champs vide !");
        }else{
            if(userText.equals(USERNAME) && passText.equals(PASSWORD)){
                Intent intent = new Intent(this, FriendActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("USERNAME",USERNAME);
                bundle.putString("PASSWORD",PASSWORD);
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Toast.makeText(this,"Les informations sont incorrect !" , Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity","Les informations sont incorrect !");
            }

        }

    }
    */
}
