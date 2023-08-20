package com.grahql.example.datasource.component.fake;

import com.grahql.example.datasource.faker.FakerStockDataSource;
import com.graphql.example.DgsConstants;
import com.graphql.example.types.Stock;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsSubscription;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@DgsComponent
public class FakerStockDataResolver {
    @Autowired
    FakerStockDataSource source;

//    @DgsSubscription
    @DgsData(parentType = DgsConstants.SUBSCRIPTION_TYPE, field = DgsConstants.SUBSCRIPTION.RandomStock)
    public Publisher<Stock> randomStock() {
        return Flux.interval(Duration.ofSeconds(0), Duration.ofSeconds(1)).map(t -> source.randomStock());
    }
}
