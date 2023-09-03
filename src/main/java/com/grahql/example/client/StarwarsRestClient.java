package com.grahql.example.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grahql.example.client.request.GraphqlRestRequest;
import com.grahql.example.client.response.FilmResponse;
import com.grahql.example.client.response.GraphqlErrorResponse;
import com.grahql.example.client.response.PlanetResponse;
import com.grahql.example.client.response.StarshipResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class StarwarsRestClient {

    private static final String URL = "https://swapi-graphql.netlify.app/.netlify/functions/index";

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper();

    public String asJson(GraphqlRestRequest body, Map<String, List<String>> headersMap) {
        var requestHeaders = new HttpHeaders();

        if (headersMap != null) {
            headersMap.forEach(requestHeaders::addAll);
        }

        var responseEntity = restTemplate.postForEntity(URL,
                new HttpEntity(body, requestHeaders), String.class);

        return responseEntity.getBody();
    }

    public List<PlanetResponse> allPlanets() throws JsonProcessingException {
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

        var json = asJson(body, null);
        var jsonNode = objectMapper.readTree(json);
        var data = jsonNode.at("/data/allPlanets/planets");

        return objectMapper.readValue(data.toString(),
                new TypeReference<List<PlanetResponse>>() {
                });
    }

    public StarshipResponse oneStarshipFixed() throws JsonProcessingException {
        var query = """
                query oneStarshipFixed {
                  starship(id: "c3RhcnNoaXBzOjU=") {
                    model
                    name
                    manufacturers
                  }
                }
                """;

        var body = new GraphqlRestRequest();
        body.setQuery(query);

        var json = asJson(body, null);
        var jsonNode = objectMapper.readTree(json);
        var data = jsonNode.at("/data/starship");

        return objectMapper.readValue(data.toString(), StarshipResponse.class);
    }

    public FilmResponse oneFilm(String filmId) throws JsonProcessingException {
        var query = """
                query oneFilm($filmId: ID!) {
                  film(filmID: $filmId) {
                    title
                    director
                    releaseDate
                  }
                }
                """;

        var body = new GraphqlRestRequest();
        body.setQuery(query);

        var variablesMap = Map.of("filmId", filmId);
        body.setVariables(variablesMap);

        var json = asJson(body, null);
        var jsonNode = objectMapper.readTree(json);
        var data = jsonNode.at("/data/film");

        return objectMapper.readValue(data.toString(), FilmResponse.class);
    }

    public List<GraphqlErrorResponse> oneFilmInvalid() throws JsonProcessingException {
        var query = """
                query oneFilm($filmId: ID!) {
                  film(filmID: $filmId) {
                    title
                    director
                    releaseDate
                  }
                }
                """;

        var body = new GraphqlRestRequest();
        body.setQuery(query);

        var variablesMap = Map.of("filmId", "xxxxx");
        body.setVariables(variablesMap);

        var json = asJson(body, null);
        var jsonNode = objectMapper.readTree(json);
        var errors = jsonNode.at("/errors");

        if (errors != null) {
            return objectMapper.readValue(errors.toString(),
                    new TypeReference<List<GraphqlErrorResponse>>() {
                    });
        }

        return null;
    }

}