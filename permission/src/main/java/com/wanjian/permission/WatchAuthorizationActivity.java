package com.wanjian.permission;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import static com.wanjian.permission.OneKeyPerm.BROADCAST_PERM;
import static com.wanjian.permission.OneKeyPerm.ONE_KEY_PERM;
import static com.wanjian.permission.OneKeyPerm.ONE_KEY_PERM_MANU;
import static com.wanjian.permission.OneKeyPerm.TIPS;

/**
 * Created by wanjian on 2018/1/10.
 */

public class WatchAuthorizationActivity extends AppCompatActivity {
    public static final String TAG = WatchAuthorizationActivity.class.getName();
    private final int REQUEST = 1;
    private String perm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        perm = intent.getStringExtra(ONE_KEY_PERM);
        request(intent.getStringExtra(TIPS));
    }

    private void request(String tips) {
        if (TextUtils.isEmpty(perm)) {
            finish();
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", getPackageName(), null));
            startActivityForResult(intent, REQUEST);

            if (TextUtils.isEmpty(tips) == false) {
                Toast.makeText(this, tips, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            onResult();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST) {
//
//        }
        onResult();
    }


    private void onResult() {
        int permState = ContextCompat.checkSelfPermission(this, perm);
        Intent result = new Intent(ONE_KEY_PERM_MANU + "/" + perm);
        result.putExtra(perm, permState);
        result.setPackage(getPackageName());
        sendBroadcast(result, BROADCAST_PERM);
        finish();
        overridePendingTransition(0, 0);
    }
}
