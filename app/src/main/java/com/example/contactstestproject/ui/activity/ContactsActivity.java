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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactstestproject.R;
import com.example.contactstestproject.data.repository.ContactsRepository;
import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.ui.comp.ContactDetailView;
import com.example.contactstestproject.ui.comp.ContactRowView;
import com.example.contactstestproject.ui.comp.ContactsListView;
import com.example.contactstestproject.utils.LayoutHelper;

import java.util.List;

import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import ir.tapsell.sdk.bannerads.TapsellBannerType;
import ir.tapsell.sdk.bannerads.TapsellBannerView;
import ir.tapsell.sdk.bannerads.TapsellBannerViewEventListener;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static ir.tapsell.sdk.TapsellAdActivity.ZONE_ID;

public class ContactsActivity extends AppCompatActivity {

    public static final String BUNDLE_CONTACT = "BUNDLE_CONTACT";
    public static final String BUNDLE_IN_DETAIL_VIEW = "BUNDLE_IN_DETAIL_VIEW";
    private static final int MENU_ITEM_BACK = 1;
    public static final int SLIDE_DURATION = 500;
    public static final String ZONE_ID = "5dcfc416bbfcde00017e027d";

    private ContactsListView contactsListView;
    private TapsellBannerView tapsellBannerView;
    private ContactsListAdapter contactsListAdapter;
    private ContactDetailView contactDetailView;
    private Contact currentContact;
    private boolean inDetailView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactsRepository repository = ContactsRepository.getInstance();
        contactsListView = new ContactsListView(this);
        contactDetailView = new ContactDetailView(this);
        setContentView(contactsListView);
        addContentView(contactDetailView, LayoutHelper.createFrame(MATCH_PARENT, MATCH_PARENT));
        contactDetailView.setVisibility(View.GONE);
        contactsListView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        repository.getContactsLiveData().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                if (inDetailView)
                    changeDetail(contacts);
                else
                    setAdapter(contacts);
            }
        });
        contactsListView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        if (savedInstanceState != null) {
            inDetailView = savedInstanceState.getBoolean(BUNDLE_IN_DETAIL_VIEW);
            currentContact = (Contact) savedInstanceState.getSerializable(BUNDLE_CONTACT);
            if (inDetailView) {
                contactDetailView.setVisibility(View.VISIBLE);
                initDetailView();
            }
        }
        repository.insertContacts();

        Tapsell.requestAd(this,
                ZONE_ID,
                new TapsellAdRequestOptions(),
                new TapsellAdRequestListener() {
                    @Override
                    public void onAdAvailable(String adId) {
                        Log.d("ttttt",adId);
                    }

                    @Override
                    public void onError(String message) {
                        Log.d("ttttt",message);
                    }
                });

        tapsellBannerView = new TapsellBannerView(this, TapsellBannerType.BANNER_250x250, ZONE_ID);
        tapsellBannerView.loadAd(this,ZONE_ID,TapsellBannerType.BANNER_250x250);
        setContentView(tapsellBannerView,LayoutHelper.createFrame(200,200,Gravity.CENTER));
        tapsellBannerView.setEventListener(new TapsellBannerViewEventListener() {
            @Override
            public void onNoAdAvailable() {
                Log.d("tttt","onNoAdAvailable");
            }

            @Override
            public void onNoNetwork() {
                Log.d("tttt","onNoNetwork");
            }

            @Override
            public void onError(String s) {
                Log.d("tttt",s);
            }

            @Override
            public void onRequestFilled() {
                Log.d("tttt","onRequestFilled");
            }

            @Override
            public void onHideBannerView() {
                Log.d("tttt","onHideBannerView");
            }
        });
        tapsellBannerView.showBannerView();
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_CONTACT, currentContact);
        outState.putBoolean(BUNDLE_IN_DETAIL_VIEW, inDetailView);
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
        transition.setDuration(SLIDE_DURATION);
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
