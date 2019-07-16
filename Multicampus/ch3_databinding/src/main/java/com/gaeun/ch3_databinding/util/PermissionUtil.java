package com.gaeun.ch3_databinding.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {
    public static void checkPermission(Activity context, String permission){
        if(ContextCompat.checkSelfPermission(context, permission) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context, new String[]{permission}, 200);
        }
    }
}
