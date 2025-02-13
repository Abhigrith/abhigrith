package com.example.abhigrith.enums;

public enum OrphanageListingStatus {

    ACCEPTED("Accepted"), PENDING("Pending"), REJECTED("Rejected");

    private String orphangeListingStatus;

    OrphanageListingStatus(String orphangeListingStatus) {
        this.orphangeListingStatus = orphangeListingStatus;
    }

    public String getOrphangeListingStatus() {
        return this.orphangeListingStatus;
    }

}
