package com.example.contactstestproject.ui.comp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.contactstestproject.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ContactRowView extends FrameLayout {

    private TextView nameTextView;
    private Paint backgroundPaint;

    public ContactRowView(@NonNull Context context) {
        super(context);
        initViews(context);
        initPaints();
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        canvas.drawRoundRect(20, 16, width-20, height-10, 80f, 50f, backgroundPaint);
    }

    public void setNameTextView(CharSequence text) {
        nameTextView.setText(text);
    }

    private void initViews(@NonNull Context context) {
        ViewGroup.LayoutParams baseLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        this.setLayoutParams(baseLayoutParams);
        nameTextView = new TextView(context);
        nameTextView.setText(R.string.name);
        nameTextView.setTextSize(26);
        nameTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        FrameLayout.LayoutParams nameLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER);
        nameLayoutParams.setMargins(0,32,0,32);
        addView(nameTextView,nameLayoutParams);
    }

    private void initPaints(){
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#4EE3DD"));
    }
}
