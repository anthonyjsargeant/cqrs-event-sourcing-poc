package uk.sky.asa93.cqrs.projector;

import uk.sky.asa93.cqrs.domain.Address;
import uk.sky.asa93.cqrs.domain.Contact;
import uk.sky.asa93.cqrs.event.Event;
import uk.sky.asa93.cqrs.event.UserAddressAddedEvent;
import uk.sky.asa93.cqrs.event.UserAddressRemovedEvent;
import uk.sky.asa93.cqrs.event.UserContactAddedEvent;
import uk.sky.asa93.cqrs.event.UserContactRemovedEvent;
import uk.sky.asa93.cqrs.query.model.UserAddress;
import uk.sky.asa93.cqrs.query.model.UserContact;
import uk.sky.asa93.cqrs.repository.UserReadRepository;

import java.util.*;

public class UserProjector {

    private final UserReadRepository userReadRepository;

    public UserProjector(UserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    public void project(String userId, List<Event> events) {
        for (Event event : events) {
            if (event instanceof UserAddressAddedEvent) {
                apply(userId, (UserAddressAddedEvent) event);
            }
            if (event instanceof UserAddressRemovedEvent) {
                apply(userId, (UserAddressRemovedEvent) event);
            }
            if (event instanceof UserContactAddedEvent) {
                apply(userId, (UserContactAddedEvent) event);
            }
            if (event instanceof UserContactRemovedEvent) {
                apply(userId, (UserContactRemovedEvent) event);
            }
        }
    }

    private void apply(String userId, UserAddressAddedEvent event) {
        Address address = new Address(event.getCity(), event.getCounty(), event.getPostCode());
        UserAddress userAddress = Optional.ofNullable(userReadRepository.getUserAddress(userId))
            .orElse(new UserAddress());
        Set<Address> addresses = Optional.ofNullable(userAddress.getAddressByRegion()
            .get(address.getCounty()))
            .orElse(new HashSet<>());
        addresses.add(address);
        userAddress.getAddressByRegion().put(address.getCounty(), addresses);
        userReadRepository.addUserAddress(userId, userAddress);
    }

    private void apply(String userId, UserAddressRemovedEvent event) {
        Address address = new Address(event.getCity(), event.getCounty(), event.getPostCode());
        UserAddress userAddress = userReadRepository.getUserAddress(userId);
        if (userAddress != null) {
            Set<Address> addresses = userAddress.getAddressByRegion()
              .get(address.getCounty());
            if (addresses != null)
                addresses.remove(address);
            userReadRepository.addUserAddress(userId, userAddress);
        }
    }
    
    public void apply(String userId, UserContactAddedEvent event) {
        Contact contact = new Contact(event.getContactType(), event.getContactDetails());
        UserContact userContact = Optional.ofNullable(userReadRepository.getUserContact(userId))
            .orElse(new UserContact());
        Set<Contact> contacts = Optional.ofNullable(userContact.getContactByType()
            .get(contact.getType()))
            .orElse(new HashSet<>());
            contacts.add(contact);

        userContact.getContactByType().put(contact.getType(), contacts);
        userReadRepository.addUserContact(userId, userContact);
    }

    private void apply(String userId, UserContactRemovedEvent event) {
        Contact contact = new Contact(event.getContactType(), event.getContactDetails());
        UserContact userContact = userReadRepository.getUserContact(userId);
        if (userContact != null) {
            Set<Contact> contacts = userContact.getContactByType()
                .get(contact.getType());
            if (contacts != null)
                contacts.remove(contact);
                userReadRepository.addUserContact(userId, userContact);
        }
    }
}
