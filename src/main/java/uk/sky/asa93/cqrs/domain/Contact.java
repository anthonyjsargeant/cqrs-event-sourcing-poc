package uk.sky.asa93.cqrs.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Contact {
    private String type;
    private String detail;
}
