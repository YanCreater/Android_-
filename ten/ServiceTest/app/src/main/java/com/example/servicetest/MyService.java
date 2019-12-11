package com.example.servicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private DownloadBinder mBinder=new DownloadBinder();

    class DownloadBinder extends Binder{
        public void startDownload(){
            Log.d("AAA", "startDownload: 开始下载");
        }

        public int getProgress(){
            Log.d("AAA", "getProgress:获取进度 ");
            return 0;
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    //用于创建服务
    public void onCreate() {
        super.onCreate();
        Log.d("AAA", "onCreate:服务创建 ");
        /**
         * 原书代码

        //创建前台服务
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        Notification notification=new Notification.Builder(this)
                .setContentText("前台服务")
                .setContentTitle("前台服务")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
         */
        String CHANNEL_ONE_ID = "CHANNEL_ONE_ID";
        String CHANNEL_ONE_NAME= "CHANNEL_ONE_ID";
        NotificationChannel notificationChannel= null;
//进行8.0的判断
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel= new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.jianshu.com/p/14ba95c6c3e2"));
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification= new Notification.Builder(this).setChannelId(CHANNEL_ONE_ID)
                .setTicker("Nature")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("这是一个测试标题")
                .setContentIntent(pendingIntent)
                .setContentText("这是一个测试内容")
                .build();
        notification.flags|= Notification.FLAG_NO_CLEAR;
        startForeground(1, notification);
        /**
         * 在使用这个是时候需要在app/build.gradle中将minSdkVersion  改为26
         *  添加了一个通知渠道
         *         Intent intent=new Intent(this,MainActivity.class);
         *         PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
         *
         *         String CHANNEL_ID = "1";
         *         String CHANNEL_Name = "channel_name";
         *         NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_Name, NotificationManager.IMPORTANCE_HIGH);
         *         NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
         *         manager.createNotificationChannel(channel);
         *
         *         Notification.Builder builder = new Notification.Builder(this,CHANNEL_ID);
         *         builder.setContentTitle("This is content file");
         *         builder.setContentText("This is content text");
         *         builder.setWhen(System.currentTimeMillis());
         *         builder.setSmallIcon(R.mipmap.ic_launcher);
         *         builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
         *         builder.setChannelId(CHANNEL_ID);
         *         builder.setContentIntent(pi);
         *         Notification notification = builder.build();
         *         startForeground(1,notification);
         *
         * */
    }

    @Override
    //每次服务启动的时候调用
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AAA", "onCreate:服务运行 ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AAA", "onDestroy: 服务销毁");
    }
}
