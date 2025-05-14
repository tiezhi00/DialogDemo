package com.app.dialogdemo.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.app.dialogdemo.R;

/**
 * 自定义Dialog弹窗
 */
public class PhotoDialog extends Dialog implements View.OnClickListener {
    private PhotoCallBack callBack;

    public PhotoDialog(@NonNull Context context) {
        super(context, R.style.Theme_PhotoDialog);
    }

    public PhotoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photo_layout);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels; // 设置宽度为屏幕宽度
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.BOTTOM); // 设置弹窗位置为底部
        window.setWindowAnimations(R.style.Theme_PhotoDialog_Animation); // 设置弹窗动画
        //设置点击事件
        findViewById(R.id.btn_take_photo).setOnClickListener(this);
        findViewById(R.id.btn_chose_photo).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_take_photo) {
            // 处理拍照按钮点击事件
            if (callBack != null) {
                callBack.onTakePhoto();
            }
            dismiss();
        } else if (v.getId() == R.id.btn_chose_photo) {
            // 处理选择照片按钮点击事件
            if (callBack != null) {
                callBack.onChosePhoto();
            }
            dismiss();
        } else if (v.getId() == R.id.btn_cancel) {
            // 处理取消按钮点击事件
            dismiss();
        }
    }

    public void setPhotoCallBack(PhotoCallBack callBack) {
        this.callBack = callBack;
    }

    public interface PhotoCallBack {
        void onTakePhoto();

        void onChosePhoto();
    }
}
