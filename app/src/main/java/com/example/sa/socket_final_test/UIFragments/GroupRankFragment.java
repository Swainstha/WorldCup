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
import android.widget.ListView;
import android.widget.Toast;

import com.example.sa.socket_final_test.Adapters.GroupRankListAdapter;
import com.example.sa.socket_final_test.Classes.GroupRankData;
import com.example.sa.socket_final_test.R;

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
public class GroupRankFragment extends Fragment {


    GroupRankListAdapter groupRankListAdapter;
    ListView groupRankListview;
    private final String urlString = "http://192.168.1.119:3001";
    //private final String urlString = "https://world-cup-server.herokuapp.com";
    private Socket socket;
    ArrayList<GroupRankData> groupRankList;
    public GroupRankFragment() {
        // Required empty public constructor
    }


    //calling node js server to retrieve data
    public class RetrieveData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Thread.currentThread().setName("Position");
            try {
                //sending message to server
                socket.emit("point", "1");
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
        GroupRankFragment.RetrieveData retrieveData = new GroupRankFragment.RetrieveData();
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

        return inflater.inflate(R.layout.fragment_group_rank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        groupRankListview= view.findViewById(R.id.group_rank_listview);
        groupRankListAdapter = new GroupRankListAdapter(this.getContext());
        Log.i("INFO",groupRankListAdapter.toString());
        groupRankListview.setAdapter(groupRankListAdapter);
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
            Random rand = new Random();
            int  id = rand.nextInt(50) + 1;

            //using sharedPreferences to read the link to the server: heroku or local
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String url = sharedPref.getString(getString(R.string.link), "http://192.168.1.119:3001");
            Log.i("INFO",url);

            socket = IO.socket(url); //specifying the url
            socket.connect(); //connecting to the server
            socket.emit("joinAndroid",Integer.toString(id));  //specifying the join group to the server

            //callback functions for socket connected, message received and socket disconnected
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on("pointData", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.i("INFO", args[0].toString());
                    final JSONArray array = (JSONArray) args[0];
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {

                                String group = "T";
                                int rank = 1;
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    if (!obj.getString("GN").equals(group)) {
                                        groupRankListAdapter.addSectionHeaderItem(new GroupRankData("Group " + obj.getString("GN")));
                                        groupRankListAdapter.addItem(new GroupRankData("Rank","Country","Pts","MP","Win","Draw","Lose"));
                                        group = obj.getString("GN");
                                        rank = 1;
                                    }

                                    groupRankListAdapter.addItem(new GroupRankData(rank++ + "",obj.getString("CN"),obj.getInt("PTS") + "",obj.getInt("MP") + "",obj.getInt("W") + "",obj.getInt("D") + "",obj.getInt("L") + ""));

                                }
                                //to notify that the data in the listview has been changed
                                groupRankListAdapter.notifyDataSetChanged();

                            } catch (Exception e) {

                                e.printStackTrace();
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
