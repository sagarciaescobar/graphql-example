package com.grahql.example.component.problemz;

import com.grahql.example.service.query.ProblemzQueryService;
import com.grahql.example.service.query.SolutionzQueryService;
import com.grahql.example.util.GraphqlBeanMapper;
import com.graphql.example.DgsConstants;
import com.graphql.example.types.Problem;
import com.graphql.example.types.SearchableItem;
import com.graphql.example.types.SearchableItemFilter;
import com.graphql.example.types.Solution;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class ItemSearchDataResolver {

    @Autowired
    private ProblemzQueryService problemzQueryService;

    @Autowired
    private SolutionzQueryService solutionzQueryService;

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.ItemSearch)
    public List<SearchableItem> searchItems(
            @InputArgument(name = "filter") SearchableItemFilter filter) {
        String keyword = filter.getKeyword();

        List<Problem> problemzByKeyword = problemzQueryService.problemzByKeyword(keyword)
                .stream().map(GraphqlBeanMapper::mapToGraphql).toList();
        List<SearchableItem> result = new ArrayList<>(problemzByKeyword);

        List<Solution> solutionzByKeyword = solutionzQueryService.solutionzByKeyword(keyword)
                .stream().map(GraphqlBeanMapper::mapToGraphql).toList();
        result.addAll(solutionzByKeyword);

        if (result.isEmpty()) {
            throw new DgsEntityNotFoundException("No item with keyword " + keyword);
        }

        result.sort(Comparator.comparing(SearchableItem::getCreateDateTime).reversed());

        return result;
    }

}