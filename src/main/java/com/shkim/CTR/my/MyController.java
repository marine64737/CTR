package com.shkim.CTR.my;

import com.shkim.CTR.question.Problem;
import com.shkim.CTR.question.ProblemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MyController {
    private static final Logger log = LoggerFactory.getLogger(MyController.class);

    public static JdbcTemplate jdbcTemplate;

    MyController (JdbcTemplate jdbcTemplate){
        MyController.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("username", null);
        model.addAttribute("password", null);
        return "login";
    }
//    @PostMapping("/logout")
//    public String logout(Model model){
//        model.addAttribute("logout", true);
//        return "login";
//    }
    @GetMapping("/home")
    public String home(Model model, Principal principal){
        List<ProblemDTO> my = jdbcTemplate.query("SELECT m.problemid, p.titleKo FROM my as m join problem as p on m.problemid = p.problemid where userid = 1 group by m.problemid order by m.problemid asc", (rs, rowNum) ->
            new ProblemDTO(rs.getInt("problemid"), rs.getString("titleKo"), "https://www.acmicpc.net/problem/"+rs.getInt("problemid")));
        //List<ProblemDTO> questions = new ArrayList<>();
//        for (int i=my.size()-1; i >= 0; i--) questions.addAll(jdbcTemplate.query("SELECT problemId, titleKo FROM problem where problemId = ?",
//                (rs, rowNum) -> new ProblemDTO(
//                        rs.getInt("problemId"),
//                        rs.getString("titleKo"),
//                        "https://www.acmicpc.net/problem/"+rs.getInt("problemId")
//                ), my.get(i)));
        List<String> times = jdbcTemplate.query("SELECT today_time FROM temp",
                (rs, rowNum) -> rs.getString("today_time"));
        model.addAttribute("my", my);
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
        List<ProblemDTO> questions = jdbcTemplate.query("SELECT problemId, titleKo FROM problem WHERE problemId LIKE ? OR titleKo LIKE ?",
                (rs, rowNum) -> new ProblemDTO(rs.getInt("problemId"), rs.getString("titleKo"), null), sql, sql);
        model.addAttribute("questions", questions);
        model.addAttribute("word", word);
        return "search";
    }

    @PostMapping("/added")
    public String add(@RequestParam(name = "id") int id, Model model, Principal principal){
        ProblemDTO problem = jdbcTemplate.queryForObject("SELECT problemId, titleKo FROM problem WHERE problemId = ?",
                (rs, rowNum) -> new ProblemDTO(rs.getInt("problemId"), rs.getString("titleKo"), "https://www.acmicpc.net/problem/"+rs.getInt("problemId")), id);
        jdbcTemplate.execute("INSERT INTO my(userid, problemid) VALUES(1, " + problem.getId()+ ")");
        home(model, principal);
        return "redirect:/home";
    }
    @PostMapping("/time")
    public String time(Model model){
        Date now = new Date();
        String nowTime = now.toString();
        jdbcTemplate.execute("INSERT INTO temp(today_time) VALUES('"+nowTime+"')");
        return "redirect:/";
    }
}
