package com.grahql.example.datasource.component.fake;

import com.grahql.example.datasource.faker.FakerBookData;
import com.grahql.example.datasource.faker.FakerHelloDataSource;
import com.graphql.example.DgsConstants;
import com.graphql.example.types.SmartSearchResult;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DgsComponent
public class FakerSmartSearchDataResolver {


    @DgsData( parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.SmartSearch)
    public List<SmartSearchResult> getSmartSearch(@InputArgument(name = "keyword") Optional<String> keyword) {
        List<SmartSearchResult> smartSearchResults = new ArrayList<>();

        if (keyword.isEmpty()) {
            smartSearchResults.addAll(FakerHelloDataSource.HELLO_LIST);
            smartSearchResults.addAll(FakerBookData.BOOK_LIST);
            System.out.println(smartSearchResults);
        } else {
            String keywordString = keyword.get();

            FakerBookData.BOOK_LIST.stream().filter(book -> StringUtils.containsIgnoreCase(book.getTitle(),keywordString))
                    .forEach(smartSearchResults::add);
            FakerHelloDataSource.HELLO_LIST.stream().filter(h -> StringUtils.containsIgnoreCase(h.getText(),keywordString))
                    .forEach(smartSearchResults::add);
        }

        return smartSearchResults;
    }
}
