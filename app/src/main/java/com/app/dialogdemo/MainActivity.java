package com.app.dialogdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.dialogdemo.dialog.PhotoDialog;
import com.app.dialogdemo.floatingwindow.ActivityFloatingWindow;
import com.app.dialogdemo.notification.CustomNotificationActivity;
import com.app.dialogdemo.service.ForegroundService;

/*
    自定义AlertDialog
 */

public class MainActivity extends AppCompatActivity {

    private ActivityFloatingWindow floatingWindow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置点击事件
        findViewById(R.id.btn_open_alert_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示自定义弹窗
                showCustomDialog();
            }
        });
        findViewById(R.id.btn_open_photo_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示自定义弹窗
                showPhotoDialog();
            }
        });
        findViewById(R.id.btn_open_activity_float_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示Activity悬浮窗
                showActivityFloatingWindow();
            }
        });
        findViewById(R.id.btn_open_service_float_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示Service悬浮窗
                Intent intent = new Intent(MainActivity.this, ForegroundService.class);
                startService(intent);
            }
        });
        findViewById(R.id.btn_custom_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示自定义通知
                Intent intent = new Intent(MainActivity.this, CustomNotificationActivity.class);
                startActivity(intent);
            }
        });
        //初始化悬浮窗
//        initFloatingWindow();

    }

    private void showActivityFloatingWindow() {
        if (floatingWindow != null) {
            floatingWindow.showFloatingWindow();
        }
    }

    private void initFloatingWindow() {
        //权限判断
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                //跳转到设置界面
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 10);
            } else {
                //有权限，初始化悬浮窗
                initView();
            }
        } else {
            //6.0以下，直接初始化悬浮窗
            initView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            //判断是否有权限
            if (Build.VERSION.SDK_INT >= 23) {
                if (Settings.canDrawOverlays(getApplicationContext())) {
                    //有权限，初始化悬浮窗
                    initView();
                } else {
                    //没有权限，提示用户
                    Toast.makeText(this, "请先开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showPhotoDialog() {
        PhotoDialog photoDialog = new PhotoDialog(this);
        photoDialog.setPhotoCallBack(new PhotoDialog.PhotoCallBack() {
            @Override
            public void onTakePhoto() {
                Toast.makeText(MainActivity.this, "点击了拍照", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChosePhoto() {
                Toast.makeText(MainActivity.this, "点击了选择照片", Toast.LENGTH_SHORT).show();
            }
        });
        photoDialog.show();

    }

    private void showCustomDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_AlertDialog_Custom)
//                .setTitle("Dialog Title")
//                .setMessage("This is a custom dialog message.")
//                .setPositiveButton("OK", (dialog1, which) -> dialog1.dismiss())
//                .setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss())
                .setView(R.layout.alertdialog_custom_layout) // 自定义布局
                .create();
        dialog.setCanceledOnTouchOutside(true); // 点击外部消失

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);// 设置弹窗位置
        //弹窗动画
        window.setWindowAnimations(R.style.Theme_AlertDialog_Custom_Animation);
        dialog.show();

        //window属性设置
        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        layoutParams.width=480;
        window.setAttributes(layoutParams);

    }

    private void initView() {
        //初始化悬浮窗
        floatingWindow = new ActivityFloatingWindow(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (floatingWindow != null) {
            floatingWindow.removeFloatingWindow();
        }
    }
}


