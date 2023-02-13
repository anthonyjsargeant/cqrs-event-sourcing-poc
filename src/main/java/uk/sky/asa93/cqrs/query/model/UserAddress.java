package uk.sky.asa93.cqrs.query.model;

import lombok.Data;
import uk.sky.asa93.cqrs.domain.Address;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class UserAddress {
    private Map<String, Set<Address>> addressByRegion = new HashMap<>();
}
