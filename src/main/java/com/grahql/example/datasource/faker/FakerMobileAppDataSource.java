package com.grahql.example.datasource.faker;

import com.graphql.example.types.Address;
import com.graphql.example.types.Author;
import com.graphql.example.types.MobileApp;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Configuration

public class FakerMobileAppDataSource {

    @Autowired
    private Faker faker;

    public static final List<MobileApp> MOBILE_APP_LIST = new ArrayList<>();

    @PostConstruct
    private void postConstruct() {
        for (int i = 0; i < 20; i++) {
            List<Address> addresses = new ArrayList<>();
            Author author = Author.newBuilder().addresses(addresses)
                    .name(faker.book().author())
                    .originCountry(faker.country().name())
                    .build();

            MobileApp mobileApp = MobileApp.newBuilder()
                    .name(faker.app().name())
                    .version(faker.app().version())
                    .author(author)
                    .appId(UUID.randomUUID().toString())
                    .platform(randomMobileAppPlatform())
                    .build();

            MOBILE_APP_LIST.add(mobileApp);
            System.out.println(MOBILE_APP_LIST);
        }
    }

    private List<String> randomMobileAppPlatform() {
        return switch (ThreadLocalRandom.current().nextInt(10) % 3) {
            case 0 -> List.of("android");
            case 1 -> List.of("ios");
            default -> List.of("ios", "android");
        };
    }
}
