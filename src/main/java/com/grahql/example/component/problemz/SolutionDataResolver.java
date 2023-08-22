package com.grahql.example.component.problemz;

import com.grahql.example.exception.ProblemzAuthenticationException;
import com.grahql.example.datasource.problemz.entity.Solutionz;
import com.grahql.example.service.command.SolutionzCommandService;
import com.grahql.example.service.query.ProblemzQueryService;
import com.grahql.example.service.query.UserzQueryService;
import com.grahql.example.util.GraphqlBeanMapper;
import com.graphql.example.DgsConstants;
import com.graphql.example.types.Solution;
import com.graphql.example.types.SolutionCreateInput;
import com.graphql.example.types.SolutionResponse;
import com.graphql.example.types.SolutionVoteInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.UUID;

@DgsComponent
public class SolutionDataResolver {

    @Autowired
    private UserzQueryService userzQueryService;

    @Autowired
    private ProblemzQueryService problemzQueryService;

    @Autowired
    private SolutionzCommandService solutionzCommandService;

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.SolutionCreation)
    public SolutionResponse createSolution(
            @RequestHeader(name = "authToken", required = true) String authToken,
            @InputArgument(name = "solution") SolutionCreateInput solutionCreateInput
    ) {
        var userz = userzQueryService.findUserzByAuthToken(authToken)
                .orElseThrow(ProblemzAuthenticationException::new);
        var problemzId = UUID.fromString(solutionCreateInput.getProblemId());
        var problemz = problemzQueryService.problemzDetail(problemzId)
                .orElseThrow(DgsEntityNotFoundException::new);
        var solutionz = GraphqlBeanMapper.mapToEntity(solutionCreateInput, userz, problemz);
        var created = solutionzCommandService.createSolutionz(solutionz);

        return SolutionResponse.newBuilder().solution(GraphqlBeanMapper.mapToGraphql(created)).build();
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.SolutionVoteCreation)
    public SolutionResponse createSolutionVote(
            @RequestHeader(name = "authToken", required = true) String authToken,
            @InputArgument(name = "vote") SolutionVoteInput solutionVoteInput
    ) {
        Optional<Solutionz> updated;
        userzQueryService.findUserzByAuthToken(authToken).orElseThrow(ProblemzAuthenticationException::new);

        if (solutionVoteInput.getVoteAsGood()) {
            updated = solutionzCommandService.voteGood(
                    UUID.fromString(solutionVoteInput.getSolutionId())
            );
        } else {
            updated = solutionzCommandService.voteBad(
                    UUID.fromString(solutionVoteInput.getSolutionId())
            );
        }

        if (updated.isEmpty()) {
            throw new DgsEntityNotFoundException("Solution "
                    + solutionVoteInput.getSolutionId() + " not found");
        }

        return SolutionResponse.newBuilder().solution(GraphqlBeanMapper.mapToGraphql(updated.get())).build();
    }

    @DgsData(parentType = DgsConstants.SUBSCRIPTION_TYPE,
            field = DgsConstants.SUBSCRIPTION.SolutionVoteChange)
    public Flux<Solution> subscribeSolutionVote(@InputArgument(name = "solutionId") String solutionId) {
        return solutionzCommandService.solutionzFlux().map(GraphqlBeanMapper::mapToGraphql);
    }

}