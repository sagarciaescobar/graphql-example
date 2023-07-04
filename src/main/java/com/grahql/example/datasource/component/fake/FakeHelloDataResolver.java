package com.grahql.example.datasource.component.fake;

import com.grahql.example.datasource.faker.FakerHelloDataSource;
import com.netflix.dgs.codegen.generated.types.Hello;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@DgsComponent
public class FakeHelloDataResolver {

     @DgsQuery
    public List<Hello> allHellos() {
        return FakerHelloDataSource.HELLO_LIST;
    }

    @DgsQuery
    public Hello oneHello() {
        return FakerHelloDataSource.HELLO_LIST.get(ThreadLocalRandom.current().nextInt(FakerHelloDataSource.HELLO_LIST.size()));
    }
}
