package com.wanjian.permission;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by wanjian on 2017/12/29.
 */

public class OneKeyPerm {

    static final String TIPS = "tips";
    static String ONE_KEY_PERM;
    static String ONE_KEY_PERM_MANU;
    static String BROADCAST_PERM;
    private static Context sContext;

    public static void install(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("application can not be null !");
        }
        sContext = application.getApplicationContext();
        Check.hasDefinePermission(sContext);
        ONE_KEY_PERM = sContext.getPackageName() + "/" + OneKeyPerm.class.getName();
        ONE_KEY_PERM_MANU = ONE_KEY_PERM + "/manu/";
        BROADCAST_PERM = sContext.getPackageName() + ".permission.ONE_KEY_PERM";
    }

    public static void request(final String permission, String tips, final OnPermResultListener listener) {

        if (sContext == null) {
            throw new IllegalStateException("init OneKeyPerm first !");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onPermResult(permission, true);
            }
            return;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(sContext, permission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onPermResult(permission, true);
            }
            return;
        }
        String filter = ONE_KEY_PERM + "/" + permission;
        sContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sContext.unregisterReceiver(this);
                if (listener != null) {
                    int result = intent.getIntExtra(permission, 1);
                    if (result == PackageManager.PERMISSION_GRANTED || result == PackageManager.PERMISSION_DENIED) {
                        listener.onPermResult(permission, result == PackageManager.PERMISSION_GRANTED);
                    }
                }
            }
        }, new IntentFilter(filter), BROADCAST_PERM, null);

        Intent intent = new Intent(sContext, PermissionActivity.class);
        intent.putExtra(ONE_KEY_PERM, permission);
        intent.putExtra(TIPS, tips);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sContext.startActivity(intent);
    }

    public static void request(final String permission, String tips, final OnPermResultListener listener, boolean manualuthorize) {
        if (manualuthorize) {
            request(permission, tips, new OnPermResultListener() {
                @Override
                public void onPermResult(String perm, boolean isGrant) {
                    if (!isGrant) {
                        watchAuthorization(listener, perm);
                    } else if (listener != null) {
                        listener.onPermResult(perm, isGrant);
                    }
                }
            });
        } else {
            request(permission, tips, listener);
        }
    }

    private static void watchAuthorization(final OnPermResultListener listener, final String permission) {
        String filter = ONE_KEY_PERM_MANU + "/" + permission;
        sContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sContext.unregisterReceiver(this);
                if (listener != null) {
                    int result = intent.getIntExtra(permission, 1);
                    if (result == PackageManager.PERMISSION_GRANTED || result == PackageManager.PERMISSION_DENIED) {
                        listener.onPermResult(permission, result == PackageManager.PERMISSION_GRANTED);
                    }
                }
            }
        }, new IntentFilter(filter), BROADCAST_PERM, null);

        Intent intent = new Intent(sContext, WatchAuthorizationActivity.class);
        intent.putExtra(ONE_KEY_PERM, permission);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sContext.startActivity(intent);
    }

    public interface OnPermResultListener {
        void onPermResult(String perm, boolean isGrant);
    }
}
