package com.shkim.CTR.question;

import com.shkim.CTR.CtrApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

//        jdbcTemplate.execute("ALTER TABLE my DROP foreign key my_ibfk_1");
//        jdbcTemplate.execute("ALTER TABLE my DROP foreign key my_ibfk_2");
//        jdbcTemplate.execute("DROP TABLE IF EXISTS problem");
//        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS problem("+
//                "problemId int NOT NULL KEY, " +
//                "titleKo VARCHAR(255)," +
//                "isSolvable boolean," +
//                "isPartial boolean," +
//                "acceptedUserCount int," +
//                "level int," +
//                "votedUserCount int," +
//                "sprout boolean," +
//                "givesNoRating boolean," +
//                "isLevelLocked boolean," +
//                "averageTries double," +
//                "official boolean)");
//        int list_num = 350;
//        for (int t=0; t<list_num; t++){
//            //List<Problem> apis = webClientService.get(t);
//            List<Object[]> apis = webClientService.get(t).stream().map(problem -> new Object[]{
//                    problem.problemId(),
//                    problem.titleKo(),
//                    problem.isSolvable(),
//                    problem.isPartial(),
//                    problem.acceptedUserCount(),
//                    problem.level(),
//                    problem.votedUserCount(),
//                    problem.sprout(),
//                    problem.givesNoRating(),
//                    problem.isLevelLocked(),
//                    problem.averageTries(),
//                    problem.official()}).toList();
//            jdbcTemplate.batchUpdate("INSERT INTO problem VALUES (?,?,?,?,?,?,?,?,?,?,?,?)", apis);
//            log.info("inserting data: t="+(t+1)+"/"+list_num+")");
//        }
//
//        List<Object[]> user = new ArrayList<>();
//        for (int i = 0; i < 100 ; i++) {
//            user.add(new Object[]{i+1});
//        }
//
//
//        jdbcTemplate.execute("DROP TABLE IF EXISTS my");
//

//        jdbcTemplate.execute("DROP TABLE IF EXISTS user");
//        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS user(id int not null AUTO_INCREMENT, name VARCHAR(255), password VARCHAR(255), PRIMARY KEY(id))");
//
//        String[] names = {
//                "Perez","Adams","Collins","Morgan","Ramirez","Price","Morales","Flores","Ramos","Parker","Myers",
//                "Allen","Edwards","Jones","Gonzalez","Cruz","Hill","Kim","Rodriguez","Castillo","Reed","Jimenez",
//                "Moore","Nguyen","Martinez","Richardson","Watson","Cox","King","Green","Ruiz","James","Murphy","Turner",
//                "Peterson","Baker","Anderson","Scott","Ross","Phillips","Bailey","Garcia","Hernandez","Evans","Morris",
//                "Harris","Ward","Clark","Patel","Brown","Lee","Wood","Howard","Walker","Ortiz","Sanders","Taylor",
//                "Nelson","Robinson","Long","Martin","Smith","Alvarez","Cook","Rogers","Johnson","Williams","Lewis",
//                "Lopez","Foster","Carter","Hall","Gray","Davis","White","Hughes","Brooks","Sanchez","Thompson",
//                "Wright","Miller","Mendoza","Bennett","Chavez","Reyes","Kelly",	"Torres","Mitchell","Thomas",
//                "Gutierrez","Rivera","Gomez","Cooper","Young","Campbell","Stewart","Diaz","Roberts","Wilson"
//
//        };
//        List<Object[]> list = new ArrayList<>();
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String password = passwordEncoder.encode("1");
//
//
//        for (int i=0; i< names.length; i++){
//           list.add(new Object[]{names[i], password});
//        }
//
//
//        jdbcTemplate.batchUpdate("INSERT INTO user(name, password) VALUE (?, ?)", list);
//
//        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS my("+
//                "id int NOT NULL AUTO_INCREMENT," +
//                "userid int not null," +
//                "problemid int not null," +
//                "start_time datetime, " +
//                "end_time datetime, " +
//                "status int not null, " +
//                "primary key(id))");
//                "foreign key(userid) references user(id), " +
//                "foreign key(problemid) references problem(problemid))");
//
//                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS my("+
//                "id int NOT NULL AUTO_INCREMENT," +
//                "userid int not null," +
//                "problemid int not null," +
//                "primary key(id),"+
//                "foreign key(userid) references user(id), " +
//                "foreign key(problemid) references problem(problemid))");
//        List<Object[]> arr = new ArrayList<>();
//        List<Integer> problemNum = jdbcTemplate.queryForList("select problemid from problem", Integer.class);
//        for(int i=0; i<10000; i++){
//            Collections.shuffle(problemNum);
//            List<Integer> problemShuffled = problemNum.subList(0, 10);
//            Random ran = new Random();
//            List<Integer> intsList = ran.ints(10, 1, 100)
//                    .boxed().toList();
//            for (int j=0; j<9; j++){
//                int u = intsList.get(j);
//                int p = problemShuffled.get(j);
//                arr.add(new Object[]{u,p,0});
//            }
//            if (i%10==9){
//                jdbcTemplate.batchUpdate("INSERT INTO my(userid, problemid, status) VALUES (?,?,?)", arr);
//                arr = new ArrayList<>();
//            }
//        }
//
//        jdbcTemplate.execute("alter table my add foreign key(userid) references user(id)");
//        jdbcTemplate.execute("alter table my add foreign key(problemid) references problem(problemid)");
//
//        // Use JdbcTemplate's batchUpdate operation to bulk load data
//        //jdbcTemplate.batchUpdate("INSERT INTO my(userid, problemid) VALUES (?,?)", arr);
//
//        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS temp("+
//                    "id int NOT NULL AUTO_INCREMENT KEY, today_time VARCHAR(255))");
//
////        log.info("Created tables");
////        String[] arr = {"A+B", "A-B", "터렛", "피보나치 함수", "어린 왕자", "ACM Craft", "습격자 초라기", "벡터 매칭", "A/B",
////                "분산처리", "다리 놓기", "Fly me to the Alpha Centauri", "유기농 배추", "Contact"};
////        for (int i=0; i<arr.length; i++){
////            jdbcTemplate.execute("INSERT INTO question (number, title, url) values ('"+(1000+i)+"', '"+arr[i]+"',"+
////                    "'https://www.acmicpc.net/problem/"+(1000+i)+"')");
////        }
//
        log.info("Created tables");
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
