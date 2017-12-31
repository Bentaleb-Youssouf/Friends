package ti.dam.bentaleb.benali.friends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ti.dam.bentaleb.benali.friends.Database.MyHelper;
import ti.dam.bentaleb.benali.friends.Database.User;
import ti.dam.bentaleb.benali.friends.Login.LoginActivity;

public class FriendActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView name, email;
    ImageView profileImg;
    User currentUser;
    int userID;
    private String[] activityTitles;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_FRIEND_REQUEST = "friend_request";
    private static final String TAG_PROFILE = "my_profile";
    public static String CURRENT_TAG = TAG_HOME;
    View headerView;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        MyHelper myHelper = new MyHelper(this);

        SharedPreferences preferences;
        preferences = getSharedPreferences("FRIEND_APP", 0);
        userID = preferences.getInt("USER_ID", -1);
        currentUser = myHelper.getUserData(userID);

        //Those lines refresh the nav drawer detail from the current user logged in
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // profile
                ProfileFragment photosFragment = new ProfileFragment();
                return photosFragment;
            case 2:
                // friend requests
                FriendRequestFragment friendRequestFragment = new FriendRequestFragment();
                return friendRequestFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app

        // update the main rightContent by replacing fragments
        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frameLayout, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();

        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    //this method set the user data into the navDrawer
    private void loadNavHeader() {
        name = headerView.findViewById(R.id.profile_name_drawer);
        profileImg = headerView.findViewById(R.id.profile_image_drawer);
        email = headerView.findViewById(R.id.profile_email_drawer);
        email.setText(currentUser.email);
        name.setText(currentUser.firstName + " " + currentUser.lastName);
        profileImg.setImageResource(currentUser.profile_img);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Check to see which item was being clicked and perform appropriate action
        switch (id) {
            //Replacing the main rightContent with ContentFragment Which is our Inbox View;
            case R.id.home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                break;
            case R.id.my_profile:
                navItemIndex = 1;
                CURRENT_TAG = TAG_PROFILE;
                break;
            case R.id.friend_request:
                navItemIndex = 2;
                CURRENT_TAG = TAG_FRIEND_REQUEST;
                break;
            case R.id.about:
                // launch new intent instead of loading fragment
                //TODO : create the about activity and pass it here!
                AboutDialog aboutDialog = new AboutDialog(this);
                aboutDialog.show();
                Toast.makeText(getApplicationContext(),"About activity !",Toast.LENGTH_SHORT).show();

                drawer.closeDrawers();
                return true;
            case R.id.logout:
                Toast.makeText(getApplicationContext(),"Logout !",Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("FRIEND_APP", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                // launch new intent instead of loading fragment
                //startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                drawer.closeDrawers();
                return true;

            default:
                navItemIndex = 0;
        }

        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
        item.setChecked(true);

        loadHomeFragment();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
