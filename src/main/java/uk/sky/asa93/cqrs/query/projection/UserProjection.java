package uk.sky.asa93.cqrs.query.projection;

import uk.sky.asa93.cqrs.domain.Address;
import uk.sky.asa93.cqrs.domain.Contact;
import uk.sky.asa93.cqrs.query.AddressByRegionQuery;
import uk.sky.asa93.cqrs.query.ContactByTypeQuery;
import uk.sky.asa93.cqrs.query.model.UserAddress;
import uk.sky.asa93.cqrs.query.model.UserContact;
import uk.sky.asa93.cqrs.repository.UserReadRepository;

import java.util.Set;

public class UserProjection {
    private final UserReadRepository userReadRepository;

    public UserProjection(UserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    public Set<Contact> handle(ContactByTypeQuery query) {
        UserContact userContact = userReadRepository.getUserContact(query.getUserId());
        return userContact.getContactByType().get(query.getContactType());
    }

    public Set<Address> handle(AddressByRegionQuery query) {
        UserAddress userAddress = userReadRepository.getUserAddress(query.getUserId());
        return userAddress.getAddressByRegion().get(query.getCounty());
    }
}
