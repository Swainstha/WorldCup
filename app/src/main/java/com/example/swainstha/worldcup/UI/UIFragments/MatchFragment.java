package com.example.swainstha.worldcup.UI.UIFragments;


import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.swainstha.worldcup.Adapters.MatchListAdapter;
import com.example.swainstha.worldcup.Classes.MatchData;
import com.example.swainstha.worldcup.Classes.PositionData;
import com.example.swainstha.worldcup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchFragment extends Fragment {


    MatchListAdapter matchListAdapter;
    ListView matchListview;

    //    private final String urlString = "http://192.168.1.119:3001";
    private final String urlString = "https://world-cup-server.herokuapp.com";
    private Socket socket;
    ArrayList<MatchData> matchList;
    BetArguments betArguments;

    public interface BetArguments {
        public void sendBetArguments(int id, String team1, String team2, String country1, String country2);
    }


    public MatchFragment() {
        // Required empty public constructor
    }

    //calling node js server to retrieve data
    public class RetrieveData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Thread.currentThread().setName("Position");
            try {
                //sending message to server
                socket.emit("match", "1");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        initializeSocket();
        MatchFragment.RetrieveData retrieveData = new MatchFragment.RetrieveData();
        try {

            //check for internet connectivity
            if(!isOnline()) {
                throw new Exception();
            }

            //execute worker thread to send data
            retrieveData.execute().get();

        }catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("INFO","Failed Sending");
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.i("INFO","Failed Sending");
        } catch(Exception e) {
            Log.i("INFO","No internet connection may be");
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        try {
            betArguments = (BetArguments) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement BetArguments");
        }


        return inflater.inflate(R.layout.fragment_match, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        matchListview= view.findViewById(R.id.match_list_view);
        matchListAdapter = new MatchListAdapter(this.getContext());

        //for checking
//        for (int i = 1; i < 21; i++) {
//            if ((i+4) % 5 == 0) {
//                matchListAdapter.addSectionHeaderItem(new MatchData("Group1"));
//            } else {
//                matchListAdapter.addItem(new MatchData("Chelsea","2","1","MU","20-05-2018","6:45 pm"));
//            }
//        }
        //to notify that the data in the listview has been changed
       // matchListAdapter.notifyDataSetChanged();

        matchListview.setAdapter(matchListAdapter);
        matchListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MatchData matchData = (MatchData)matchListview.getItemAtPosition(i);
                Log.i("INFO",matchData.getTeam1());
                betArguments.sendBetArguments(matchData.getId(),"cr7","lm10",matchData.getTeam1(),matchData.getTeam2());
            }
        });

    }

    //function called that allows the fragment to clean up resources associated with its View.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(socket != null)
            socket.disconnect(); //disconnecting the socket
        Log.i("INFO","Fragment Exit");
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

    //function to Initiliaze socket

    private void initializeSocket() {

        //creating a socket to connect to the node js server using socket.io.client
        try {
            //check for internet connection
            if(!isOnline()) {
                throw new Exception();
            }

            //generating a random number for join id in the server
//            Random rand = new Random();
//            int  id = rand.nextInt(50) + 1;

            //using sharedPreferences to read the link to the server: heroku or local
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String url = sharedPref.getString(getString(R.string.link), "http://192.168.1.119:3001");
            Log.i("INFO",url);

            socket = IO.socket(url); //specifying the url
            socket.connect(); //connecting to the server
            socket.emit("joinAndroid",Integer.toString(2));  //specifying the join group to the server

            //callback functions for socket connected, message received and socket disconnected
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on("matchData", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.i("VERBOSE", args[0].toString());
                    final JSONArray array = (JSONArray) args[0];
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {

                                String group = "T";
                                int groupNum = 65;
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    if (!obj.getString("GN").equals(group)) {
                                        matchListAdapter.addSectionHeaderItem(new MatchData("Group " + obj.getString("GN")));
                                        group = obj.getString("GN");
                                    }

                                    matchListAdapter.addItem(new MatchData(obj.getInt("id"),obj.getString("HT"),obj.getString("HS"),obj.getString("AS"),obj.getString("AT"),"20-05-2018","6:45 pm"));

                                }
                                //to notify that the data in the listview has been changed
                                matchListAdapter.notifyDataSetChanged();

                            } catch (Exception e) {

                            }
                            //Toast.makeText(getContext(), args[0].toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
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

}
