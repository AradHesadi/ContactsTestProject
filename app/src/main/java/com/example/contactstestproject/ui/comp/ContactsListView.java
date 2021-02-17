package com.example.contactstestproject.ui.comp;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ContactsListView extends FrameLayout {

    private final RecyclerView mRecyclerView;

    public ContactsListView(@NonNull Context context) {
        super(context);
        mRecyclerView = new RecyclerView(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        addView(mRecyclerView, layoutParams);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
