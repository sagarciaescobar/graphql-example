package com.grahql.example.datasource.faker;

import com.graphql.example.types.*;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FakerStockDataSource {
    @Autowired
    private Faker faker;

    public Stock randomStock() {
        return Stock.newBuilder()
                .lastTradeDateTime(OffsetDateTime.now())
                .symbol(faker.stock().nyseSymbol())
                .price(faker.random().nextInt(100,1000))
                .build();
    }
}
