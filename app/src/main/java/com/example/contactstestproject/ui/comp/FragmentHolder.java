package com.example.contactstestproject.ui.comp;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.contactstestproject.R;

public class FragmentHolder extends FrameLayout {

    public FragmentHolder(@NonNull Context context) {
        super(context);
        setId(R.id.fragment_holder);
    }
}
