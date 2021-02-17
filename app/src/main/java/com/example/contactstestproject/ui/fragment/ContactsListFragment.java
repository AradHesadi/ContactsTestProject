package com.example.contactstestproject.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.ui.adapter.ContactsListAdapter;
import com.example.contactstestproject.ui.comp.ContactsListView;
import com.example.contactstestproject.viewmodel.ContactsListViewModel;

import java.util.List;
import java.util.Objects;

public class ContactsListFragment extends Fragment {

    private ContactsListView mContactsListView;
    private ContactsListAdapter mContactsListAdapter;
    private ContactsListViewModel mContactsListViewModel;

    public ContactsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(ContactsListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContactsListView = new ContactsListView(getContext());
        return mContactsListView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactsListViewModel.insertContacts();
        mContactsListViewModel.getContactsLiveData().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                setAdapter(contacts);
            }
        });
        mContactsListView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setAdapter(List<Contact> contacts) {
        if (mContactsListAdapter == null) {
            mContactsListAdapter = new ContactsListAdapter(getContext(), contacts);
            mContactsListView.getRecyclerView().setAdapter(mContactsListAdapter);
        } else {
            mContactsListAdapter.updateAdapter(contacts);
        }
    }

    public static ContactsListFragment newInstance() {
        return new ContactsListFragment();
    }
}