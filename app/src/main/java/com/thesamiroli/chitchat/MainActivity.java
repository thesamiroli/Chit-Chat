package com.thesamiroli.chitchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    DatabaseReference userRef;

    //Android
    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        mAuth = FirebaseAuth.getInstance();
        //String uid = mAuth.getCurrentUser().getUid();
        //Pointing the userRef to current user's UID
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggler = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggler);
        mToggler.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                int items = item.getItemId();
                if (items == R.id.menu_logout) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser != null){
                        userRef.child("presence").setValue("offline");
                    }
                    FirebaseAuth.getInstance().signOut();
                    Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
                    startActivity(startIntent);
                    finish();

                }

                else if(items == R.id.menu_settings){
                    Intent settingsIntent = new Intent(MainActivity.this, Settings.class);
                    startActivity(settingsIntent);

                }
                else if(items == R.id.menu_profile){
                    Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                    startActivity(profileIntent);

                }
                else{

                    Intent usersIntent = new Intent(MainActivity.this, Users.class);
                    startActivity(usersIntent);
                }
                return true;
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //If user has not signed him, send him to the welcome page
        if(currentUser == null){
            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
        }
        else {
           userRef.child("presence").setValue("online");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggler.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            userRef.child("presence").setValue("offline");
        }

    }
}
