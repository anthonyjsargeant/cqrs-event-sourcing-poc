package uk.sky.asa93.cqrs.query;

public class ContactByTypeQuery {
    private final String userId;
    private final String contactType;

    public ContactByTypeQuery(String userId, String contactType) {
        this.userId = userId;
        this.contactType = contactType;
    }

    public String getUserId() {
        return userId;
    }

    public String getContactType() {
        return contactType;
    }
}
