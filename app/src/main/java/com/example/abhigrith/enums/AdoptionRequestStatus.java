package com.example.abhigrith.enums;

public enum AdoptionRequestStatus {

    PENDING("Pending"), ACCEPTED("Accepted"), REJECTED("Rejected");

    private String adoptionStatus;

    AdoptionRequestStatus(String adoptionStatus){
        this.adoptionStatus = adoptionStatus;
    }

    public String getAdoptionStatus(){
        return this.adoptionStatus;
    }
}

