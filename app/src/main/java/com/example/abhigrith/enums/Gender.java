package com.example.abhigrith.enums;

public enum Gender {
    MALE("Male"), FEMALE("Female"), OTHERS("Others");

    private String gender;

    Gender(String gender){
        this.gender = gender;
    }

    public String getGender(){
        return this.gender;
    }
}
