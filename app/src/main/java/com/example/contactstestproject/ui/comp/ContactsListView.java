package com.example.contactstestproject.ui.comp;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactstestproject.ui.Theme;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ContactsListView extends FrameLayout {

    private final RecyclerView recyclerView;

    public ContactsListView(@NonNull Context context) {
        super(context);
        recyclerView = new RecyclerView(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(Theme.getColor("contactListViewBackgroundPaint"));
        addView(recyclerView, layoutParams);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
