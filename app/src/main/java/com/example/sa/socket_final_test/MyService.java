package com.example.sa.socket_final_test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.sa.socket_final_test.Login.Login;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MyService extends Service {
    //Intent intent = new Intent(this,MainActivity.class);

//    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
String identity;
    static Socket socket;
    private final IBinder binder = new MyLocalBinder();
public static Socket getMyService()
{
    return socket;
}

public MyService()
{

}

    public class MyLocalBinder extends Binder {
        MyService getService (){
            return MyService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public String getCurrentTime()
    {
        SimpleDateFormat df =new SimpleDateFormat("HH:mm:ss", Locale.US);
        return (df.format(new Date()));
    }
    public  Socket gettheSocket(){

        return socket;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this,"Service created ...", Toast.LENGTH_LONG).show();
        createNotificationChannel();
        Intent i = intent;
        if(SaveSharedPreference.getUserID(this).length() == 0)
        {
           identity=SaveSharedPreference.getUserID(this);
        }
        else identity =null;

        mBuilder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(R.drawable.argentina)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      Runnable connect = new connectSocket();
       new Thread(connect).start();
        return START_STICKY;
    }
    class connectSocket implements Runnable {

        @Override
        public void run() {

            try {
                //here you must put your computer's IP address.

                //socket = IO.socket("https://world-cup-server.herokuapp.com/");
                socket = IO.socket("http://192.168.1.119:3001");
                socket.connect();
                socket.emit("joinAndroid",1);
                Log.i("hjgj","igiveup");

            } catch (Exception e) {

                Log.e("socket", "C: Error", e);

            }
            socket.on("betRequest",onLogin);

        }

    }
    private Emitter.Listener onLogin=new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            try {
                final String id = args[0].toString();
//                final String valid_user = user_det.get("result").toString();
//                Log.i("info", user_det.get("id").toString());
//                Log.i("info", user_det.get("result").toString());
                mBuilder.setContentTitle(id);
                //Toast.makeText(getApplicationContext(),user_det.get("result").toString()+user_det.get("id").toString(), Toast.LENGTH_LONG).show();
                //if (valid_user.equals("True")) {
                    //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    Log.i("info", "new activity started, logged in");
                  //  i.putExtra("id", id);
                   notificationManager.notify(0, mBuilder.build());
                   Log.i("socket value is given",identity);


                //    startActivity(i);

              //  }

            } catch (Exception e) {
                Log.i("error", e.toString());
            }

        }


    }
            ;


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        socket = null;
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";
            String description = "channel test";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
