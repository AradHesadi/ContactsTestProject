package com.example.contactstestproject.utils;

import android.content.Context;

/*
in order to call context less in different classes of app
 */

public class ApplicationUtils {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context Context) {
        context = Context;
    }
}
