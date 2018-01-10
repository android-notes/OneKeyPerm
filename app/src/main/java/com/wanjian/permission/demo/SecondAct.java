package com.wanjian.permission.demo;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wanjian.permission.OneKeyPerm;


/**
 * Created by wanjian on 2018/1/2.
 */

public class SecondAct extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sec);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneKeyPerm.request(Manifest.permission.READ_EXTERNAL_STORAGE, "需要读取文件", new OneKeyPerm.OnPermResultListener() {
                    @Override
                    public void onPermResult(String perm, boolean isGrant) {
                        Toast.makeText(SecondAct.this, "请求读取权限 " + isGrant, Toast.LENGTH_SHORT).show();
                    }
                },true);
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
