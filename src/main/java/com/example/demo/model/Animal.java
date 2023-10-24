package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;

    public Animal(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Animal() {}

    public String getName() {
        return name;
    }
}
