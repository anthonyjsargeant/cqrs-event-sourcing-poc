package uk.sky.asa93.cqrs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import uk.sky.asa93.cqrs.aggregate.UserAggregate;
import uk.sky.asa93.cqrs.command.CreateUserCommand;
import uk.sky.asa93.cqrs.command.UpdateUserCommand;
import uk.sky.asa93.cqrs.domain.Address;
import uk.sky.asa93.cqrs.domain.Contact;
import uk.sky.asa93.cqrs.event.Event;
import uk.sky.asa93.cqrs.event.store.EventStore;
import uk.sky.asa93.cqrs.projector.UserProjector;
import uk.sky.asa93.cqrs.query.AddressByRegionQuery;
import uk.sky.asa93.cqrs.query.ContactByTypeQuery;
import uk.sky.asa93.cqrs.query.projection.UserProjection;
import uk.sky.asa93.cqrs.repository.UserReadRepository;

public class ApplicationUnitTest {

    private UserProjector projector;
    private UserAggregate userAggregate;
    private UserProjection userProjection;

    @Before
    public void setUp() {
        EventStore writeRepository = new EventStore();
        UserReadRepository readRepository = new UserReadRepository();
        projector = new UserProjector(readRepository);
        userAggregate = new UserAggregate(writeRepository);
        userProjection = new UserProjection(readRepository);
    }

    @Test
    public void givenCQRSApplication_whenCommandRun_thenQueryShouldReturnResult() {
        String userId = UUID.randomUUID()
            .toString();
        CreateUserCommand createUserCommand = new CreateUserCommand(userId, "Anthony", "Sargeant");
        List<Event> events = new ArrayList<>(userAggregate.handleCreateUserCommand(createUserCommand));

        projector.project(userId, events);

        UpdateUserCommand updateUserCommand = new UpdateUserCommand(userId, Stream.of(new Address("New York", "NY", "10001"), new Address("Los Angeles", "CA", "90001"))
            .collect(Collectors.toSet()),
            Stream.of(new Contact("EMAIL", "tom.sawyer@gmail.com"), new Contact("EMAIL", "tom.sawyer@rediff.com"))
                .collect(Collectors.toSet()));
        events.addAll(userAggregate.handleUpdateUserCommand(updateUserCommand));
        projector.project(userId, events);

        updateUserCommand = new UpdateUserCommand(userId, Stream.of(new Address("New York", "NY", "10001"), new Address("Housten", "TX", "77001"))
            .collect(Collectors.toSet()),
            Stream.of(new Contact("EMAIL", "tom.sawyer@gmail.com"), new Contact("PHONE", "700-000-0001"))
                .collect(Collectors.toSet()));
        events.addAll(userAggregate.handleUpdateUserCommand(updateUserCommand));
        projector.project(userId, events);

        ContactByTypeQuery contactByTypeQuery = new ContactByTypeQuery(userId, "EMAIL");
        assertEquals(Stream.of(new Contact("EMAIL", "tom.sawyer@gmail.com"))
            .collect(Collectors.toSet()), userProjection.handle(contactByTypeQuery));
        AddressByRegionQuery addressByRegionQuery = new AddressByRegionQuery(userId, "NY");
        assertEquals(Stream.of(new Address("New York", "NY", "10001"))
            .collect(Collectors.toSet()), userProjection.handle(addressByRegionQuery));
    }

}
