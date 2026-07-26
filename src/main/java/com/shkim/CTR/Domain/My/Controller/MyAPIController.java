package com.shkim.CTR.Domain.My.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyAPIController {
    private static final Logger log = LoggerFactory.getLogger(MyAPIController.class);

    public static JdbcTemplate jdbcTemplate;

    MyAPIController (JdbcTemplate jdbcTemplate){
        MyAPIController.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/toggle/{id}")
    public static ResponseEntity<String> toggle(@PathVariable Long id) {
        String sql = "UPDATE my SET nonvisible = NOT nonvisible WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.ok("success");
    }
    @PostMapping("/solve/timelaps/{id}/{status}/{success}")
    public ResponseEntity<String> timelaps(@PathVariable String id, @PathVariable String status, @PathVariable String success){
        int mid = Integer.parseInt(id);
        int mstatus = Integer.parseInt(status);
        int msuccess = Integer.parseInt(success);
        if (mstatus == 0) jdbcTemplate.execute("update my set start_time=now() + INTERVAL 9 HOUR, status=1 where id="+mid);
        else if (mstatus == 1) {
            if (msuccess == 1) jdbcTemplate.execute("update my set end_time=now() + INTERVAL 9 HOUR, status=2 where id="+mid);
            else jdbcTemplate.execute("update my set end_time=now() + INTERVAL 9 HOUR, status=3 where id="+mid);
        }
        return ResponseEntity.ok("success");
    }
    @PostMapping("/solve/solveadd/{currentUserName}/{pid}")
    public ResponseEntity<String> solveadd(@PathVariable String currentUserName, @PathVariable String pid){
        int uid = jdbcTemplate.queryForObject("select id from user where name=?", (rs, rowNum) -> rs.getInt("id"), currentUserName);
        int probid = Integer.parseInt(pid);
        jdbcTemplate.execute("insert into my(userid, problemid, status, nonvisible) values("+uid+", "+probid+", 0, 0)");
        return ResponseEntity.ok("success");
    }
}
