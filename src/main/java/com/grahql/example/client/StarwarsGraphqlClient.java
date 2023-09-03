package com.grahql.example.client;

import com.grahql.example.client.response.FilmResponse;
import com.grahql.example.client.response.PlanetResponse;
import com.grahql.example.client.response.StarshipResponse;
import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.client.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class StarwarsGraphqlClient {

    private static final String QUERY = """
            query allPlanets {
              allPlanets {
                planets {
                  name
                  climates
                  terrains
                }
              }
            }
            query oneStarshipFixed {
              starship(id: "c3RhcnNoaXBzOjU=") {
                model
                name
                manufacturers
              }
            }
            query oneFilm($filmId: ID!) {
              film(filmID: $filmId) {
                title
                director
                releaseDate
              }
            }
            """;

    private static final String URL = "https://swapi-graphql.netlify.app/.netlify/functions/index";

    private RestTemplate restTemplate = new RestTemplate();

    private CustomGraphQLClient graphqlClient =
            GraphQLClient.createCustom(
                    URL, (url, headers, body) -> {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        headers.forEach(httpHeaders::addAll);
                        ResponseEntity<String> exchange = restTemplate.exchange(
                                url, HttpMethod.POST,
                                new HttpEntity<>(body, httpHeaders),
                                String.class
                        );

                        return new HttpResponse(exchange.getStatusCode().value(), exchange.getBody());
                    }
            );

    private GraphQLResponse getGraphqlResponse(
            String operationName, Map<String, ? extends Object> variablesMap,
            Map<String, List<String>> headersMap
    ) {
        return graphqlClient.executeQuery(
                QUERY,
                Optional.ofNullable(variablesMap).orElse(Collections.emptyMap()),
                operationName
        );
    }

    public String asJson(
            String operationName, Map<String, ? extends Object> variablesMap,
            Map<String, List<String>> headersMap
    ) {
        return getGraphqlResponse(operationName, variablesMap, headersMap).getJson();
    }

    public List<PlanetResponse> allPlanets() {
        return getGraphqlResponse("allPlanets", null, null)
                .extractValueAsObject("allPlanets.planets",
                        new TypeRef<List<PlanetResponse>>() {
                        });
    }

    public StarshipResponse oneStarshipFixed() {
        return getGraphqlResponse("oneStarshipFixed", null, null)
                .extractValueAsObject("starship", StarshipResponse.class);
    }

    public FilmResponse oneFilm(String filmId) {
        var variablesMap = Map.of("filmId", filmId);

        return getGraphqlResponse("oneFilm", variablesMap, null)
                .extractValueAsObject("film", FilmResponse.class);
    }

    public List<GraphQLError> oneFilmInvalid() {
        var variablesMap = Map.of("filmId", "xxxxxx");
        var graphqlResponse = getGraphqlResponse(
                "oneFilm", variablesMap, null
        );

        return graphqlResponse.hasErrors() ? graphqlResponse.getErrors() : null;
    }

}