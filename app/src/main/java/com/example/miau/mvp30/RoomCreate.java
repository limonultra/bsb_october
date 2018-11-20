package com.example.miau.mvp30;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class RoomCreate extends AppCompatActivity {

    private TextView WIFI;
    private  Button btnCreate;
    private  TextView PIN;
    private TextView changeWiFi;
    private TextView changeIdiom;


    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate( SavedInstanceState );
        setContentView( R.layout.activity_room_create );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.registerReceiver(mWifiStateChangedReceiver,new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));

        btnCreate = findViewById( R.id.RoomStart );
        PIN = findViewById( R.id.pinName );
        WIFI = findViewById(R.id.wifiName);
        changeIdiom = findViewById( R.id.changeIdiom );
        changeWiFi = findViewById( R.id.changeWiFi );

        String[] tempPerms = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_NETWORK_STATE};

        hasPermissions( this,tempPerms );

        WIFI.setText( getWIFIName() );

        btnCreate.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                Intent toRoom = new Intent( RoomCreate.this, Room.class );
                toRoom.putExtra("PIN",getHex());
                toRoom.putExtra( "wifiName", getWIFIName());
                startActivity( toRoom );
            }
        } );
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mWifiStateChangedReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(mWifiStateChangedReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }


    public void setChangeIdiom(View view){
        Intent toSettings = new Intent( this,Settings.class );
        startActivity( toSettings );
    }
    public void setChangeWiFi(View view){
        startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
    }

    private BroadcastReceiver mWifiStateChangedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

            switch (extraWifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                case WifiManager.WIFI_STATE_DISABLING:
                    updateUI(false);
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    while(conMan.getActiveNetworkInfo() == null || conMan.getActiveNetworkInfo().getState() != NetworkInfo.State.CONNECTED) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    updateUI(true);
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    String wifi = getWIFIName();
                    while (wifi.contains("unknown")) {
                        wifi = getWIFIName();
                    }
                    String ip = getHex();
                    while (ip.equals("0000")) {
                        ip = getHex();
                    }
                    updateUI(true);
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }

        }
    };

    private void updateUI(boolean isConnected) {

        if(!isConnected) {
            WIFI.setText("No hay conexión WiFi");
            PIN.setText( "0000" );
            btnCreate.setEnabled( false );
        } else {
            String wifiName = getWIFIName();
            if (wifiName.contains("unknown")) {
                wifiName = getWIFIName();
                if (wifiName.contains("unknown"))
                    WIFI.setText("No hay conexión WiFi");
                else
                    WIFI.setText(wifiName);
            }
            else
                WIFI.setText(wifiName);
            btnCreate.setEnabled( true );
            PIN.setText( getHex() );
        }
    }

    @Override
    protected
    void onStart() {
        super.onStart();

        if (getWIFIName().length() > 5)
            updateUI( true );
        else
            updateUI( false );
    }


    private boolean hasPermissions (Context ctx, String[] permissions) {
        if(ctx != null && permissions != null) {
            for(String permission: permissions) {
                if(ActivityCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions( this, permissions, 2 );
                return false;
            }
        }
        return true;
    }

    public  String getIp(){
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        @SuppressLint("MissingPermission") WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        Log.d("DEBUG", String.valueOf( ip ) );
        String ipAddress = Formatter.formatIpAddress(ip);
        return ipAddress;

    } // Get IP address
    public String getHex() {
        String ip = getIp();
        String[] ipNumbers = ip.split( "[.]" );
        if(ipNumbers.length > 3)
            return codify(Integer.parseInt(ipNumbers[2])) + codify(Integer.parseInt(ipNumbers[3]));
        return "Error";
    } // Returns HEX code from IP address

    public String codify(int codificable){
        return String.format( "%02X", codificable );
    } // Parses int numbers into HEX code
    public String getWIFIName(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String[] wifiName = wifiInfo.toString().split(",");
        String wifiFinalName = wifiName[0].replace("SSID: "," ");

        return wifiFinalName;
    } // Get WiFi SSID

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, com.example.miau.mvp30.Menu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

