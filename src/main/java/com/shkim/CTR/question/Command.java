package com.shkim.CTR.question;

import com.shkim.CTR.CtrApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Command implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CtrApplication.class);

    public static JdbcTemplate jdbcTemplate;

    @Autowired
    public WebClientServiceImpl webClientService;

    public Command(JdbcTemplate jdbcTemplate){
        Command.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void run(String... strings) {
        log.info("Creating tables");
        //jdbcTemplate.execute("DROP TABLE IF EXISTS question");
        Integer num = jdbcTemplate.queryForObject("SELECT EXISTS (SELECT * from question)", (rs, rowNum) -> rs.getInt(1));
        if (num == 0){
            jdbcTemplate.execute("CREATE TABLE question("+
                    "id int NOT NULL AUTO_INCREMENT KEY, number VARCHAR(255), title VARCHAR(255), level int)");
            for (int t=0; t<340; t++){
                //List<Problem> apis = webClientService.get(t);
                List<Object[]> apis = webClientService.getObject(t);
                jdbcTemplate.batchUpdate("INSERT INTO question(number, title, level) VALUES (?,?,?)", apis);
                log.info("inserting data: t="+(t+1)+"/340");
            }
        }

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS my("+
                "id int NOT NULL AUTO_INCREMENT KEY, userid int, questionid int)" +
                "IF NOT EXISTS my");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS temp("+
                    "id int NOT NULL AUTO_INCREMENT KEY, today_time VARCHAR(255))");

        log.info("Created tables");
//        String[] arr = {"A+B", "A-B", "터렛", "피보나치 함수", "어린 왕자", "ACM Craft", "습격자 초라기", "벡터 매칭", "A/B",
//                "분산처리", "다리 놓기", "Fly me to the Alpha Centauri", "유기농 배추", "Contact"};
//        for (int i=0; i<arr.length; i++){
//            jdbcTemplate.execute("INSERT INTO question (number, title, url) values ('"+(1000+i)+"', '"+arr[i]+"',"+
//                    "'https://www.acmicpc.net/problem/"+(1000+i)+"')");
//        }

//        log.info("Creating tables");
//
//        jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
//        jdbcTemplate.execute("CREATE TABLE customers(" +
//                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");
//
//        // Split up the array of whole names into an array of first/last names
//        List<Object[]> splitUpNames = Stream.of("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long")
//                .map(name -> name.split(" "))
//                .collect(Collectors.toList());
//
//        // Use a Java 8 stream to print out each tuple of the list
//        splitUpNames.forEach(name -> log.info("Inserting customer record for {} {}", name[0], name[1]));
//
//        // Use JdbcTemplate's batchUpdate operation to bulk load data
//        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);
//
//        log.info("Querying for customer records where first_name = 'Josh':");
//        jdbcTemplate.query(
//                        "SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
//                        (rs, rowNum) ->
//                                new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")),
//                        "Josh")
//                .forEach(customer -> log.info(customer.toString()));
    }
}
