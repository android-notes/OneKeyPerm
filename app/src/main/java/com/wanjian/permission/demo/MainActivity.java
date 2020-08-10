package com.wanjian.permission.demo;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wanjian.permission.OneKeyPerm;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneKeyPerm.request(getApplication(), Manifest.permission.CAMERA, "您需要允许相机权限，否则无法使用扫码功能", new OneKeyPerm.OnPermResultListener() {
                    @Override
                    public void onPermResult(String perm, boolean isGrant) {
                        Toast.makeText(MainActivity.this, "请求相机权限 " + isGrant, Toast.LENGTH_SHORT).show();
                    }
                },true);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneKeyPerm.request(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE, "您需要允许读取文件权限，否则无法查看图片", new OneKeyPerm.OnPermResultListener() {
                    @Override
                    public void onPermResult(String perm, boolean isGrant) {
                        Toast.makeText(MainActivity.this, "请求读取权限 " + isGrant, Toast.LENGTH_SHORT).show();
                    }
                },true);
            }
        });

        findViewById(R.id.newAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SecondAct.class));
            }
        });
        ((TextView) findViewById(R.id.tv)).setText(processName() + " " + android.os.Process.myPid());

    }

    private String processName() {
        int pid = android.os.Process.myPid();
        String processName;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                return processName;
            }
        }
        return null;
    }
}
