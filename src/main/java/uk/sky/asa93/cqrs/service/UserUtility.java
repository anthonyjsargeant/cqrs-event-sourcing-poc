package uk.sky.asa93.cqrs.service;

import uk.sky.asa93.cqrs.domain.Address;
import uk.sky.asa93.cqrs.domain.Contact;
import uk.sky.asa93.cqrs.domain.User;
import uk.sky.asa93.cqrs.event.*;
import uk.sky.asa93.cqrs.event.store.EventStore;

import java.util.List;
import java.util.UUID;

public class UserUtility {

    public static User recreateUserState(EventStore store, String userId) {
        User user = null;

        List<Event> events = store.getEvents(userId);

        for (Event event : events) {
            if (event instanceof UserCreatedEvent userCreatedEvent) {
                user = new User(UUID.randomUUID()
                        .toString(), userCreatedEvent.getFirstName(), userCreatedEvent.getLastName());
            }
            if (event instanceof UserAddressAddedEvent userAddressAddedEvent) {
                Address address = new Address(
                        userAddressAddedEvent.getCity(),
                        userAddressAddedEvent.getCounty(),
                        userAddressAddedEvent.getPostCode());
                if (user != null)
                    user.getAddresses()
                            .add(address);
            }
            if (event instanceof UserAddressRemovedEvent userAddressRemovedEvent) {
                Address address = new Address(
                        userAddressRemovedEvent.getCity(),
                        userAddressRemovedEvent.getCounty(),
                        userAddressRemovedEvent.getPostCode());
                if (user != null)
                    user.getAddresses()
                            .remove(address);
            }
            if (event instanceof UserContactAddedEvent userContactAddedEvent) {
                Contact contact = new Contact(
                        userContactAddedEvent.getContactType(),
                        userContactAddedEvent.getContactDetails());
                if (user != null)
                    user.getContacts()
                            .add(contact);
            }
            if (event instanceof UserContactRemovedEvent userContactRemovedEvent) {
                Contact contact = new Contact(
                        userContactRemovedEvent.getContactType(),
                        userContactRemovedEvent.getContactDetails());
                if (user != null)
                    user.getContacts()
                            .remove(contact);
            }
        }

        return user;
    }
}
