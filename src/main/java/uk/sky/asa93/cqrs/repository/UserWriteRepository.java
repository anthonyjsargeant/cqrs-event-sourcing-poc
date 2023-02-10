package uk.sky.asa93.cqrs.repository;

import uk.sky.asa93.cqrs.domain.User;

import java.util.HashMap;
import java.util.Map;

public class UserWriteRepository {
    private final Map<String, User> userStore = new HashMap<>();

    public void addUser(String userId, User user) {
        userStore.put(userId, user);
    }
}
