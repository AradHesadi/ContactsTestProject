package com.example.contactstestproject.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactstestproject.R;
import com.example.contactstestproject.databinding.RowContactsListBinding;
import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.ui.activity.ContactDetailActivity;

import java.util.List;


public class ContactsListAdapter extends
        RecyclerView.Adapter<ContactsListAdapter.ContactsListHolder> {

    private List<Contact> mContacts;
    private final Context mContext;

    public ContactsListAdapter(Context context, List<Contact> contacts) {
        mContext = context;
        mContacts = contacts;
        Log.d("testt","1 "+contacts.size());
    }

    public void updateAdapter(List<Contact> contacts) {
        mContacts = contacts;
        notifyDataSetChanged();
        Log.d("testt","2 "+contacts.size());
    }

    @NonNull
    @Override
    public ContactsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RowContactsListBinding rowContactsListBinding =
                DataBindingUtil.inflate(
                        inflater,
                        R.layout.row_contacts_list,
                        parent,
                        false);
        return new ContactsListHolder(rowContactsListBinding);
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
        private final RowContactsListBinding mRowContactsListBinding;
        private Contact mContact;

        public ContactsListHolder(RowContactsListBinding rowContactsListBinding) {
            super(rowContactsListBinding.getRoot());
            mRowContactsListBinding = rowContactsListBinding;
            mRowContactsListBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = ContactDetailActivity.newIntent(mContext, mContact);
                    mContext.startActivity(intent);
                }
            });
        }

        public void bindProduct(int position) {
            mContact = mContacts.get(position);
            mRowContactsListBinding.textViewName.setText(mContact.getName());
        }
    }
}
