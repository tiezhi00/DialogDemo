package com.app.dialogdemo.floatingwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.dialogdemo.R;

public class ServiceFloatingWindow implements View.OnTouchListener {
    private Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View rootView;
    private int inViewX, inViewY;//相对于悬浮窗的坐标,以View的左上角为原点
    private int inScreenX, inScreenY;//相对于屏幕的坐标,以屏幕的左上角为原点
    private int downInScreenX, downInScreenY;//按下时相对于屏幕的坐标
    private Boolean isMoving = false;
    private static ServiceFloatingWindow instance;
    private Button btn_hide;
    private View v_line;
    private LinearLayout ll_content;

    public static ServiceFloatingWindow getInstance() {
        if (instance == null) {
            instance = new ServiceFloatingWindow();
        }
        return instance;
    }

    private ServiceFloatingWindow() {

    }

    public void init(Context context) {
        this.context = context;
        initFloatWindow();
    }

    private void initFloatWindow() {
        rootView = LayoutInflater.from(context).inflate(R.layout.floatingwindow_in_service_layout, null);
        btn_hide = rootView.findViewById(R.id.btn_hide);
        v_line = rootView.findViewById(R.id.v_line);
        ll_content = rootView.findViewById(R.id.ll_content);
        btn_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_line.setVisibility(View.VISIBLE);
                ll_content.setVisibility(View.GONE);
                layoutParams.width = 40;
                windowManager.updateViewLayout(rootView, layoutParams);
            }
        });
        v_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_line.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                layoutParams.width = 300;
                windowManager.updateViewLayout(rootView, layoutParams);
            }
        });

        rootView.setOnTouchListener(this);
        //获取WindowManager
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //设置WindowManager.LayoutParams
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 26) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//设置不获取焦点
        layoutParams.width = 300;
        layoutParams.height = 300;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        //设置默认位置
        defaultPosition();
    }

    private void defaultPosition() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //设置默认位置，水平居右，垂直居中
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams.x = displayMetrics.widthPixels;
        layoutParams.y = displayMetrics.heightPixels / 2 - 150;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return moveFloatLayout(event);
    }

    //处理悬浮窗的移动事件
    private boolean moveFloatLayout(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下事件
                //获取相对悬浮窗的坐标
                inViewX = (int) event.getX();
                inViewY = (int) event.getY();
                //获取相对屏幕的坐标
                downInScreenX = (int) event.getRawX();
                downInScreenY = (int) event.getRawY();
                inScreenX = (int) event.getRawX();
                inScreenY = (int) event.getRawY();
                isMoving = true;
                break;
            case MotionEvent.ACTION_MOVE://移动事件
                //获取相对屏幕的坐标
                inScreenX = (int) event.getRawX();
                inScreenY = (int) event.getRawY();
                layoutParams.x = inScreenX - inViewX;
                layoutParams.y = inScreenY - inViewY;
                //更新悬浮窗位置
                windowManager.updateViewLayout(rootView, layoutParams);
                isMoving = true;
                break;
            case MotionEvent.ACTION_UP://抬起事件
                isMoving = false;
                //按下时的坐标和抬起时的坐标相同，表示点击事件
                if (downInScreenX == inScreenX && downInScreenY == inScreenY) {
                    Toast.makeText(context, "触发了点击", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    public void showFloatingWindow() {
        if (rootView.getParent() == null) {
            windowManager.addView(rootView, layoutParams);
        }
    }

    public void removeFloatingWindow() {
        if (rootView.getParent() != null) {
            windowManager.removeView(rootView);
        }
    }
}
