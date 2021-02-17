package com.example.contactstestproject.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.contactstestproject.ui.fragment.ContactsListFragment;
import com.example.contactstestproject.utils.contacts.ContactPermissionUtils;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class ContactsListActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return ContactsListFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ContactPermissionUtils.requestPermission(this);

        AppCenter.start(getApplication(), "20b55e59-360b-47b7-b153-95887833cc77",
                Analytics.class, Crashes.class);
    }
}

