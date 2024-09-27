package com.uni.libraryproj.model;

public class Member {

    private Integer id;

    private String name;

    private Boolean isActive;

    private String contacts;

    private String membershipType;

    private Integer book_id;

    public Member() {
    }

    public Member(String name, String contacts) {
        this.name = name;
        this.contacts = contacts;
        this.membershipType = "Basic";
        this.isActive = true;
    }

    public Member(String name, Boolean isActive, String contacts, String membershipType, Integer book_id) {
        this.name = name;
        this.isActive = isActive;
        this.contacts = contacts;
        this.membershipType = membershipType;
        this.book_id = book_id;
    }

    public Integer getId() {
        return id;
    }

    public Member setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Member setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Member setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public String getContacts() {
        return contacts;
    }

    public Member setContacts(String contacts) {
        this.contacts = contacts;
        return this;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public Member setMembershipType(String membershipType) {
        this.membershipType = membershipType;
        return this;
    }

    public Integer getBook_id() {
        return book_id;
    }

    public Member setBook_Id(Integer book_id) {
        this.book_id = book_id;
        return this;
    }
}
