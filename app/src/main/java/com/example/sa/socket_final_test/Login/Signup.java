package com.example.sa.socket_final_test.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sa.socket_final_test.Adapters.SocketService;
import com.example.sa.socket_final_test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by sa on 6/11/18.
 */

public class Signup extends AppCompatActivity{

    EditText username;
    EditText name;
    EditText password;
    EditText phone;
    EditText confirm;
    EditText email;
    Button signup;
    SocketService mSocket;
    Socket socket;

    String st_username, st_password,st_name,st_email,st_phone,st_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        username = (EditText) findViewById(R.id.username);
        name = (EditText) findViewById(R.id.name);
        password= (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        confirm = (EditText) findViewById(R.id.confirm);
        email= (EditText) findViewById(R.id.email);
        signup= (Button) findViewById(R.id.signup);
        mSocket=new SocketService();
        socket= mSocket.getSocket();
        socket.on("signUpValid", (Emitter.Listener) signupValid);

    }

    Emitter.Listener signupValid = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject user_det = (JSONObject)args[0];
            try{final String msg= user_det.get("msg").toString();
                final String result= user_det.get("result").toString();
                runOnUiThread(new Runnable(){
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    }
                });
               } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    } ;

    public void signUp(View view)
    {
        st_password=password.getText().toString();
        st_username=username.getText().toString();
        st_name=name.getText().toString();
        st_email=email.getText().toString();
        st_phone=phone.getText().toString();
        st_confirm =confirm.getText().toString();

        JSONObject signUpData=new JSONObject();
        try {
            signUpData.put("username", st_username);
            signUpData.put("name", st_name);
            signUpData.put("password", st_password);
            signUpData.put("email", st_email);
            signUpData.put("phone", st_phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("confirm",confirm.getText().toString());
        Log.i("password",password.getText().toString());
        if(st_password.length()<8)
        {
            Toast.makeText(getApplicationContext(),"Password must be atleast 8 characters long",Toast.LENGTH_SHORT).show();
        }
        else if (!st_confirm.equals(st_password))
        {
            Toast.makeText(getApplicationContext(),"The passwords do not match ", Toast.LENGTH_SHORT).show();
        }
        else if (!isPatternMatching(st_email,"^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$"))
        {
            Toast.makeText(getApplicationContext(),"Email is not valid ",Toast.LENGTH_SHORT).show();

        }
        else if(st_phone.length()!= 10 || !isPatternMatching(phone.getText().toString(),"^98[0-9]*"))
        {
            Toast.makeText(getApplicationContext(),"Phone number is not valid",Toast.LENGTH_SHORT).show();
        }
        else socket.emit("signUp",signUpData);


    }

    public boolean isPatternMatching(String line,String pat){

        Pattern ptn = Pattern.compile(pat);
        Matcher mtch = ptn.matcher(line);
        if(mtch.find()){
            return true;
        }
        return false;
    }

}
