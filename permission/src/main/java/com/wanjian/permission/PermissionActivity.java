package com.wanjian.permission;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import static com.wanjian.permission.OneKeyPerm.BROADCAST_PERM;
import static com.wanjian.permission.OneKeyPerm.ONE_KEY_PERM;
import static com.wanjian.permission.OneKeyPerm.TIPS;


/**
 * Created by wanjian on 2017/12/29.
 */

public class PermissionActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String perm = intent.getStringExtra(ONE_KEY_PERM);
        if (TextUtils.isEmpty(perm)) {
            finish();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                String tips = intent.getStringExtra(TIPS);
                if (TextUtils.isEmpty(tips)) {
                    ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{perm}, REQUEST_CODE);
                } else {
                    new AlertDialog.Builder(PermissionActivity.this).setTitle(tips).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{perm}, REQUEST_CODE);
                        }
                    }).create().show();
                }
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{perm}, REQUEST_CODE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Intent result = new Intent(ONE_KEY_PERM + "/" + perm);
            result.putExtra(perm, PackageManager.PERMISSION_GRANTED);
            intent.setPackage(getPackageName());
            sendBroadcast(result, BROADCAST_PERM);
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Intent intent = new Intent(ONE_KEY_PERM + "/" + permissions[0]);
                    intent.putExtra(permissions[0], grantResults[0]);
                    intent.setPackage(getPackageName());
                    sendBroadcast(intent, BROADCAST_PERM);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                finish();
            }

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
