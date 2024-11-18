package com.example.demo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long ID;
    private Long UserID;
    private Long Value;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getUserID() {
        return UserID;
    }

    public void setUserID(Long userID) {
        UserID = userID;
    }

    public Long getValue() {
        return Value;
    }

    public void setValue(Long value) {
        Value = value;
    }


    public  Accounts(){

    }

    public Accounts(Long ID, Long userID, Long value) {
        this.ID = ID;
        UserID = userID;
        Value = value;
    }
}
