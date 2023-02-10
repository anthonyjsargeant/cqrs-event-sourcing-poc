package uk.sky.asa93.cqrs.event.store;

import uk.sky.asa93.cqrs.event.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventStore {
    private final Map<String, List<Event>> eventStore = new HashMap<>();

    public void addEvent(String userId, Event event) {
        eventStore.put(userId, List.of(event));
    }

    public List<Event> getEvents(String userId) {
        return eventStore.get(userId);
    }
}
