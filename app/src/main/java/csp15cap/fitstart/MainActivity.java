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

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;


    private TextView tempTextView;

    private TextView navHeaderUsername;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout = findViewById(R.id.drawer_layout_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_view_main);
        navigationView.inflateHeaderView(R.layout.nav_header);
        View header = navigationView.getHeaderView(0);


        navHeaderUsername = header.findViewById(R.id.nav_username);
        //set up fragment
        if(findViewById(R.id.fragment_container) !=null){
            if(savedInstanceState != null){
                return;
            }
        }

        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();






        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavMenuSelected(item);
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void NavMenuSelected(MenuItem item) {
        Fragment fragment = new HomeFragment();
        FragmentManager mFragManager = getSupportFragmentManager();
        switch(item.getItemId()){
            case R.id.nav_home:
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                break;


            case R.id.nav_friends:
                fragment = new FriendsFragment();
                Toast.makeText(this, "Friends selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_meals:
                fragment = new FoodFragment();
                Toast.makeText(this, "Meals selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_exercise:

                fragment = new ExerciseFragment();
                Toast.makeText(this, "Exercise selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
                LogUserOut();
                break;

            default:

        }
        drawerLayout.closeDrawer(3);
        mFragManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();


    }



    @Override
    protected void onStart() {
        super.onStart();

        //if no user logged in sent to login activity.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            sendUserToLoginActivity();
        }else{
            //temp implementation to test DB access
            String CurrentUUID = mAuth.getCurrentUser().getUid();
            mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUUID);
            mDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("user_name").getValue().toString();
                    navHeaderUsername.setText(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent (MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);

    }


    private void LogUserOut() {
        mAuth.signOut();
        sendUserToLoginActivity();
    }
}
