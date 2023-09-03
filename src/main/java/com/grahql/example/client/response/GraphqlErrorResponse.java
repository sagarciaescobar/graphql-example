package com.grahql.example.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphqlErrorResponse {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {
        private int line;
        private int column;
    }

    private String message;
    private List<String> path;
    private List<Location> locations;
}