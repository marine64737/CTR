package com.shkim.CTR.question;

import jakarta.validation.Valid;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class QuestionController {

    public static JdbcTemplate jdbcTemplate;

    public QuestionController(JdbcTemplate jdbcTemplate){
        QuestionController.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/")
    public String home(Model model){
        List<Question> questions = jdbcTemplate.query("SELECT * FROM question", (rs, rowNum) -> new Question(rs.getInt("id"), rs.getString("title")));
        model.addAttribute("questions", questions);
        return  "home";
    }

//    @GetMapping("/")
//    public String questionForm(Model model){
//        model.addAttribute("question", new Question());
//        return  "form";
//    }

    @PostMapping("/result")
    public String questionSubmit(@ModelAttribute Question question, Model model){
        jdbcTemplate.execute("INSERT INTO question (id, title) VALUES ("+question.getId()+", "+"'"+question.getTitle()+"'"+")");
        return "redirect:/";
    }

    @GetMapping("/add")
    public String questionAdd(Model model){
        model.addAttribute(new Question(1, null));
        return "form";
    }
}
