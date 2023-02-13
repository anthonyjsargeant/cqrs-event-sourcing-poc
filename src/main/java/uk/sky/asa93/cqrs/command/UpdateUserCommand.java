package uk.sky.asa93.cqrs.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import uk.sky.asa93.cqrs.domain.Address;
import uk.sky.asa93.cqrs.domain.Contact;

import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateUserCommand {
    private String userId;
    private Set<Address> addresses;
    private Set<Contact> contacts;
}
