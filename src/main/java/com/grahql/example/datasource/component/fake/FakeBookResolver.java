package com.grahql.example.datasource.component.fake;

import com.grahql.example.datasource.faker.FakerBookData;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.Book;
import com.netflix.dgs.codegen.generated.types.ReleaseHistory;
import com.netflix.dgs.codegen.generated.types.ReleaseHistoryInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import io.micrometer.common.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@DgsComponent
public class FakeBookResolver {

    @DgsData(parentType = "Query", field="books")
    public List<Book> booksWrittenBy(@InputArgument(name = "author") Optional<String> authorName) {
        if (authorName.isEmpty() || StringUtils.isBlank(authorName.get())) {
            return FakerBookData.BOOK_LIST;
        }
        return FakerBookData.BOOK_LIST.stream()
                .filter(b -> b.getAuthor().getName().equals(authorName.get())).toList();
    }

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.BooksByRelease)
    public List<Book> getBooksByRelease(DataFetchingEnvironment dataFetchingEnvironment) {
        HashMap<String,Object> releaseMap = dataFetchingEnvironment.getArgument("releasedInput");
        ReleaseHistoryInput input = ReleaseHistoryInput.newBuilder()
                .printedEdition((Boolean) releaseMap.get(DgsConstants.RELEASEHISTORYINPUT.PrintedEdition))
                .year((Integer) releaseMap.get(DgsConstants.RELEASEHISTORYINPUT.Year))
                .build();
        return FakerBookData.BOOK_LIST.stream().filter(b -> this.matchReleaseHistory(input, b.getReleased())).toList();
    }

    private boolean matchReleaseHistory(ReleaseHistoryInput input, ReleaseHistory element) {
        return  input.getPrintedEdition().equals(element.getPrintedEdition()) && input.getYear().equals(element.getYear());
    }
}
