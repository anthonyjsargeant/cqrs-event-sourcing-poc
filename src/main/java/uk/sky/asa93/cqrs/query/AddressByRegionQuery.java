package uk.sky.asa93.cqrs.query;

public class AddressByRegionQuery {
    private final String userId;
    private final String county;

    public AddressByRegionQuery(String userId, String county) {
        this.userId = userId;
        this.county = county;
    }

    public String getUserId() {
        return userId;
    }

    public String getCounty() {
        return county;
    }
}
