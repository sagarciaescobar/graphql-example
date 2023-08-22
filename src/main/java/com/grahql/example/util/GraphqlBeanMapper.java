package com.grahql.example.util;

import com.grahql.example.datasource.problemz.entity.Problemz;
import com.grahql.example.datasource.problemz.entity.Solutionz;
import com.grahql.example.datasource.problemz.entity.Userz;
import com.grahql.example.datasource.problemz.entity.UserzToken;
import com.graphql.example.types.*;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class GraphqlBeanMapper {

    private static final PrettyTime PRETTY_TIME = new PrettyTime();

    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(7);

    public static User mapToGraphql(Userz original) {
        User result = new User();
        OffsetDateTime createDateTime = original.getCreationTimestamp().atOffset(ZONE_OFFSET);

        result.setAvatar(original.getAvatar());
        result.setCreatedDateTime(createDateTime);
        result.setDisplayName(original.getDisplayName());
        result.setEmail(original.getEmail());
        result.setId(original.getId().toString());
        result.setUsername(original.getUsername());

        return result;
    }

    public static Problem mapToGraphql(Problemz original) {
        Problem result = new Problem();
        OffsetDateTime createDateTime = original.getCreationTimestamp().atOffset(ZONE_OFFSET);
        User author = mapToGraphql(original.getCreatedBy());
        List<Solution> convertedSolutions = original.getSolutions().stream()
                .sorted(Comparator.comparing(Solutionz::getCreationTimestamp).reversed())
                .map(GraphqlBeanMapper::mapToGraphql)
                .collect(Collectors.toList());
        List<String> tagList = List.of(original.getTags().split(","));

        result.setAuthor(author);
        result.setContent(original.getContent());
        result.setCreateDateTime(createDateTime);
        result.setId(original.getId().toString());
        result.setSolutions(convertedSolutions);
        result.setTags(tagList);
        result.setTitle(original.getTitle());
        result.setSolutionCount(convertedSolutions.size());
        result.setPrettyCreateDateTime(PRETTY_TIME.format(createDateTime));

        return result;
    }

    public static Solution mapToGraphql(Solutionz original) {
        Solution result = new Solution();
        OffsetDateTime createDateTime = original.getCreationTimestamp().atOffset(ZONE_OFFSET);
        User author = mapToGraphql(original.getCreatedBy());
        SolutionCategory category = StringUtils.equalsIgnoreCase(
                original.getCategory(), SolutionCategory.EXPLANATION.toString()) ?
                SolutionCategory.EXPLANATION : SolutionCategory.REFERENCE;

        result.setAuthor(author);
        result.setCategory(category);
        result.setContent(original.getContent());
        result.setCreateDateTime(createDateTime);
        result.setId(original.getId().toString());
        result.setVoteAsBadCount(original.getVoteBadCount());
        result.setVoteAsGoodCount(original.getVoteGoodCount());
        result.setPrettyCreateDateTime(PRETTY_TIME.format(createDateTime));

        return result;
    }

    public static UserAuthToken mapToGraphql(UserzToken original) {
        UserAuthToken result = new UserAuthToken();
        OffsetDateTime expiryDateTime = original.getExpiryTimestamp().atOffset(ZONE_OFFSET);

        result.setAuthToken(original.getAuthToken());
        result.setExpiryTime(expiryDateTime);

        return result;
    }

    public static Problemz mapToEntity(ProblemCreateInput original, Userz author) {
        Problemz result = new Problemz();

        result.setContent(original.getContent());
        result.setCreatedBy(author);
        result.setId(UUID.randomUUID());
        result.setSolutions(Collections.emptyList());
        result.setTags(String.join(",", original.getTags()));
        result.setTitle(original.getTitle());

        return result;
    }

    public static Solutionz mapToEntity(SolutionCreateInput original, Userz author, Problemz problemz) {
        Solutionz result = new Solutionz();

        result.setCategory(original.getCategory().name());
        result.setContent(original.getContent());
        result.setCreatedBy(author);
        result.setId(UUID.randomUUID());
        result.setProblemz(problemz);

        return result;
    }

    public static Userz mapToEntity(UserCreateInput original) {
        Userz result = new Userz();

        result.setId(UUID.randomUUID());
        result.setHashedPassword(HashUtil.hashBcrypt(original.getPassword()));
        result.setUsername(original.getUsername());
        result.setEmail(original.getEmail());
        result.setDisplayName(original.getDisplayName());
        result.setAvatar(original.getAvatar());
        result.setActive(true);

        return result;
    }

}
