package com.shkim.CTR.my;

import com.shkim.CTR.problem.ProblemDTO;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class MyController {
    private static final Logger log = LoggerFactory.getLogger(MyController.class);

    @Autowired
    public QueryMethod queryMethod;

    public static JdbcTemplate jdbcTemplate;

    MyController (JdbcTemplate jdbcTemplate){
        MyController.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/login")
    public String login(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/"; // 메인 페이지로 강제 압송
        }
        return "login";
    }
    @GetMapping("/")
    public String home(@RequestParam(name = "key", required = false) String key,
                       @RequestParam(name = "value", required = false) String value, HttpServletRequest request, Model model, Principal principal){
        if (!ObjectUtils.isEmpty(key) && !ObjectUtils.isEmpty(value)) {
            request.getSession().setAttribute(key, value);
            log.info("Session Success");
        }
        List<Map<String, Object>> my_solved = queryMethod.problems(true, "bojproblem", principal);
        List<Map<String, Object>> my_not_solved = queryMethod.problems(false, "bojproblem", principal);
        int probNum = queryMethod.problemNum(principal.getName());
        model.addAttribute("count", probNum);
        model.addAttribute("my", my_solved);
        model.addAttribute("my1", my_not_solved);
        model.addAttribute("sessionAttributeNames", Collections.list(request.getSession().getAttributeNames()));
        return "index";
    }
    @GetMapping("/search")
    public String search(Model model){
        return "search";
    }

    @GetMapping("/searchcomplete")
    public String searchcomplete(@RequestParam(name = "word") String word, Model model, Principal principal){
        String sql = "%"+word+"%";
        String name = principal.getName();
        List<Map<String, Object>> problems = jdbcTemplate.queryForList("select p.problemid, p.titleKo, m.userid from problem as p left join (SELECT distinct problemId, userid FROM my WHERE userid\n" +
                        "= (SELECT id FROM user WHERE name = ? LIMIT 1)) as m on p.problemid = m.problemid where p.problemId LIKE ?\n" +
                        "or p.titleKo LIKE ?;",
                name, sql, sql);
        model.addAttribute("problems", problems);
        model.addAttribute("word", word);
        return "search";
    }

    @RequestMapping("/solve/{problemid}")
    public String solve(@PathVariable String problemid, Model model, Principal principal){
        int pid = Integer.parseInt(problemid);
        String name = principal.getName();
        List<Map<String, Object>> my = queryMethod.solveProblem(name, pid, "bojproblem");
        String title = jdbcTemplate.queryForObject("SELECT titleKo from problem where problemid = ?",
                (rs, rowNum) -> rs.getString("titleKo"), pid);
       Object status = jdbcTemplate.queryForObject(
               "SELECT status from my where userid = (select id from user where name = ?) and problemid = ? order by id desc limit 1",
               (rs, rowNum) -> rs.getInt("status"), name, pid);
        int i_status=0;
        if (status != null) i_status = Integer.parseInt(status.toString());
        String link = "https://www.acmicpc.net/problem/"+pid;
        model.addAttribute("problemtitle", title);
        model.addAttribute("my", my);
        model.addAttribute("pid", pid);
        model.addAttribute("name", name);
        model.addAttribute("link", link);
        model.addAttribute("status", i_status);
        return "solve";
    }
    @RequestMapping("/solve/{problemid}/{id}")
    public String detail(@PathVariable String problemid, @PathVariable String id, Model model, Principal principal){
        int pid = Integer.parseInt(problemid);
        int mid = Integer.parseInt(id);
        Map<String, Object> my = jdbcTemplate.queryForMap("SELECT m.id as id, m.problemid as pid, p.titleKo as title, " +
                        "m.code, m.memo, m.memory, m.time FROM my as m join user as u on m.userid=u.id join problem as p on m.problemid=p.problemid where m.id=?",
                mid);
        String link = "https://www.acmicpc.net/problem/"+pid;
        model.addAttribute("my", my);
        model.addAttribute("link", link);
        return "detail";
    }

    @PostMapping("/update")
    public String update(@RequestParam String id, @RequestParam String pid, @RequestParam String code, @RequestParam String memo,
                         @RequestParam String memory, @RequestParam String time, Model model, Principal principal){
        int mid = Integer.parseInt(id);
        int probid = Integer.parseInt(pid);
        jdbcTemplate.update("update my set code=?, memo=?, memory=?, time=? where id=?", code, memo, memory, time, mid);
        solve(pid, model, principal);
        return "redirect:/solve/"+pid;
    }

    @PostMapping("/solve/solveadd")
    public String solveadd(@RequestParam String pid, Model model, Principal principal){
        int probid = Integer.parseInt(pid);
        int uid = jdbcTemplate.queryForObject("select id from user where name=?", (rs, rowNum) -> rs.getInt("id"), principal.getName());
        jdbcTemplate.execute("insert into my(userid, problemid, status, nonvisible) values("+uid+", "+probid+", 0, 0)");
        solve(pid, model, principal);
        return "redirect:/solve/"+pid;
    }

    @PostMapping("/added")
    public String add(@RequestParam(name = "id") int id, Model model, Principal principal){
        Map<String, Object> my = jdbcTemplate.queryForMap("SELECT problemId, titleKo FROM problem WHERE problemId = ?", id);
        int uid = jdbcTemplate.queryForObject("SELECT id FROM user WHERE name = ?",(rs, rowNum) -> rs.getInt("id"), principal.getName());
        jdbcTemplate.execute("INSERT INTO my(userid, problemid, status, nonvisible) VALUES("+uid+", "+my.get("problemid")+ ", 0, 0)");
        return "redirect:/solve/"+id;
    }
}
