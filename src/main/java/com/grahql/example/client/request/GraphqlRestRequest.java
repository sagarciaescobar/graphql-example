package com.grahql.example.client.request;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GraphqlRestRequest {

    private String query;

    private Map<String,? extends Object> variables;
}
