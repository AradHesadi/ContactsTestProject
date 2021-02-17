package com.example.contactstestproject.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.contactstestproject.R;
import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.ui.comp.ContactDetailView;
import com.example.contactstestproject.viewmodel.ContactDetailViewModel;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ContactDetailFragment extends Fragment {

    private ContactDetailViewModel mContactDetailViewModel;
    private ContactDetailView mContactDetailView;
    private Contact mContact;
    public static final String ARGS_CONTACT = "ARGS_CONTACT";

    public ContactDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactDetailViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(ContactDetailViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContactDetailView = new ContactDetailView(getContext());
        return mContactDetailView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContact = (Contact) (getArguments() != null ? getArguments().getSerializable(ARGS_CONTACT) : null);
        initData(mContact);
        mContactDetailViewModel.getContactsLiveData().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                initData(mContactDetailViewModel.getContactNewInfo(contacts, mContact));
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contact_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_back) {
            Objects.requireNonNull(getActivity()).onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData(Contact contact) {
        if (contact != null) {
            mContactDetailView.setNameTextView(contact.getName());
            mContactDetailView.setPhoneTextView(String.format(getString(R.string.number_contact_detail),
                    contact.getPhoneNumber()));
        } else {
            mContactDetailView.setNameTextView(getString(R.string.no_contact));
            mContactDetailView.setPhoneTextView("");
        }
    }

    public static ContactDetailFragment newInstance(Serializable serializable) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CONTACT, serializable);
        fragment.setArguments(args);
        return fragment;
    }
}