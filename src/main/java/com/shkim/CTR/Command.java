package com.shkim.CTR;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Command implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CtrApplication.class);

    public static JdbcTemplate jdbcTemplate;
    Command(JdbcTemplate jdbcTemplate){
        Command.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void run(String... strings) throws IOException {
        log.info("Command Started");
        String boj_base_url = "https://www.acmicpc.net/problem/";
        String prg_base_url = "https://school.programmers.co.kr/learn/courses/30/lessons/";
        for (int i=1; i<=40000; i++) {
            jdbcTemplate.update("update problem set url = ? where problemid = ? and platform = 0", boj_base_url+i, i);
            jdbcTemplate.update("update problem set url = ? where problemid = ? and platform = 1", prg_base_url+i, i);
        }
        log.info("Completed");
    }
}
