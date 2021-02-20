package com.example.contactstestproject.ui;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class Theme {

    static Map<String, String> colors = new HashMap<String, String>() {{
        put("contactDetailViewTopPaint", "#6ec6ff");
        put("contactDetailViewBackground", "#c3fdff");
        put("contactRowViewBackground", "#4EE3DD");
        put("contactListViewBackgroundPaint", "#9be7ff");
    }};

    public static int getColor(String key) {
        return Color.parseColor(colors.get(key));
    }
}
