package com.example.swainstha.worldcup.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.swainstha.worldcup.MainActivity;
import com.example.swainstha.worldcup.R;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Login extends AppCompatActivity {

    ImageView imageView;
    EditText username;
    EditText password;
    Button sign;
    JSONObject user_detail= new JSONObject();
    Toolbar toolbar;
    private Socket socket;
    private String urlString="http://192.168.1.119:3001";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try{
            socket = IO.socket(urlString);
            socket.connect();
            socket.emit("joinAndroid");
            socket.on("loginSuccess", new Emitter.Listener() {

                @Override
                public void call(Object... args) {



                    JSONObject user_det = (JSONObject)args[0];
                    try {
                        final String id = user_det.get("id").toString();
                        final String valid_user=user_det.get("result").toString();
                        Log.i("info", user_det.get("id").toString());
                       Log.i("info", user_det.get("result").toString());
                       //Toast.makeText(getApplicationContext(),user_det.get("result").toString()+user_det.get("id").toString(), Toast.LENGTH_LONG).show();
                        if(valid_user.equals("True"))
                        {
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            Log.i("info","new activity started, logged in");
                            startActivity(i);

                        }

                    }
                   catch (Exception e)
                   {
                        Log.i("error",e.toString());
                   }

                }


            });
        }
        catch (Exception e)
        {
            Log.i("error",e.toString());
        }

        imageView= (ImageView) findViewById(R.id.imageView);
        username= (EditText) findViewById(R.id.editText);
        password= (EditText) findViewById(R.id.editText2);
        sign= (Button) findViewById(R.id.button);

    }

    public void signUpORLogIn(View view){
        try {
            user_detail.put("username", username.getText().toString());
            user_detail.put("password",password.getText().toString());

            socket.emit("login",user_detail);
            //socket.emit("joinAndroid","1");

            //Toast.makeText(getApplicationContext(),"socket",Toast.LENGTH_LONG).show();


        }
        catch (Exception e)
        {
            Log.i("error",e.toString());
        }

        //socket.emit("password",password.getText().toString());
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

        }

        return super.onOptionsItemSelected(item);

    }

}



