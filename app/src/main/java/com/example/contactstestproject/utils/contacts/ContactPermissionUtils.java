package com.example.contactstestproject.utils.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.contactstestproject.utils.ApplicationUtils;

public class ContactPermissionUtils {

    public static final int REQUEST_CODE_PERMISSION = 1;

    public static void requestPermission(Activity activity) {
        if (!isGranted())
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_PERMISSION);
    }

    public static boolean isGranted() {
        return ContextCompat.checkSelfPermission(ApplicationUtils.getContext(),
                Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }
}
