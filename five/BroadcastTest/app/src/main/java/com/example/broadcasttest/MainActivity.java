package com.example.broadcasttest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static androidx.core.content.ContextCompat.getSystemService;

public class MainActivity extends AppCompatActivity {

    String TAG="MMMMMMM";
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    private  LocalReceiver localReceiver;
    private LocalActivityManager localActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button);
        localActivityManager=LocalActivityManager.get
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction("com.example.broadcasttest.MY_BROADCAST");
                intent.setComponent(new ComponentName("com.example.broadcasttest","com.example.broadcasttest.MyBroadcastReceiver"));
                sendOrderedBroadcast(intent,null);
                intent.setPackage("com.example.broadcasttest2");
               sendOrderedBroadcast(intent ,null);
            }
        });
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.com.CONNECTIVITY_CHANGE");
        networkChangeReceiver =new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        unregisterReceiver(networkChangeReceiver );
    }

    class NetworkChangeReceiver  extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager  connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isAvailable()){
                Toast.makeText(context,"network is available",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"network is unavailable" ,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
