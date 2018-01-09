package com.wanjian.permission;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.support.v4.content.ContextCompat;

import static android.content.pm.PermissionInfo.PROTECTION_SIGNATURE;

/**
 * Created by wanjian on 2018/1/9.
 */

class Check {
    static void hasDefinePermission(Context context) {
        String packName = context.getPackageName();
        try {
            String perm = packName.concat(".permission.ONE_KEY_PERM");
            PackageManager pm = context.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);

            if (pi.permissions == null) {
                throwException(packName);
            }
            for (PermissionInfo permission : pi.permissions) {
                if (perm.equals(permission.name)
                        && permission.protectionLevel == PROTECTION_SIGNATURE
                        && ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            throwException(packName);
        } catch (PackageManager.NameNotFoundException e) {
        }


    }

    private static void throwException(String packName) {
        String definePerm = (" <permission\n" +
                "        android:name=\"$.permission.ONE_KEY_PERM\"\n" +
                "        android:protectionLevel=\"signature\" />").replace("$", packName);


        String usePerm = " <uses-permission android:name=\"$.permission.ONE_KEY_PERM\" />\n".replace("$", packName);

        throw new RuntimeException("define and use permission in your manifest ! \nexample:\n".concat(definePerm).concat("\n").concat(usePerm));
    }
}
