package com.grahql.example.datasource.faker;

import com.graphql.example.types.*;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FakerPetDataSource {
    @Autowired
    private Faker faker;

    public static final List<Pet> PET_LIST = new ArrayList<>();

    @PostConstruct
    private void postConstruct() {
        for (int i= 0;i<20; i++) {
            Pet animal = switch (i % 2) {
                case 0:
                    yield Dog.newBuilder().name(faker.dog().name())
                            .food(PetFoodType.OMNIVORE)
                            .breed(faker.dog().breed())
                            .size(faker.dog().size())
                            .coatLength(faker.dog().coatLength())
                            .build();
                default:
                    yield Cat.newBuilder().name(faker.cat().name())
                            .food(PetFoodType.CARNIVORE)
                            .breed(faker.cat().breed())
                            .registry(faker.cat().registry())
                            .build();
            };
            PET_LIST.add(animal);
        }
    }
}
