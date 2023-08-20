package com.grahql.example.datasource.faker;

import com.graphql.example.types.Address;
import com.graphql.example.types.Author;
import com.graphql.example.types.MobileApp;

import com.graphql.example.types.MobileAppCategory;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
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

            MobileApp mobileApp = null;
            try {
                mobileApp = MobileApp.newBuilder()
                        .name(faker.app().name())
                        .version(faker.app().version())
                        .author(author)
                        .appId(UUID.randomUUID().toString())
                        .platform(randomMobileAppPlatform())
                        .releaseDate(LocalDate.now().minusDays(faker.random().nextInt(365)))
                        .downloaded(faker.number().numberBetween(1,1_500_000))
                        .homepage(new URL("https://" + faker.internet().url()))
                        .category(MobileAppCategory.values()[faker.random().nextInt(MobileAppCategory.values().length)])
                        .build();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            MOBILE_APP_LIST.add(mobileApp);
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
