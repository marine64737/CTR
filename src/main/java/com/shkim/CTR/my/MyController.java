package com.shkim.CTR.my;

import com.shkim.CTR.question.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class MyController {
    private static final Logger log = LoggerFactory.getLogger(MyController.class);

    public static JdbcTemplate jdbcTemplate;

    MyController (JdbcTemplate jdbcTemplate){
        MyController.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/")
    public String home(Model model){
        List<Integer> my = jdbcTemplate.query("SELECT questionid FROM my where userid = 1", (rs, rowNum) -> rs.getInt("questionid"));
        List<Question> questions = new ArrayList<>();
        for (int i=my.size()-1; i >= 0; i--) questions.addAll(jdbcTemplate.query("SELECT * FROM question where id = ?",
                (rs, rowNum) -> new Question(
                        rs.getInt("id"),
                        rs.getString("number"),
                        rs.getString("title"),
                        "https://www.acmicpc.net/problem/"+rs.getString("number")
                ), my.get(i)));
        List<String> times = jdbcTemplate.query("SELECT today_time FROM temp",
                (rs, rowNum) -> rs.getString("today_time"));
        model.addAttribute("my", questions);
        model.addAttribute("times", times);
        return "home";
    }

    @GetMapping("/search")
    public String search(Model model){
        model.addAttribute("word", null);
        return "search";
    }

    @GetMapping("/searchcomplete")
    public String searchcomplete(@RequestParam(name = "word") String word, Model model){
        String sql = "%"+word+"%";
        List<Question> questions = jdbcTemplate.query("SELECT * FROM question WHERE number LIKE ? OR title LIKE ?",
                (rs, rowNum) -> new Question(rs.getInt("id"), rs.getString("number"), rs.getString("title"), null), sql, sql);
        model.addAttribute("questions", questions);
        return "search";
    }

    @PostMapping("/added")
    public String add(@RequestParam(name = "id") int id, Model model){
        List<Question> questionList = jdbcTemplate.query("SELECT * FROM question WHERE id = ?",
                (rs, rowNum) -> new Question(rs.getInt("id"), rs.getString("number"), rs.getString("title"), null), id);
        for (Question question : questionList) {
            jdbcTemplate.execute("INSERT INTO my(userid, questionid) VALUES(1, " + question.getId() + ")");
        }
        home(model);
        return "redirect:/";
    }
    @PostMapping("/time")
    public String time(Model model){
        Date now = new Date();
        String nowTime = now.toString();
        jdbcTemplate.execute("INSERT INTO temp(today_time) VALUES('"+nowTime+"')");
        return "redirect:/";
    }
}
