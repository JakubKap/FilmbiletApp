package com.companysf.filmbilet.Entities;

public class Customer {
    private String name;
    private String surname;
    private String uniqueId;

    public Customer(String name, String surname, String uniqueId) {
        this.name = name;
        this.surname = surname;
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
