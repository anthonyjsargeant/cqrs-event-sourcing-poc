package uk.sky.asa93.cqrs.aggregate;

import uk.sky.asa93.cqrs.command.CreateUserCommand;
import uk.sky.asa93.cqrs.command.UpdateUserCommand;
import uk.sky.asa93.cqrs.domain.Address;
import uk.sky.asa93.cqrs.domain.Contact;
import uk.sky.asa93.cqrs.domain.User;
import uk.sky.asa93.cqrs.event.*;
import uk.sky.asa93.cqrs.event.store.EventStore;

import java.util.ArrayList;
import java.util.List;

import static uk.sky.asa93.cqrs.service.UserUtility.recreateUserState;

public class UserAggregate {
    private final EventStore writeRepository;

    public UserAggregate(EventStore repository) {
        this.writeRepository = repository;
    }

    public List<Event> handleCreateUserCommand(CreateUserCommand command) {
        UserCreatedEvent event =
            new UserCreatedEvent(command.getUserId(), command.getFirstName(), command.getLastName());
        writeRepository.addEvent(command.getUserId(), event);
        return List.of(event);
    }

    public List<Event> handleUpdateUserCommand(UpdateUserCommand command) {
        User user = recreateUserState(writeRepository, command.getUserId());
        List<Event> events = new ArrayList<>();

        List<Contact> contactsToRemove = user.getContacts()
            .stream()
            .filter(contact -> !command.getContacts().contains(contact))
            .toList();

        for (Contact contact : contactsToRemove) {
            UserContactRemovedEvent userContactRemovedEvent = new UserContactRemovedEvent(contact.getType(),
                contact.getDetail());
            events.add(userContactRemovedEvent);
            writeRepository.addEvent(command.getUserId(), userContactRemovedEvent);
        }

        addContacts(command, user, events);
        removeAddresses(command, user, events);
        addAddresses(command, user, events);

        return events;
    }

    private void removeAddresses(UpdateUserCommand command, User user, List<Event> events) {
        List<Address> addressesToRemove = user.getAddresses()
            .stream()
            .filter(address -> !user.getAddresses().contains(address))
            .toList();

        for (Address address : addressesToRemove) {
            UserAddressRemovedEvent userAddressRemovedEvent =
                new UserAddressRemovedEvent(address.getCity(), address.getCounty(), address.getPostcode());
            events.add(userAddressRemovedEvent);
            writeRepository.addEvent(command.getUserId(), userAddressRemovedEvent);
        }
    }

    private void addAddresses(UpdateUserCommand command, User user, List<Event> events) {
        List<Address> addressesToAdd = command.getAddresses()
            .stream()
            .filter(address -> !user.getAddresses().contains(address))
            .toList();

        for (Address address : addressesToAdd) {
            UserAddressAddedEvent userAddressAddedEvent
                = new UserAddressAddedEvent(address.getCity(), address.getCounty(), address.getPostcode());
            events.add(userAddressAddedEvent);
            writeRepository.addEvent(command.getUserId(), userAddressAddedEvent);
        }
    }

    private void addContacts(UpdateUserCommand command, User user, List<Event> events) {
        List<Contact> contactsToAdd = command.getContacts().stream()
            .filter(contact -> !user.getContacts().contains(contact))
            .toList();
        for (Contact contact : contactsToAdd) {
            UserContactAddedEvent contactAddedEvent = new UserContactAddedEvent(contact.getType(),
                contact.getDetail());
            events.add(contactAddedEvent);
            writeRepository.addEvent(command.getUserId(), contactAddedEvent);
        }
    }
}
