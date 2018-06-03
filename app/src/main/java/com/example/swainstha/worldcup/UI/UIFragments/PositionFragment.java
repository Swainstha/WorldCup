package com.example.swainstha.worldcup.UI.UIFragments;


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
import android.widget.TextView;
import android.widget.Toast;

import com.example.swainstha.worldcup.Adapters.PositionListAdapter;
import com.example.swainstha.worldcup.Classes.PositionData;
import com.example.swainstha.worldcup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PositionFragment extends Fragment {


    ListView positionListView;
    PositionListAdapter positionListAdapter;

//    private final String urlString = "http://192.168.1.119:3001";
    private final String urlString = "https://world-cup-server.herokuapp.com";
    private Socket socket;
    ArrayList<PositionData> positionList;

    public PositionFragment() {
        // Required empty public constructor
    }

    //calling node js server to retrieve data
    public class RetrieveData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Thread.currentThread().setName("Position");
            try {
                //sending message to server
                socket.emit("userRank");
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
        View view = inflater.inflate(R.layout.fragment_position, container, false);


        initializeSocket();
        RetrieveData retrieveData = new RetrieveData();
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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        positionListView = view.findViewById(R.id.position_list_view);

        //default data
        positionList = new ArrayList<>();
//        positionList.add(new PositionData("1","Sajan Amatya","45"));
//        positionList.add(new PositionData("2","Shovan Raj Shrestha","44"));
//        positionList.add(new PositionData("3","Sushil Khadka","43"));
//        positionList.add(new PositionData("4","Swain Shrestha","42"));
//        positionList.add(new PositionData("5","Nischal Rai","41"));
//        positionList.add(new PositionData("6","Sizu karmacharya","41"));
//        positionList.add(new PositionData("7","Anjal Lakhey","40"));

        positionListAdapter = new PositionListAdapter(this.getContext(), positionList);

        // Add a header to the ListView
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.position_list,positionListView,false);
        positionListView.addHeaderView(header);

        //associating the listview with the adapter
        positionListView.setAdapter(positionListAdapter);

    }

    //function called that allows the fragment to clean up resources associated with its View.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

            }).on("userRankData", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.i("INFO", args[0].toString());
                    final JSONArray array = (JSONArray) args[0];
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {

                                //parse the json array
                                for (int i = 0; i < array.length(); i++) {
                                    positionList.add(new PositionData(array.getJSONObject(i).getString("Position"),
                                            array.getJSONObject(i).getString("Name"),
                                            array.getJSONObject(i).getString("Score")));
                                }

                                //to notify that the data in the listview has been changed
                                positionListAdapter.notifyDataSetChanged();

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
