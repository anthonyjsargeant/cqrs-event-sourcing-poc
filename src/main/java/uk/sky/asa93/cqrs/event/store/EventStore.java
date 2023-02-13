package uk.sky.asa93.cqrs.event.store;

import uk.sky.asa93.cqrs.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventStore {
    private final Map<String, List<Event>> eventStore = new HashMap<>();

    public void addEvent(String id, Event event) {
        List<Event> events = eventStore.get(id);
        if (events == null) {
            events = new ArrayList<>();
            events.add(event);
            eventStore.put(id, events);
        } else {
            events.add(event);
        }
    }

    public List<Event> getEvents(String id) {
        return eventStore.get(id);
    }
}
