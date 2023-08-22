package com.grahql.example.component.problemz;

import com.grahql.example.datasource.problemz.entity.Userz;
import com.grahql.example.datasource.problemz.entity.UserzToken;
import com.grahql.example.service.command.UserzCommandService;
import com.grahql.example.service.query.UserzQueryService;
import com.grahql.example.util.GraphqlBeanMapper;
import com.graphql.example.DgsConstants;
import com.graphql.example.types.*;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestHeader;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;

import java.util.Optional;

@DgsComponent
public class UserDataResolver {

    @Autowired
    private UserzCommandService userzCommandService;

    @Autowired
    private UserzQueryService userzQueryService;

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.Me)
    public User accountInfo(@RequestHeader(name = "authToken", required = true) String authToken) {
        Userz userz = userzQueryService.findUserzByAuthToken(authToken)
                .orElseThrow(DgsEntityNotFoundException::new);

        return GraphqlBeanMapper.mapToGraphql(userz);
    }

    @Secured("ROLE_ADMIN")
    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.UserCreate)
    public UserResponse createUser(@InputArgument(name = "user") UserCreateInput userCreateInput) {
//        var userAuth = userzQueryService.findUserzByAuthToken(authToken)
//                .orElseThrow(ProblemzAuthenticationException::new);
//
//        if (!StringUtils.equals(userAuth.getUserRole(), "ROLE_ADMIN")) {
//            throw new ProblemzPermissionException();
//        }

        Userz userz = GraphqlBeanMapper.mapToEntity(userCreateInput);
        Userz saved = userzCommandService.createUserz(userz);
        UserResponse userResponse = UserResponse.newBuilder().user(
                GraphqlBeanMapper.mapToGraphql(saved)).build();

        return userResponse;
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.UserLogin)
    public UserResponse userLogin(@InputArgument(name = "user") UserLoginInput userLoginInput) {
        UserzToken generatedToken = userzCommandService.login(userLoginInput.getUsername(),
                userLoginInput.getPassword());
        UserAuthToken userAuthToken = GraphqlBeanMapper.mapToGraphql(generatedToken);
        User userInfo = accountInfo(userAuthToken.getAuthToken());
        UserResponse userResponse = UserResponse.newBuilder().authToken(userAuthToken)
                .user(userInfo).build();

        return userResponse;
    }

    @Secured("ROLE_ADMIN")
    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.UserActivation)
    public UserActivationResponse userActivation(
            @InputArgument(name = "user") UserActivationInput userActivationInput) {
//        var userAuth = userzQueryService.findUserzByAuthToken(authToken)
//                .orElseThrow(ProblemzAuthenticationException::new);
//
//        if (!StringUtils.equals(userAuth.getUserRole(), "ROLE_ADMIN")) {
//            throw new ProblemzPermissionException();
//        }

        Userz updated = userzCommandService.activateUser(
                userActivationInput.getUsername(), userActivationInput.getActive()
        ).orElseThrow(DgsEntityNotFoundException::new);
        UserActivationResponse userActivationResponse = UserActivationResponse.newBuilder()
                .isActive(updated.isActive()).build();

        return userActivationResponse;
    }

}