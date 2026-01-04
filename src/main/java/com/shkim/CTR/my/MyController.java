package com.shkim.CTR.my;

import com.shkim.CTR.question.ProblemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class MyController {
    private static final Logger log = LoggerFactory.getLogger(MyController.class);

    public static JdbcTemplate jdbcTemplate;

    MyController (JdbcTemplate jdbcTemplate){
        MyController.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
//    @PostMapping("/logout")
//    public String logout(Model model){
//        model.addAttribute("logout", true);
//        return "login";
//    }
    @GetMapping("/home")
    public String home(Model model, Principal principal){
        List<Map<String, Object>> my = jdbcTemplate.queryForList("SELECT m.problemid as pid, p.titleKo as title, count(*) as count FROM my as m join problem as p on m.problemid = p.problemid join user as u on m.userid = u.id where u.name = ? and status = 2 group by m.problemid order by m.problemid asc",
                principal.getName());
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

    @RequestMapping("/solve/{problemid}")
    public String solve(@PathVariable String problemid, Model model, Principal principal){
        int pid = Integer.parseInt(problemid);
        String name = principal.getName();
        List<Map<String, Object>> my = jdbcTemplate.queryForList("SELECT m.id as id, m.problemid as pid, " +
                        "DATE_FORMAT(m.start_time, '%Y-%m-%d %H:%i:%s') as st, DATE_FORMAT(m.end_time, '%Y-%m-%d %H:%i:%s') as end," +
                        "TIMESTAMPDIFF(MINUTE, start_time, end_time) as duration, " +
                        "m.status FROM my as m join user as u on m.userid=u.id where u.name = ? and m.problemid = ? order by m.id asc",
                name, pid);
        String title = jdbcTemplate.queryForObject("SELECT titleKo from problem where problemid = ?",
                (rs, rowNum) -> rs.getString("titleKo"), problemid);
        model.addAttribute("myProblem", problemid+". "+title);
        model.addAttribute("my", my);
        model.addAttribute("pid", pid);
        model.addAttribute("name", name);
        return "solve";
    }

    @RequestMapping("/solve/{problemid}/{id}")
    public String detail(@PathVariable String problemid, @PathVariable String id, Model model, Principal principal){
        int pid = Integer.parseInt(problemid);
        int mid = Integer.parseInt(id);
        Map<String, Object> my = jdbcTemplate.queryForMap("SELECT m.id as id, m.problemid as pid, p.titleKo, m.start_time, m.end_time, m.status,m.code, m.memo FROM my as m join user as u on m.userid=u.id join problem as p on m.problemid=p.problemid where m.id=?",
                mid);
        model.addAttribute("my", my);
        return "detail";
    }

    @PostMapping("/update")
    public String update(@RequestParam String id, @RequestParam String pid, @RequestParam String code, @RequestParam String memo, Model model, Principal principal){
        int mid = Integer.parseInt(id);
        int probid = Integer.parseInt(pid);
        jdbcTemplate.execute("update my set code='"+code+"', memo='"+memo+"' where id="+mid);
        solve(pid, model, principal);
        return "redirect:/solve/"+pid;
    }
//    @PostMapping("/solve/add")
//    public String update(@RequestParam String pid, Model model, Principal principal){
//        int id = Integer.parseInt(pid);
//        int uid = jdbcTemplate.queryForObject("select id from user where name=?", (rs, rowNum) -> rs.getInt("id"), principal.getName());
//        jdbcTemplate.execute("insert into my(userid, problemid, status) values("+uid+", "+id+", 0)");
//        solve(pid, model, principal);
//        return "redirect:/solve/"+pid;
//    }

    @PostMapping("/solve/timelap")
    public String timelap(@RequestParam String id, @RequestParam String pid, @RequestParam String status, @RequestParam(defaultValue = "true") boolean complete,
                          Model model, Principal principal){
        int probid = Integer.parseInt(pid);
        int mid = Integer.parseInt(id);
        int uid = jdbcTemplate.queryForObject("select id from user where name=?", (rs, rowNum) -> rs.getInt("id"), principal.getName());
        int mstatus = Integer.parseInt(status);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);
        if (mstatus == 0) jdbcTemplate.execute("update my set start_time=now() + INTERVAL 9 HOUR, status=1 where id="+mid);
        else if (mstatus == 1) {
            if (complete) jdbcTemplate.execute("update my set end_time=now() + INTERVAL 9 HOUR, status=2 where id="+mid);
            else jdbcTemplate.execute("update my set end_time=now() + INTERVAL 9 HOUR, status=3 where id="+mid);
            jdbcTemplate.execute("insert into my(userid, problemid, status) values("+uid+", "+probid+", 0)");
        }
        solve(pid, model, principal);
        return "redirect:/solve/"+pid;
    }

    @PostMapping("/added")
    public String add(@RequestParam(name = "id") int id, Model model, Principal principal){
        Map<String, Object> my = jdbcTemplate.queryForMap("SELECT problemId, titleKo FROM problem WHERE problemId = ?", id);
        int uid = jdbcTemplate.queryForObject("SELECT id FROM user WHERE name = ?",(rs, rowNum) -> rs.getInt("id"), principal.getName());
        jdbcTemplate.execute("INSERT INTO my(userid, problemid, status) VALUES("+uid+", "+my.get("problemid")+ ", 0)");
        home(model, principal);
        return "redirect:/solve/"+id;
    }
    @PostMapping("/time")
    public String time(Model model){
        Date now = new Date();
        String nowTime = now.toString();
        jdbcTemplate.execute("INSERT INTO temp(today_time) VALUES('"+nowTime+"')");
        return "redirect:/";
    }
}
