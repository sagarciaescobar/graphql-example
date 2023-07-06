package com.grahql.example;

import com.graphql.example.client.BooksByReleaseGraphQLQuery;
import com.graphql.example.client.BooksGraphQLQuery;
import com.graphql.example.client.BooksProjectionRoot;
import com.jayway.jsonpath.TypeRef;
import com.netflix.dgs.codegen.generated.types.Author;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.graphql.example.types.ReleaseHistoryInput;
import net.datafaker.Faker;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static graphql.Assert.assertFalse;
import static graphql.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest

public class TestFakerBookDatResolver {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    Faker faker;

    @Test
    void testAllBooks() {
        BooksGraphQLQuery query = new BooksGraphQLQuery.Builder().build();
        BooksProjectionRoot root = new BooksProjectionRoot().title()
                .author().name().originCountry()
                .getRoot().released().year().getRoot();
        @Language("GraphQL") String req = new GraphQLQueryRequest(query,root).serialize();

        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(req,"data.books[*].title");

        assertNotNull(titles);
        assertFalse(titles.isEmpty());

        List<Author> authors = dgsQueryExecutor.executeAndExtractJsonPathAsObject(req,"data.books[*].author", new TypeRef<>() {
        });

        assertNotNull(authors);
        assertEquals(titles.size(),authors.size());

        List<Integer> releases = dgsQueryExecutor.executeAndExtractJsonPathAsObject(req, "data.books[*].released.year", new TypeRef<>() {
        });

        assertNotNull(releases);

        assertEquals(titles.size(), releases.size());
    }

    @Test
    void testBookWithInput() {
        Integer expectedYear = faker.number().numberBetween(2019,2021);
        Boolean expectedPrintedEdition = faker.bool().bool();

        ReleaseHistoryInput input = ReleaseHistoryInput.newBuilder()
                .printedEdition(expectedPrintedEdition)
                .year(expectedYear).build();
        BooksByReleaseGraphQLQuery query = BooksByReleaseGraphQLQuery.newRequest()
                .releasedInput(input).build();
        BooksProjectionRoot root = new BooksProjectionRoot()
                .released().year().printedEdition().getRoot();

        @Language("GraphQL") String req = new GraphQLQueryRequest(query,root).serialize();

        List<Integer> releasedYears = dgsQueryExecutor.executeAndExtractJsonPathAsObject(req,
                "data.booksByRelease[*].released.year", new TypeRef<>() {
                });

        Set<Integer> uniqueReleaseYears = new HashSet<>(releasedYears);

        assertNotNull(uniqueReleaseYears);
        assertTrue(uniqueReleaseYears.size() <= 1);
        if (!uniqueReleaseYears.isEmpty()) assertTrue(uniqueReleaseYears.contains(expectedYear));

        List<Boolean> printedEditions = dgsQueryExecutor.executeAndExtractJsonPathAsObject(req,
                "data.booksByRelease[*].released.printedEdition", new TypeRef<>() {
                });

        Set<Boolean> uniqueReleasedPrintedEdition = new HashSet<>(printedEditions);

        assertNotNull(uniqueReleasedPrintedEdition);
        assertTrue(uniqueReleasedPrintedEdition.size() <= 1);
        if (!uniqueReleasedPrintedEdition.isEmpty()) assertTrue(uniqueReleasedPrintedEdition.contains(expectedPrintedEdition));

    }
}
