package com.example.contactstestproject.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.ui.activity.ContactDetailActivity;
import com.example.contactstestproject.ui.comp.ContactRowView;

import java.util.List;


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
                    Intent intent = ContactDetailActivity.newIntent(mContext, mContact);
                    mContext.startActivity(intent);
                }
            });
        }

        public void bindProduct(int position) {
            mContact = mContacts.get(position);
            mContactRowView.setNameTextView(mContact.getName());
        }
    }
}
