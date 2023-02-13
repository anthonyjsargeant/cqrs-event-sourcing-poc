package uk.sky.asa93.cqrs.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String city;
    private String county;
    private String postcode;
}
