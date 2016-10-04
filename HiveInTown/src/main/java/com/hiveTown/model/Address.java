package com.hiveTown.model;
 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="address")
public class Address {
 
    @Id
    @Column(name="idAddress")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
 
    private String addressLine1;
    private String addressLine2;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    
    
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getAddressLine1() {
        return this.addressLine1;
    }
 
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }
    
    
    public String getAddressLine2() {
        return this.addressLine2;
    }
 
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    
    public String getStreet() {
        return this.street;
    }
 
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return this.city;
    }
 
    public void setCity(String city) {
        this.city = city;
    }
    
    
    public String getState() {
        return this.state;
    }
 
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return this.zipCode;
    }
 
    public void setZipCode(String zipcode) {
        this.zipCode = zipcode;
    }
    
}