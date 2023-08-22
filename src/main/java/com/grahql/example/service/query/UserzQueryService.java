package com.grahql.example.service.query;

import com.grahql.example.datasource.problemz.entity.Userz;
import com.grahql.example.datasource.problemz.repository.UserzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserzQueryService {

    @Autowired
    private UserzRepository userzRepository;

    public Optional<Userz> findUserzByAuthToken(String authToken) {
        return userzRepository.findUserByToken(authToken);
    }

}