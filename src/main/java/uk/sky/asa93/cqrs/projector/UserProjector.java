package uk.sky.asa93.cqrs.projector;

import uk.sky.asa93.cqrs.domain.Address;
import uk.sky.asa93.cqrs.domain.Contact;
import uk.sky.asa93.cqrs.domain.User;
import uk.sky.asa93.cqrs.query.model.UserAddress;
import uk.sky.asa93.cqrs.query.model.UserContact;
import uk.sky.asa93.cqrs.repository.UserReadRepository;

import java.util.*;

public class UserProjector {

    private final UserReadRepository userReadRepository;

    public UserProjector(UserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    public void project(User user) {
        projectUserContacts(user);
        projectUserAddress(user);
    }

    private void projectUserAddress(User user) {
        UserAddress userAddress = Optional.ofNullable(
                        userReadRepository.getUserAddress(user.getUserId()))
                .orElse(new UserAddress());
        Map<String, Set<Address>> addressByRegion = new HashMap<>();
        for (Address address : user.getAddresses()) {
            Set<Address> addresses = Optional.ofNullable(
                            addressByRegion.get(address.getCounty()))
                    .orElse(new HashSet<>());
            addresses.add(address);
            addressByRegion.put(address.getCounty(), addresses);
        }
        userAddress.setAddressByRegion(addressByRegion);
        userReadRepository.addUserAddress(user.getUserId(), userAddress);
    }

    private void projectUserContacts(User user) {
        UserContact userContact = Optional.ofNullable(userReadRepository.getUserContact(user.getUserId()))
                .orElse(new UserContact());
        Map<String, Set<Contact>> contactByType = new HashMap<>();

        for (Contact contact : user.getContacts()) {
            Set<Contact> contacts = Optional.ofNullable(contactByType.get(contact.getType()))
                    .orElse(new HashSet<>());
            contacts.add(contact);
            contactByType.put(contact.getType(), contacts);
        }

        userContact.setContactByType(contactByType);
        userReadRepository.addUserContact(user.getUserId(), userContact);
    }
}
