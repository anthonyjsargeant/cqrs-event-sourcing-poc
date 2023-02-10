package uk.sky.asa93.cqrs.service;

import uk.sky.asa93.cqrs.domain.Address;
import uk.sky.asa93.cqrs.domain.Contact;
import uk.sky.asa93.cqrs.domain.User;
import uk.sky.asa93.cqrs.event.*;
import uk.sky.asa93.cqrs.event.store.EventStore;

import java.util.Set;
import java.util.stream.Collectors;

public class UserService {

    private final EventStore eventStore;

    public UserService(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void createUser(String userId, String firstName, String lastName) {
        eventStore.addEvent(userId, new UserCreatedEvent(userId, firstName, lastName));
    }

    public void updateUser(String userId, Set<Contact> contacts, Set<Address> addresses) {
        User user = UserUtility.recreateUserState(eventStore, userId);
        updateContacts(userId, contacts, user);

        updateAddresses(userId, addresses, user);
    }

    private void updateAddresses(String userId, Set<Address> addresses, User user) {
        removeAddresses(userId, addresses, user);
        addAddresses(userId, addresses, user);
    }

    private void addAddresses(String userId, Set<Address> addresses, User user) {
        addresses.stream()
            .filter(a -> !user.getAddresses().contains(a))
            .forEach(a -> eventStore.addEvent(
                userId, new UserAddressAddedEvent(a.getCity(), a.getCounty(), a.getPostcode())));
    }

    private void removeAddresses(String userId, Set<Address> addresses, User user) {
        user.getAddresses().stream()
            .filter(a -> !addresses.contains(a))
            .forEach(a -> eventStore.addEvent(
                userId, new UserAddressRemovedEvent(a.getCity(), a.getCounty(), a.getPostcode())));
    }

    private void updateContacts(String userId, Set<Contact> contacts, User user) {
        removeContacts(userId, contacts, user);
        addContacts(userId, contacts, user);
    }

    private void addContacts(String userId, Set<Contact> contacts, User user) {
        contacts.stream()
            .filter(contact -> !user.getContacts().contains(contact))
            .forEach(contact ->
                eventStore.addEvent(userId, new UserContactAddedEvent(contact.getType(), contact.getDetail())));
    }

    private void removeContacts(String userId, Set<Contact> contacts, User user) {
        user.getContacts()
            .stream()
            .filter(contact -> !contacts.contains(contact))
            .forEach(contact ->
                eventStore.addEvent(userId, new UserContactRemovedEvent(contact.getType(), contact.getDetail())));
    }

    public Set<Contact> getContactByType(String userId, String contactType) {
        User user = UserUtility.recreateUserState(eventStore, userId);
        return user.getContacts()
            .stream()
            .filter(contact -> contact.getType().equals(contactType))
            .collect(Collectors.toSet());
    }

    public Set<Address> getAddressByRegion(String userId, String county) {
        User user = UserUtility.recreateUserState(eventStore, userId);
        return user.getAddresses().stream()
            .filter(address -> address.getCounty().equals(county))
            .collect(Collectors.toSet());
    }
}
