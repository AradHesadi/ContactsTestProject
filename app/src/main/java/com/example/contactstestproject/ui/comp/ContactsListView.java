package com.example.contactstestproject.ui.comp;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactstestproject.ui.Theme;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ContactsListView extends FrameLayout {

    private final RecyclerView mRecyclerView;

    public ContactsListView(@NonNull Context context) {
        super(context);
        mRecyclerView = new RecyclerView(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(Theme.getColor("contactListViewBackgroundPaint"));
        addView(mRecyclerView, layoutParams);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
