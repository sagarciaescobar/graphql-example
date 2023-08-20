package com.grahql.example.datasource.component.fake;

import com.grahql.example.datasource.faker.FakerHelloDataSource;
import com.graphql.example.DgsConstants;
import com.graphql.example.types.Hello;
import com.graphql.example.types.HelloInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;

@DgsComponent
public class FakeHelloMutation {

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.AddHello)
    public int addHello(@InputArgument(name = "helloInput")HelloInput input) {
        Hello newHello = Hello.newBuilder().text(input.getText()).randomInt(input.getNumber()).build();
        FakerHelloDataSource.HELLO_LIST.add(newHello);
        return FakerHelloDataSource.HELLO_LIST.size();
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.ReplaceHelloText)
    public List<Hello> replaceHelloText(@InputArgument(name = "helloInput") HelloInput input) {
        FakerHelloDataSource.HELLO_LIST.stream().filter(h -> h.getRandomInt().equals(input.getNumber()))
                .forEach(h -> h.setText(input.getText()));
        return FakerHelloDataSource.HELLO_LIST;
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.DeleteHello)
    public int deleteHello(@InputArgument("number") Integer number) {
        FakerHelloDataSource.HELLO_LIST.removeIf(h -> h.getRandomInt().equals(number));
        return FakerHelloDataSource.HELLO_LIST.size();
    }
}
