package com.shkim.CTR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Command implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CtrApplication.class);

    public static JdbcTemplate jdbcTemplate;

    public Command(JdbcTemplate jdbcTemplate){
        Command.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void run(String... strings) {
        log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE IF EXISTS question");
        jdbcTemplate.execute("CREATE TABLE question("+
                "id int AUTO_INCREMENT, number VARCHAR(255), title VARCHAR(255))");
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
        jdbcTemplate.execute("CREATE TABLE users("+
                "id int AUTO_INCREMENT, userid VARCHAR(255))");
        log.info("Created tables");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1000', 'A+B')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1001', 'A-B')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1002', '터렛')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1003', '피보나치 함수')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1004', '어린 왕자')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1005', 'ACM Craft')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1006', '습격자 초라기')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1007', '벡터 매칭')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1008', 'A/B')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1009', '분산처리')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1010', '다리 놓기')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1011', 'Fly me to the Alpha Centauri')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1012', '유기농 배추')");
        jdbcTemplate.execute("INSERT INTO question (number, title) values ('1013', 'Contact')");

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
