package com.example.sa.socket_final_test.UIFragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sa.socket_final_test.R;

import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BetFragment extends Fragment {


    private final String urlString = "http://192.168.1.119:3001";
    //private final String urlString = "https://world-cup-server.herokuapp.com";
    private Socket socket;

    private ImageView image1;
    private ImageView image2;
    private TextView country1;
    private TextView country2;
    private TextView team1;
    private TextView team2;
    private TextView betText;
    private Spinner betAmountSpinner;
    private Button betButton;

    private String betAmount;

    public BetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        initializeSocket();

        return inflater.inflate(R.layout.fragment_bet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image1 = view.findViewById(R.id.team1Flag);
        image2 = view.findViewById(R.id.team2Flag);
        country1 = view.findViewById(R.id.team1Country);
        country2 = view.findViewById(R.id.team2Country);
        team1 = view.findViewById(R.id.team1Name);
        team2 = view.findViewById(R.id.team2Name);
        //betText = view.findViewById(R.id.betText);
        betAmountSpinner = view.findViewById(R.id.betAmount);
        betButton = view.findViewById(R.id.betButton);

        readBundle(getArguments());

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.bet_amount_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        betAmountSpinner.setAdapter(adapter);

        betAmountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                betAmount = (String) adapterView.getItemAtPosition(i);
                Log.i("Bet", betAmount);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                betAmount = 5 + "";
            }
        });

        betButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("INFO","Bet");
                try{
                    SendBet sendBet = new SendBet();
                    Bundle bundle = getArguments();
                    sendBet.execute(bundle.getInt("id") + "",bundle.getString("team1"),bundle.getString("team2"),bundle.getString("country1"),
                            bundle.getString("country2"),betAmount).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.i("INFO","Failed Sending");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.i("INFO","Failed Sending");
                }
            }
        });

    }

    private void readBundle(Bundle bundle) {
        Log.i("readbundle",bundle.getInt("id") + "");
        if (bundle != null) {
            String c1 = bundle.getString("country1");
            country1.setText(c1);
            c1 = c1.toLowerCase();
            c1 = c1.replaceAll("\\s","");

            int resID = getResources().getIdentifier(c1,
                    "drawable", getContext().getPackageName());

            image1.setImageResource(resID);

            String c2 = bundle.getString("country2");
            country2.setText(c2);
            c2 = c2.toLowerCase();
            c2 = c2.replaceAll("\\s","");
            resID = getResources().getIdentifier(c2,
                    "drawable", getContext().getPackageName());

            image2.setImageResource(resID);
            team1.setText(bundle.getString("team1"));
            team2.setText(bundle.getString("team2"));


        }
    }

    //calling node js server to retrieve data
    public class SendBet extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Thread.currentThread().setName("Position");
            try {
                //sending message to server
                Log.i("match_id",urls[0]);
                Log.i("team1",urls[1]);
                Log.i("team2",urls[2]);
                Log.i("country1",urls[3]);
                Log.i("country2",urls[4]);
                Log.i("betAmount",urls[5]);
                socket.emit("betStart", urls[0],urls[1],urls[2],urls[3],urls[5]);
                return "";

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("INFO","Failed Sending. May be no internet connection");
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Log.i("INFO",result);
            result = "";
        }
    }

    //function to Initiliaze socket

    private void initializeSocket() {

        //creating a socket to connect to the node js server using socket.io.client
        try {
            //check for internet connection
            if(!isOnline()) {
                throw new Exception();
            }

            //generating a random number for join id in the server
            Random rand = new Random();
            int  id = rand.nextInt(50) + 1;

            //using sharedPreferences to read the link to the server: heroku or local
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String url = sharedPref.getString(getString(R.string.link), "http://192.168.1.119:3001");
            Log.i("INFO",url);

            socket = IO.socket(url); //specifying the url
            socket.connect(); //connecting to the server
            //socket.emit("joinAndroid",Integer.toString(id));  //specifying the join group to the server

            //callback functions for socket connected, message received and socket disconnected
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on("pointData", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.i("INFO", args[0].toString());
                    //Toast.makeText(getContext(), args[0].toString(), Toast.LENGTH_SHORT).show();

                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
        } catch(URISyntaxException e) {
            Log.i("INFO","Uri syntax exception");
        } catch(Exception e) {
            Log.i("INFO", "No internet connection");
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_LONG).show();
        }
    }

    //check if the device is connected to the internet
    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //check if the connection id wifi or mobile data
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

        return isConnected;

    }

}
