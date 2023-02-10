package uk.sky.asa93.cqrs.event;

public class UserAddressRemovedEvent extends Event {
    private String city;
    private String county;
    private String postCode;

    public UserAddressRemovedEvent(String city, String county, String postCode) {
        this.city = city;
        this.county = county;
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getPostCode() {
        return postCode;
    }
}
