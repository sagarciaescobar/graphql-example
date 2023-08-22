package com.grahql.example.service.query;

import com.grahql.example.datasource.problemz.entity.Problemz;
import com.grahql.example.datasource.problemz.entity.Solutionz;
import com.grahql.example.datasource.problemz.repository.ProblemzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProblemzQueryService {

    @Autowired
    private ProblemzRepository repository;

    public List<Problemz> problemzLatestList() {
        List<Problemz> problemz = repository.findAllByOrderByCreationTimestampDesc();

        problemz.forEach(
                p -> p.getSolutions().sort(Comparator.comparing(Solutionz::getCreationTimestamp).reversed()));

        return problemz;
    }

    public Optional<Problemz> problemzDetail(UUID problemzId) {
        return repository.findById(problemzId);
    }

    public List<Problemz> problemzByKeyword(String keyword) {
        return repository.findByKeyword("%" + keyword + "%");
    }
}