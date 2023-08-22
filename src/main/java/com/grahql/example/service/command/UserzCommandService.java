package com.grahql.example.service.command;


import com.grahql.example.datasource.problemz.entity.Userz;
import com.grahql.example.datasource.problemz.entity.UserzToken;
import com.grahql.example.datasource.problemz.repository.UserzRepository;
import com.grahql.example.datasource.problemz.repository.UserzTokenRepository;
import com.grahql.example.util.HashUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserzCommandService {

    @Autowired
    private UserzRepository userzRepository;

    @Autowired
    private UserzTokenRepository userzTokenRepository;

    public UserzToken login(String username, String password) {
        Optional<Userz> userzQueryResult = userzRepository.findByUsernameIgnoreCase(username);

        if (userzQueryResult.isEmpty() ||
                !HashUtil.isBcryptMatch(password, userzQueryResult.get().getHashedPassword())) {
            throw new IllegalArgumentException("Invalid credential");
        }

        String randomAuthToken = RandomStringUtils.randomAlphanumeric(40);

        return refreshToken(userzQueryResult.get().getId(), randomAuthToken);
    }

    private UserzToken refreshToken(UUID userId, String authToken) {
        UserzToken userzToken = new UserzToken();
        userzToken.setUserId(userId);
        userzToken.setAuthToken(authToken);

        LocalDateTime now = LocalDateTime.now();
        userzToken.setCreationTimestamp(now);
        userzToken.setExpiryTimestamp(now.plusHours(2));

        return userzTokenRepository.save(userzToken);
    }

    public Userz createUserz(Userz userz) {
        return userzRepository.save(userz);
    }

    public Optional<Userz> activateUser(String username, boolean isActive) {
        userzRepository.activateUser(username, isActive);

        return userzRepository.findByUsernameIgnoreCase(username);
    }

}