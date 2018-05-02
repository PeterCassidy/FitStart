package csp15cap.fitstart;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;


    private TextView navHeaderUsername;
    private ImageView navHeaderProfileImage;

    private Boolean todayChallengeExists;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat DbDateFormat = new SimpleDateFormat("yyyyMMdd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up toolbar
        mToolbar = findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        //set up navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set nav menu header
        navigationView = findViewById(R.id.nav_view_main);
        navigationView.inflateHeaderView(R.layout.nav_header);
        View header = navigationView.getHeaderView(0);
        navHeaderUsername = header.findViewById(R.id.nav_username);
        navHeaderProfileImage = header.findViewById(R.id.nav_profile_pic);

        //set up fragment
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void NavMenuSelected(MenuItem item) {
        Fragment fragment = null;
        FragmentManager mFragManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragment = new HomeFragment();
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_friends:
                getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragment = new FriendsFragment();
                Toast.makeText(this, "Friends selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_meals:
                getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragment = new FoodFragment();
                Toast.makeText(this, "Meals selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_exercise:
                getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragment = new ExerciseFragment();
                Toast.makeText(this, "Exercise selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_challenge:
                getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                if(todayChallengeExists){
                    fragment = new DailyChallengeSelectedFragment();
                }else {
                    fragment = new DailyChallengeFragment();
                }
                Toast.makeText(this, "Challenges selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_profile:
                getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragment = new ProfileFragment();
                Toast.makeText(this, "Profile Settings selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:

                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
                LogUserOut();
                break;

            default:
                break;

        }
        drawerLayout.closeDrawer(3);
        if (fragment != null) {
            mFragManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("Home").commit();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        String selectedDate = DbDateFormat.format(c.getTime());



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavMenuSelected(item);
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        //if no user logged in sent to login activity.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendUserToLoginActivity();
        } else {
            String CurrentUUID = mAuth.getCurrentUser().getUid();
            mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUUID);
            mDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("user_name").getValue().toString();
                        String imageURL = "placeholder";
                        if(dataSnapshot.child("profile_image").exists()) {
                             imageURL = dataSnapshot.child("profile_image").getValue().toString();
                        }
                        navHeaderUsername.setText(name);
                        Picasso.get().load(imageURL).placeholder(R.drawable.common_google_signin_btn_icon_light).into(navHeaderProfileImage);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("DailyChallenges").child(CurrentUUID).child(selectedDate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                    todayChallengeExists = true;
                    }else{
                        todayChallengeExists = false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);

    }


    private void LogUserOut() {
        mAuth.signOut();
        sendUserToLoginActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
