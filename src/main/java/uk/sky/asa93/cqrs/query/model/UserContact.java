package uk.sky.asa93.cqrs.query.model;

import lombok.Data;
import uk.sky.asa93.cqrs.domain.Contact;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class UserContact {
    private Map<String, Set<Contact>> contactByType = new HashMap<>();
}
