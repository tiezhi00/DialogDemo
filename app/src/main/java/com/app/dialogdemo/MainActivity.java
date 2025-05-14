package com.app.dialogdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.dialogdemo.dialog.PhotoDialog;

/*
    自定义AlertDialog
 */
public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}