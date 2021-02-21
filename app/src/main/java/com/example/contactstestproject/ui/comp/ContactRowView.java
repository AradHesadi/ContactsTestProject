package com.example.contactstestproject.ui.comp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.contactstestproject.R;
import com.example.contactstestproject.ui.Theme;
import com.example.contactstestproject.utils.LayoutHelper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ContactRowView extends FrameLayout {

    public static final String CONTACT_ROW_VIEW_BACKGROUND = "contactRowViewBackground";
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
        canvas.drawRoundRect(LayoutHelper.dp(20), LayoutHelper.dp(16)
                , width - LayoutHelper.dp(20)
                , height - LayoutHelper.dp(10)
                , LayoutHelper.dp(80)
                , LayoutHelper.dp(50), backgroundPaint);
    }

    public void setNameTextView(CharSequence text) {
        nameTextView.setText(text);
    }

    private void initViews(@NonNull Context context) {
        this.setLayoutParams(LayoutHelper.createFrame(MATCH_PARENT, 160));
        nameTextView = new TextView(context);
        nameTextView.setText(R.string.name);
        nameTextView.setTextSize(26);
        nameTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        addView(nameTextView, LayoutHelper.createFrame(WRAP_CONTENT, WRAP_CONTENT
                , Gravity.CENTER, 0, 0, 0, 0));
    }

    private void initPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Theme.getColor(CONTACT_ROW_VIEW_BACKGROUND));
    }
}
