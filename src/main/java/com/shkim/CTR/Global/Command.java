package com.shkim.CTR.Global;

import com.shkim.CTR.CtrApplication;
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
        String base_url = "https://school.programmers.co.kr/learn/courses/30/lessons/";
        List<Integer> integers = IntStream.rangeClosed(0, 500000).boxed().toList();

        String targetClassName = "challenge-title";

        for (int integer : integers) {
            String url = base_url+integer;
            //System.out.println("=== 크롤링 중인 주소: " + url + " ===");

            try {
                Document doc = Jsoup.connect(url).get();

                Elements element = doc.select("." + targetClassName);
                String e = element.text();

                // 매칭된 요소들의 텍스트 출력 또는 데이터 수집
                if (!e.isEmpty()) {
                    jdbcTemplate.update("INSERT INTO prgproblem(problemid, titleKo) VALUES(?, ?)", integer, e);
                    log.info("["+integer+"]: " + e);
                }

            } catch (IOException e) {
                continue;
                //System.err.println("URL 연결 실패 (" + url + "): " + e.getMessage());
            } finally {
                if (integer % 1000 == 0) log.info("진행 중: "+integer);
            }
        }
        log.info("Completed");
    }
}
