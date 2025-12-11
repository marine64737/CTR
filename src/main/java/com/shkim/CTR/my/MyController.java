package com.shkim.CTR.my;

import com.shkim.CTR.question.Question;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MyController {
    public static JdbcTemplate jdbcTemplate;

    MyController (JdbcTemplate jdbcTemplate){
        MyController.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/")
    public String home(Model model){
        //List<Question> my = jdbcTemplate.query("SELECT * FROM my where user = 1", (rs, rowNum) -> new Question(rs.getInt("id"), rs.getString("userId")));
        //model.addAttribute("my", my);
        return "home";
    }

    @GetMapping("/search")
    public String search(Model model){
        model.addAttribute("word", null);
        return "search";
    }

    @GetMapping("/searchcomplete")
    public String searchcomplete(@RequestParam(name = "word") String word, Model model){
        List<Question> num = jdbcTemplate.query("SELECT * FROM question WHERE number = ?",
                (rs, rowNum) -> new Question(null, rs.getString("number"), rs.getString("title")), word);
        List<Question> title = jdbcTemplate.query("SELECT * FROM question WHERE title = ?",
                (rs, rowNum) -> new Question(null, rs.getString("number"), rs.getString("title")), word);
        model.addAttribute("num", num);
        model.addAttribute("title", title);
        return "search";
    }
}
