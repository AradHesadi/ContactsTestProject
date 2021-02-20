package com.example.contactstestproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.contactstestproject.data.repository.ContactsRepository;
import com.example.contactstestproject.model.Contact;

import java.util.List;

public class ContactsListViewModel extends ViewModel {

    private final ContactsRepository repository;

    public ContactsListViewModel() {
        repository = ContactsRepository.getInstance();
    }

    public LiveData<List<Contact>> getContactsLiveData() {
        return repository.getContactsLiveData();
    }

    public void insertContacts() {
        repository.insertContacts();
    }
}
