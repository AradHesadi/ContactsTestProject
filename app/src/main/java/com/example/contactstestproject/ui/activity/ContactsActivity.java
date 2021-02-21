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

public class ContactsActivity extends AppCompatActivity {

    private static final int MENU_ITEM_BACK = 1;

    private ContactsListView contactsListView;
    private ContactsListAdapter contactsListAdapter;
    private ContactDetailView contactDetailView;
    private Contact currentContact;
    private boolean inDetailView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsListView = new ContactsListView(this);
        contactDetailView = new ContactDetailView(this);
        ContactsListViewModel mContactsListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(ContactsListViewModel.class);
        setContentView(contactsListView);
        addContentView(contactDetailView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contactDetailView.setVisibility(View.GONE);
        contactsListView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
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
        contactsListView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        if (inDetailView) {
            inDetailView = false;
            slideDetailView(false);
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
        if (contactsListAdapter == null) {
            contactsListAdapter = new ContactsListAdapter(this, contacts);
            contactsListView.getRecyclerView().setAdapter(contactsListAdapter);
        } else {
            contactsListAdapter.updateAdapter(contacts);
        }
    }

    private void changeDetail(List<Contact> contacts) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == (currentContact.getId())) {
                currentContact.setName(contacts.get(i).getName());
                currentContact.setPhoneNumber(contacts.get(i).getPhoneNumber());
                initDetailView();
            }
        }
    }

    private void initDetailView() {
        contactDetailView.setNameTextView(currentContact.getName());
        contactDetailView.setPhoneTextView(currentContact.getPhoneNumber());
    }

    private void slideDetailView(boolean inDetailView) {
        Transition transition = new Slide(Gravity.END);
        transition.setDuration(500);
        transition.addTarget(contactDetailView);
        TransitionManager.beginDelayedTransition(contactsListView, transition);
        contactDetailView.setVisibility(inDetailView ? View.VISIBLE : View.GONE);
    }

    public class ContactsListAdapter extends
            RecyclerView.Adapter<ContactsListAdapter.ContactsListHolder> {

        private List<Contact> contacts;
        private final Context context;

        public ContactsListAdapter(Context context, List<Contact> contacts) {
            this.context = context;
            this.contacts = contacts;
            Log.d("testt", "1 " + contacts.size());
        }

        public void updateAdapter(List<Contact> contacts) {
            this.contacts = contacts;
            notifyDataSetChanged();
            Log.d("testt", "2 " + contacts.size());
        }

        @NonNull
        @Override
        public ContactsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ContactRowView mContactRowView = new ContactRowView(context);
            return new ContactsListHolder(mContactRowView);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactsListHolder holder, int position) {
            holder.bindProduct(position);
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        public class ContactsListHolder extends RecyclerView.ViewHolder {
            private final ContactRowView contactRowView;
            private Contact contact;

            public ContactsListHolder(final ContactRowView contactRowView) {
                super(contactRowView);
                this.contactRowView = contactRowView;
                this.contactRowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!inDetailView) {
                            currentContact = contact;
                            inDetailView = true;
                            initDetailView();
                            slideDetailView(inDetailView);
                        }
                    }
                });
            }

            public void bindProduct(int position) {
                contact = contacts.get(position);
                contactRowView.setNameTextView(contact.getName());
            }
        }
    }
}
