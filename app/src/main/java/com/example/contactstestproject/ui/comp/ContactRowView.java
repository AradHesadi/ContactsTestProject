package com.example.contactstestproject.ui.comp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.contactstestproject.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ContactRowView extends FrameLayout {

    private TextView nameTextView;
    private Paint backgroundPaint;

    public ContactRowView(@NonNull Context context) {
        super(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        initViews(context);
        initPaints();
        addView(nameTextView);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        canvas.drawRoundRect(0, 0, width, height, 300f, 50f, backgroundPaint);
    }

    public void setNameTextView(CharSequence text) {
        nameTextView.setText(text);
    }

    private void initViews(@NonNull Context context) {
        nameTextView = new TextView(context);
        nameTextView.setText(R.string.name);
        nameTextView.setTextSize(18);
        nameTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    private void initPaints(){
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#4EE3DD"));
    }
}
