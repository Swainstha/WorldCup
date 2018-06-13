package com.example.sa.socket_final_test.Adapters;

import android.app.Application;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketService extends Application{

      Socket socket;

public SocketService() {
    try {
        socket = IO.socket("http://192.168.1.119:3001");
        //socket = IO.socket("https://world-cup-server.herokuapp.com/");
        Log.i("ininti","initialized");

        socket.connect();
        //
    } catch (URISyntaxException e) {
        e.printStackTrace();
        Log.i("ininti","Could not initialize");
    }
}

public Socket getSocket()
    {
        return socket;
    }

}








