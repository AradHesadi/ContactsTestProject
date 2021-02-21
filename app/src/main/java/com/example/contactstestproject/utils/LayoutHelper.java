package com.example.contactstestproject.utils;

import android.widget.FrameLayout;

public class LayoutHelper {

    public static Float density = 1f;

    public static FrameLayout.LayoutParams createFrame(int width, int height, int gravity, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getSize(width), getSize(height), gravity);
        layoutParams.setMargins(LayoutHelper.dp(leftMargin), LayoutHelper.dp(topMargin), LayoutHelper.dp(rightMargin), LayoutHelper.dp(bottomMargin));
        return layoutParams;
    }

    public static FrameLayout.LayoutParams createFrame(int width, int height, int gravity) {
        return new FrameLayout.LayoutParams(getSize(width), getSize(height), gravity);
    }

    public static FrameLayout.LayoutParams createFrame(int width, int height) {
        return new FrameLayout.LayoutParams(getSize(width), getSize(height));
    }

    private static int getSize(float size) {
        return (int) (size < 0 ? size : LayoutHelper.dp(size));
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }
}
