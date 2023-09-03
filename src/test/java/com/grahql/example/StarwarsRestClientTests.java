package com.grahql.example;


import com.grahql.example.client.StarwarsRestClient;
import com.grahql.example.client.request.GraphqlRestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static graphql.Assert.assertFalse;
import static graphql.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class StarwarsRestClientTests {

    @Autowired
    private StarwarsRestClient client;

    @Test
    void testAsJson() throws Exception {
        var query = """
                query allPlanets {
                  allPlanets {
                    planets {
                      name
                      climates
                      terrains
                    }
                  }
                }
                """;

        var body = new GraphqlRestRequest();
        body.setQuery(query);

        var result = client.asJson(body, null);

        assertNotNull(result);
    }

    @Test
    void testAsJson_Invalid() throws Exception {
        var query = """
                query allPlanets {
                  allPlanetsxxxxx {
                    planets {
                      name
                      climates
                      terrains
                    }
                  }
                }
                """;

        var body = new GraphqlRestRequest();
        body.setQuery(query);

        assertThrows(
                RuntimeException.class,
                () -> {
                    var result = client.asJson(body, null);
                }
        );
    }

    @Test
    void testAllPlanets() throws Exception {
        var result = client.allPlanets();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testOneStarshipFixed() throws Exception {
        var result = client.oneStarshipFixed();
        assertNotNull(result);
        assertNotNull(result.getModel());
        assertNotNull(result.getName());
        assertNotNull(result.getManufacturers());
    }

    @Test
    void testOneFilm_Right() throws Exception {
        var result = client.oneFilm("1");
        assertNotNull(result);
        assertNotNull(result.getTitle());
    }

    @Test
    void testOneFilm_Invalid() throws Exception {
        var errors = client.oneFilmInvalid();
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
    }

}