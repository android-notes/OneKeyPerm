package com.wanjian.permission;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

/**
 * Created by wanjian on 2017/12/29.
 */

public class OneKeyPerm {

    static final String TIPS = "tips";
    static final String RECEIVER = "receiver";
    static final String ONE_KEY_PERM = OneKeyPerm.class.getName();

    public static void request(Context context,final String permission, String tips, final OnPermResultListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onPermResult(permission, true);
            }
            return;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onPermResult(permission, true);
            }
            return;
        }
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(ONE_KEY_PERM, permission);
        intent.putExtra(TIPS, tips);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putBinder(RECEIVER, new Binder() {
            @Override
            protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
                if (listener != null) {
                    String permission = data.readString();
                    int result = data.readInt();
                    setPermResult(listener, permission, result == PackageManager.PERMISSION_GRANTED);
                }
                return true;
            }
        });
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    public static void request(final Context context, final String permission, final String tips, final OnPermResultListener listener, boolean manualuthorize) {
        if (manualuthorize) {
            request(context,permission, tips, new OnPermResultListener() {
                @Override
                public void onPermResult(String perm, boolean isGrant) {
                    if (!isGrant) {
                        watchAuthorization(context,listener, tips, perm);
                    } else if (listener != null) {
                        setPermResult(listener, perm, isGrant);
                    }
                }
            });
        } else {
            request(context,permission, tips, listener);
        }
    }

    private static void watchAuthorization(Context context, final OnPermResultListener listener, String tips, final String permission) {
        Intent intent = new Intent(context, WatchAuthorizationActivity.class);
        intent.putExtra(ONE_KEY_PERM, permission);
        intent.putExtra(TIPS, tips);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putBinder(RECEIVER, new Binder() {
            @Override
            protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
                if (listener != null) {
                    String permission = data.readString();
                    int result = data.readInt();
                    setPermResult(listener, permission, result == PackageManager.PERMISSION_GRANTED);
                }
                return true;
            }
        });
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private static void setPermResult(final OnPermResultListener listener, final String perm, final boolean result) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            listener.onPermResult(perm, result);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listener.onPermResult(perm, result);
                }
            });
        }
    }
    public interface OnPermResultListener {
        void onPermResult(String perm, boolean isGrant);
    }
}
