//package com.shkim.CTR.question;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.util.List;
//
//@Controller
//public class ProblemController {
//
//    public static JdbcTemplate jdbcTemplate;
//
//    public ProblemController(JdbcTemplate jdbcTemplate){
//        ProblemController.jdbcTemplate = jdbcTemplate;
//    }
//
//    @GetMapping("/list")
//    public String list(Model model){
//        List<ProblemDTO> questions = jdbcTemplate.query("SELECT * FROM problem",
//                (rs, rowNum) -> new ProblemDTO(
//                        rs.getInt("problemId"),
//                        rs.getString("titleKo"),
//                        "https://www.acmicpc.net/problem/"+rs.getInt("problemId")));
//        model.addAttribute("questions", questions);
//        return  "list";
//    }
//
//    @PostMapping("/result")
//    public String questionSubmit(@ModelAttribute ProblemDTO problemDTO){
//        jdbcTemplate.execute("INSERT INTO problem (problemId, titleKo) VALUES ('"+problemDTO.getId()+"', '"+problemDTO.getTitle()+"')");
//        return "redirect:/list";
//    }
//
////    @GetMapping("/add")
////    public String questionAdd(Model model){
////        model.addAttribute(new ProblemDTO(null, null, null));
////        return "form";
////    }
//
//}
