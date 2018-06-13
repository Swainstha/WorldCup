package com.example.sa.socket_final_test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sa.socket_final_test.Login.Login;
import com.example.sa.socket_final_test.UIFragments.BetFragment;

import com.example.sa.socket_final_test.MyService;
import com.example.sa.socket_final_test.UIFragments.BetFragment;
import com.example.sa.socket_final_test.UIFragments.CommonFragment;
import com.example.sa.socket_final_test.UIFragments.GroupRankFragment;
import com.example.sa.socket_final_test.UIFragments.PositionFragment;
import com.example.sa.socket_final_test.UIFragments.MatchFragment;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class MainActivity extends  AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,MatchFragment.BetArguments{

        Toolbar toolbar;
        String provider;

public MyService testService;
    boolean isBound =false;
    String identity;

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

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyLocalBinder binder = (MyService.MyLocalBinder) iBinder;
            testService =binder.getService();
            testService.gettheSocket().emit("login", "dkljfl");
            //Log.i("info", String.valueOf(isBound));
            Toast.makeText(getApplicationContext(),String.valueOf(MyService.getMyService()),Toast.LENGTH_SHORT).show();
            final MyService a=testService;
            Log.i("INFO",testService.toString());
            isBound=true;


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            Toast.makeText(getApplicationContext(),"disconnected binder",Toast.LENGTH_SHORT).show();
            isBound=false;
        }
    };

    public class GetService extends AsyncTask<MyService,Void,String> {

        @Override
        protected String doInBackground(MyService... myServices) {
            myServices[0].gettheSocket().emit("login", "dkljfl");
            return "success";
        }
    }



    public void showTime(View view )
    {
        String currentTime = testService.getCurrentTime();
       testService.gettheSocket().emit("login", "dkljfl");
        TextView textView=(TextView)findViewById(R.id.textView);
        textView.setText(currentTime);
    }
    public void emitter(View view, String event)
    {
        Toast.makeText(getApplicationContext(),MyService.getMyService().toString(),Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(SaveSharedPreference.getUserID(MainActivity.this).length() == 0)
        {
            Intent intent_login = new Intent(this,Login.class);
            startActivity(intent_login);
        }
        else
        {
            Intent serviceIntent=new Intent(MainActivity.this, MyService.class);
            serviceIntent.putExtra("identity",identity);
            startService(serviceIntent);
        }

        //set the layout to activity_nav_drawer which contains the layout of activity_nav_drawer
        //and activity_main. Contentview is set to activity_nav_drawer but we should edit the layout activity_main
        super.onCreate(savedInstanceState);
        Intent intentId=getIntent();
        identity = intentId.getStringExtra("identity");
        setContentView(R.layout.activity_nav_drawer);

        // bindService(i, mConnection, Context.BIND_AUTO_CREATE);

        Toast.makeText(getApplicationContext(), String.valueOf(MyService.getMyService()), Toast.LENGTH_SHORT).show();




        //The app theme is set to noActionBar in styles.xml
        //toolbar is in activity_main.xml
        Toast.makeText(getApplicationContext(), String.valueOf(MyService.getMyService()), Toast.LENGTH_SHORT).show();

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
            SaveSharedPreference.clearUserID(MainActivity.this);
            Intent intent_login = new Intent(this,Login.class);
            stopService(new Intent(this,MyService.class));
            startActivity(intent_login);


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

