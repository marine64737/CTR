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

    @GetMapping("/list")
    public String list(Model model){
        List<Question> questions = jdbcTemplate.query("SELECT * FROM question",
                (rs, rowNum) -> new Question(rs.getInt("id"), rs.getString("number"), rs.getString("title")));
        model.addAttribute("questions", questions);
        return  "list";
    }

    @PostMapping("/result")
    public String questionSubmit(@ModelAttribute Question question){
        jdbcTemplate.execute("INSERT INTO question (number, title) VALUES ('"+question.getNum()+"', '"+question.getTitle()+"')");
        return "redirect:/list";
    }

    @GetMapping("/add")
    public String questionAdd(Model model){
        model.addAttribute(new Question(null, null, null));
        return "form";
    }

}
