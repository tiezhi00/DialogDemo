package com.app.dialogdemo.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.dialogdemo.R;

public class CustomNotificationActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "custom_notification_channel";
    private static final String CHANNEL_NAME = "Custom Notification Channel";
    //通知管理器
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_notification);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //创建通知渠道
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is a custom notification channel");
            notificationManager.createNotificationChannel(channel);
        }
        setListener();
    }

    private void setListener() {
        findViewById(R.id.btn_show_custom_notification)
                .setOnClickListener(v -> {
                    //显示自定义通知
                    showCustom();
                });
    }

    private void showCustom() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setContentTitle("Notification自定义通知")//标题内容
                .setWhen(System.currentTimeMillis())//通知创建的时间
                .setColor(Color.rgb(255, 0, 0))
                .setSmallIcon(R.mipmap.ic_notification);//小图标
        //设置自定义布局
        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);

        @SuppressLint("RemoteViewLayout") RemoteViews bigRemoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification_big);
        //设置点击跳转事件
        Intent intent = new Intent(this, CustomNotificationActivity.class);
        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            flag = flag | PendingIntent.FLAG_IMMUTABLE;
        }
        intent.putExtra("info", System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, flag);
        bigRemoteViews.setOnClickPendingIntent(R.id.btn_confirm, pendingIntent);
        builder.setCustomBigContentView(bigRemoteViews);
//        builder.setCustomContentView(remoteViews);
        //设置自定义Style
        NotificationCompat.DecoratedCustomViewStyle style = new NotificationCompat.DecoratedCustomViewStyle();
        builder.setStyle(style);

        Notification notification = builder.build();
        //发送通知
        notificationManager.notify(1, notification);

    }

}