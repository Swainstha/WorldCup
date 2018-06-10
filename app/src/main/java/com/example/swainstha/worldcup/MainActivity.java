package com.example.swainstha.worldcup;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.swainstha.worldcup.UI.UIFragments.BetFragment;
import com.example.swainstha.worldcup.UI.UIFragments.CommonFragment;
import com.example.swainstha.worldcup.UI.UIFragments.GroupRankFragment;
import com.example.swainstha.worldcup.UI.UIFragments.PositionFragment;
import com.example.swainstha.worldcup.UI.UIFragments.MatchFragment;


public class MainActivity extends  AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,MatchFragment.BetArguments{

    Toolbar toolbar;
    String provider;

    @Override
    public void sendBetArguments(int id, String team1, String team2, String country1, String country2) {
        BetFragment betFragment = new BetFragment();
        toolbar.setTitle("Bet");
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("team1", team1);
        args.putString("team2", team2);
        args.putString("country1", country1);
        args.putString("country2", country2);
        betFragment.setArguments(args);
        loadFragment(betFragment);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //set the layout to activity_nav_drawer which contains the layout of activity_nav_drawer
        //and activity_main. Contentview is set to activity_nav_drawer but we should edit the layout activity_main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        //The app theme is set to noActionBar in styles.xml
        //toolbar is in activity_main.xml
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("News");

        //pointed to activity_nav_drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //if any item is selected in the navigation list, for that a listener is set onNavigationItemSelected(MenuItem item)
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initializing a bottombar navigation view with the bottombar view  and setting the callback function
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //callback function for bottombar navigation when items are selected
    //we can build different fragments and call them when items are selected
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_news:
                    toolbar.setTitle("News");
                    fragment = new CommonFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_matches:
                    toolbar.setTitle("Matches");
                    fragment = new MatchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_position:
                    toolbar.setTitle("Position");
                    fragment = new PositionFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_group_ranks:
                    toolbar.setTitle("Group Ranks");
                    fragment = new GroupRankFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_history:
                    toolbar.setTitle("My History");
                    fragment = new CommonFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }

    };

    //loading the different fragments when items of bottombar nav are selected
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_fragmentholder, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present. like MyProfile, Settings etc
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;

        } else if (id==R.id.Logout) {


        } else if(id == R.id.Heroku) {
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.link), "https://world-cup-server.herokuapp.com");
            editor.apply();


        } else if(id == R.id.Local) {
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.link), "http://192.168.1.119:3001");
            editor.apply();

        } else if(id == R.id.search_m) {
            super.onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_credits) {

        } else if (id == R.id.nav_logout) {

        }

        //when a item is selected in slide navigation, the drawer closes itself
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
