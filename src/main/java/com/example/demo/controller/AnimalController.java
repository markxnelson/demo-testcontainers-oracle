package com.example.demo.controller;

import com.example.demo.model.Animal;
import com.example.demo.repository.AnimalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AnimalController {

    AnimalRepository animalRepository;

    AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @GetMapping("/animals")
    public ResponseEntity<List<Animal>> getAnimals() {
        return ResponseEntity.ok(animalRepository.findAll());
    }
}
