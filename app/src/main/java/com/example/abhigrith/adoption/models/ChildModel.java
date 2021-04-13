package com.example.abhigrith.adoption.models;

public class ChildModel {

    private String childDateOfBirth;
    private String childFullName;
    private String childGender;
    private String childId;
    private String childImageUrl;

    // Empty Constructor for firebase
    public ChildModel() {

    }

    public ChildModel(String childDateOfBirth, String childFullName, String childGender, String childId, String childImageUrl) {
        this.childDateOfBirth = childDateOfBirth;
        this.childFullName = childFullName;
        this.childGender = childGender;
        this.childId = childId;
        this.childImageUrl = childImageUrl;
    }

    public String getChildDateOfBirth() {
        return childDateOfBirth;
    }

    public void setChildDateOfBirth(String childDateOfBirth) {
        this.childDateOfBirth = childDateOfBirth;
    }

    public String getChildFullName() {
        return childFullName;
    }

    public void setChildFullName(String childFullName) {
        this.childFullName = childFullName;
    }

    public String getChildGender() {
        return childGender;
    }

    public void setChildGender(String childGender) {
        this.childGender = childGender;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildImageUrl() {
        return childImageUrl;
    }

    public void setChildImageUrl(String childImageUrl) {
        this.childImageUrl = childImageUrl;
    }
}
