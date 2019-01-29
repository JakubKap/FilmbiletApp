package com.companysf.filmbilet.entities;

public class Customer {
    private String name;
    private String surname;
    private String email;
    private int id;

    public Customer(String name, String surname, String email, int id) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
