package com.shkim.CTR.Domain.My.Service;

import com.shkim.CTR.Domain.My.DTO.*;
import com.shkim.CTR.Domain.My.Repository.MyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class MyService {
    public static JdbcTemplate jdbcTemplate;
    MyService(JdbcTemplate jdbcTemplate){
        MyService.jdbcTemplate = jdbcTemplate;
    }
    @Autowired
    public MyRepository myRepository;

    public List<SolvedProblemsDTO> solved_problems(Principal principal){
        return myRepository.solved_problems(principal);
    }
    public List<UnsolvedProblemsDTO> unsolved_problems(Principal principal){
        return myRepository.unsolved_problems(principal);
    }
    public int totalNum(String name){
        return myRepository.totalNum(name);
    }
    public int problemNum(String name) {
        return myRepository.problemNum(name);
    }
    public List<SearchCompleteDTO> searchComplete(String word, String name){
        return myRepository.searchComplete(word, name);
    }
    public List<SolveProblemDTO> solveProblem(Principal principal, int problemId){
        return myRepository.solveProblem(principal.getName(), problemId);
    }
    public DetailDTO detail(int problemId){
        return myRepository.detail(problemId);
    }
}
