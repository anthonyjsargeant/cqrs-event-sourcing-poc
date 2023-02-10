package uk.sky.asa93.cqrs.repository;

import uk.sky.asa93.cqrs.query.model.UserAddress;
import uk.sky.asa93.cqrs.query.model.UserContact;

import java.util.HashMap;
import java.util.Map;

public class UserReadRepository {
    private final Map<String, UserAddress> userAddress = new HashMap<>();
    private final Map<String, UserContact> userContact = new HashMap<>();

    public UserContact getUserContact(String userId) {
        return userContact.get(userId);
    }

    public UserAddress getUserAddress(String userId) {
        return userAddress.get(userId);
    }

    public void addUserContact(String userId, UserContact userContact) {
        this.userContact.put(userId, userContact);
    }

    public void addUserAddress(String userId, UserAddress userAddress) {
        this.userAddress.put(userId, userAddress);
    }
}
