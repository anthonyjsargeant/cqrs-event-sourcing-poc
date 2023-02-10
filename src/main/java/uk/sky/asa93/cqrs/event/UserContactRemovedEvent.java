package uk.sky.asa93.cqrs.event;

public class UserContactRemovedEvent extends Event {
    private final String contactType;
    private final String contactDetails;

    public UserContactRemovedEvent(String contactType, String contactDetails) {
        this.contactType = contactType;
        this.contactDetails = contactDetails;
    }

    public String getContactType() {
        return contactType;
    }

    public String getContactDetails() {
        return contactDetails;
    }
}
