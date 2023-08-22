package com.grahql.example.service.query;

import com.grahql.example.datasource.problemz.entity.Solutionz;
import com.grahql.example.datasource.problemz.repository.SolutionzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolutionzQueryService {

    @Autowired
    private SolutionzRepository repository;

    public List<Solutionz> solutionzByKeyword(String keyword) {
        return repository.findByKeyword("%" + keyword + "%");
    }

}