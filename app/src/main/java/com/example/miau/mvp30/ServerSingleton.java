package com.example.miau.mvp30;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import java.net.InetSocketAddress;

/**
 * Created by Alex on 05/10/2018.
 */

public class ServerSingleton {
    private static ServerSingleton ourInstance;
    private static Server server;

    public static Server getInstance(InetSocketAddress address, TextView clients, Activity activity) {
        if(server==null)
            ourInstance = new ServerSingleton(address, clients, activity);
        return server;
    }

    private ServerSingleton(InetSocketAddress address, TextView clients, Activity activity) {
        server = new Server(address, clients, activity);
        server.setReuseAddr(true);
        server.start();


    }

    public static void setServerNull() {
        ServerSingleton.server = null;
    }
}
