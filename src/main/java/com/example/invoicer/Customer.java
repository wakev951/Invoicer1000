package com.example.invoicer;

public class Customer {
    private String name;
    private String email;
    private String address;
    private String city;
    private String postalCode;

    public Customer(String name, String email, String address, String city, String postalCode) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
