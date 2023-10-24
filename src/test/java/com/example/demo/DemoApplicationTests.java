package com.example.demo;

import com.example.demo.model.Animal;
import com.example.demo.repository.AnimalRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Value(value = "${local.server.port}")
    private int port;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Container
    @ServiceConnection
    static OracleContainer oracleContainer = new OracleContainer(
            DockerImageName.parse("gvenzl/oracle-free:23.3-faststart")
                    .asCompatibleSubstituteFor("gvenzl/oracle-xe"))
                .withDatabaseName("pdb1")
                .withUsername("testuser")
                .withPassword(("testpwd"));

    @Autowired
    AnimalRepository animalRepository;

    @Test
    void testGetAnimals() {
        // create the test data
        List<Animal> animals = List.of(
                new Animal(null, "cat"),
                new Animal(null, "dog")
        );
        animalRepository.saveAll(animals);

        // check the api
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object[]> result = restTemplate.getForEntity(
                "http://127.0.0.1:" + port + "/animals", Object[].class
        );
        Object[] objects = result.getBody();

        List<String> actualAnimals = Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, Animal.class))
                .map(Animal::getName)
                .collect(Collectors.toList());

        assertTrue(actualAnimals.containsAll(Arrays.asList("cat", "dog")),
                "The expected animals should be present");
    }

}
