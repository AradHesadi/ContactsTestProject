package com.example.contactstestproject.ui.comp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.contactstestproject.R;
import com.example.contactstestproject.ui.Theme;

public class ContactDetailView extends FrameLayout {

    private static final String TAG = "MSG";
    private TextView nameTextView;
    private TextView phoneTextView;
    private ImageView contactImageView;
    private Paint backgroundPaint;
    private Paint topPaint;

    public ContactDetailView(@NonNull Context context) {
        super(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        initViews(context);
        addViews(height, width);
        initPaints();
        setWillNotDraw(false);
        Log.d(TAG, "constructor: " + height + " " + width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        Log.d(TAG, "onDraw: " + height + " " + width);
        canvas.drawPaint(backgroundPaint);
        canvas.drawRect(0, 0, width, height / 3f, topPaint);
    }

    public void setNameTextView(CharSequence text) {
        nameTextView.setText(text);
    }

    public void setPhoneTextView(CharSequence text) {
        if (text != null)
            phoneTextView.setText(text);
    }

    private void initViews(@NonNull Context context) {
        nameTextView = new TextView(context);
        nameTextView.setText(R.string.contact_name);
        nameTextView.setTextSize(18);
        nameTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        phoneTextView = new TextView(context);
        phoneTextView.setTextSize(18);
        phoneTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        phoneTextView.setText(R.string.no_number);
        contactImageView = new ImageView(context);
        contactImageView.setImageResource(R.drawable.ic_contact_foreground);
    }

    private void addViews(int height, int width) {
        LayoutParams imageLayoutParams = new LayoutParams(height / 4, height / 4, Gravity.CENTER | Gravity.TOP);
        imageLayoutParams.setMargins(0, 60, 0, 0);
        LayoutParams nameLayoutParams = new LayoutParams(width, 100, Gravity.CENTER);
        LayoutParams phoneLayoutParams = new LayoutParams(width, 100, Gravity.CENTER);
        phoneLayoutParams.setMargins(0, 160, 0, 0);
        addView(nameTextView, nameLayoutParams);
        addView(contactImageView, imageLayoutParams);
        addView(phoneTextView, phoneLayoutParams);
    }

    private void initPaints() {
        topPaint = new Paint();
        topPaint.setShadowLayer(10f, 0, 4.0f, Color.BLUE);
        topPaint.setColor(Theme.getColor("contactDetailViewTopPaint"));
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Theme.getColor("contactDetailViewBackground"));
    }
}
