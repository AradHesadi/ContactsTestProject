package com.example.contactstestproject.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactstestproject.R;
import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.ui.comp.ContactDetailView;
import com.example.contactstestproject.ui.comp.ContactRowView;
import com.example.contactstestproject.ui.comp.ContactsListView;
import com.example.contactstestproject.viewmodel.ContactsListViewModel;

import java.util.List;

import static com.example.contactstestproject.utils.ApplicationUtils.getContext;

public class ContactsActivity extends AppCompatActivity {

    private static final int MENU_ITEM_BACK = 1;

    private ContactsListView mContactsListView;
    private ContactsListAdapter mContactsListAdapter;
    private ContactDetailView mContactDetailView;
    private Contact mCurrentContact;
    private boolean inDetailView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsListView = new ContactsListView(this);
        mContactDetailView = new ContactDetailView(this);
        ContactsListViewModel mContactsListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(ContactsListViewModel.class);
        setContentView(mContactsListView);
        addContentView(mContactDetailView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContactDetailView.setVisibility(View.GONE);
        mContactsListView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mContactsListViewModel.insertContacts();
        mContactsListViewModel.getContactsLiveData().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                if (inDetailView)
                    changeDetail(contacts);
                else
                    setAdapter(contacts);
            }
        });
        mContactsListView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onBackPressed() {
        if (inDetailView) {
            inDetailView = false;
            slideDetailView(inDetailView);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_BACK, Menu.NONE, getString(R.string.back));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ITEM_BACK) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void setAdapter(List<Contact> contacts) {
        if (mContactsListAdapter == null) {
            mContactsListAdapter = new ContactsListAdapter(getContext(), contacts);
            mContactsListView.getRecyclerView().setAdapter(mContactsListAdapter);
        } else {
            mContactsListAdapter.updateAdapter(contacts);
        }
    }

    private void changeDetail(List<Contact> contacts) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId().equals(mCurrentContact.getId())) {
                mCurrentContact.setName(contacts.get(i).getName());
                mCurrentContact.setPhoneNumber(contacts.get(i).getPhoneNumber());
                initDetailView();
            }
        }
    }

    private void initDetailView() {
        mContactDetailView.setNameTextView(mCurrentContact.getName());
        mContactDetailView.setPhoneTextView(mCurrentContact.getPhoneNumber());
    }

    private void slideDetailView(boolean inDetailView) {
        Transition transition = new Slide(Gravity.END);
        transition.setDuration(500);
        transition.addTarget(mContactDetailView);
        TransitionManager.beginDelayedTransition(mContactsListView, transition);
        mContactDetailView.setVisibility(inDetailView ? View.VISIBLE : View.GONE);
    }

    public class ContactsListAdapter extends
            RecyclerView.Adapter<ContactsListAdapter.ContactsListHolder> {

        private List<Contact> mContacts;
        private final Context mContext;

        public ContactsListAdapter(Context context, List<Contact> contacts) {
            mContext = context;
            mContacts = contacts;
            Log.d("testt", "1 " + contacts.size());
        }

        public void updateAdapter(List<Contact> contacts) {
            mContacts = contacts;
            notifyDataSetChanged();
            Log.d("testt", "2 " + contacts.size());
        }

        @NonNull
        @Override
        public ContactsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ContactRowView mContactRowView = new ContactRowView(mContext);
            return new ContactsListHolder(mContactRowView);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactsListHolder holder, int position) {
            holder.bindProduct(position);
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }

        public class ContactsListHolder extends RecyclerView.ViewHolder {
            private final ContactRowView mContactRowView;
            private Contact mContact;

            public ContactsListHolder(ContactRowView contactRowView) {
                super(contactRowView);
                mContactRowView = contactRowView;
                mContactRowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentContact = mContact;
                        inDetailView = true;
                        initDetailView();
                        slideDetailView(inDetailView);
                    }
                });
            }

            public void bindProduct(int position) {
                mContact = mContacts.get(position);
                mContactRowView.setNameTextView(mContact.getName());
            }
        }
    }
}
