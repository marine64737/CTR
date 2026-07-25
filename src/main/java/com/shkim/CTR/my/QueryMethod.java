package com.shkim.CTR.my;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Service
public class QueryMethod {

    public static JdbcTemplate jdbcTemplate;

    QueryMethod(JdbcTemplate jdbcTemplate){
        QueryMethod.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> problems(boolean solved, String platform, Principal principal){
        if (solved) {
            String sql = """
                            SELECT m.problemid as pid, p.titleKo as title, m.status, p.level
                            FROM (SELECT id, userid, problemid, start_time, end_time, status FROM my WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1)
                            AND start_time IS NOT NULL ORDER BY id DESC LIMIT 100) AS m
                            JOIN user u ON m.userid = u.id JOIN " + platform + " p ON m.problemid = p.problemId order by end_time desc, start_time desc
                    """;
            return jdbcTemplate.queryForList(sql, principal.getName());
        }
        else {
            String sql = """
                           SELECT m.id as id, m.problemid as pid, p.titleKo as title, m.status, m.nonvisible
                           FROM (SELECT id, userid, problemid, start_time, status, nonvisible FROM my
                           WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1)
                           AND start_time is null ORDER BY id DESC LIMIT 100) AS m
                           JOIN user u ON m.userid = u.id JOIN " + platform + " p ON m.problemid = p.problemId order by id desc
                    """;
            return jdbcTemplate.queryForList(sql, principal.getName());
        }
    }
    public int problemNum(String name){
        String query = """
                SELECT count(distinct problemid) as count FROM my
                                        WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1)" +
                                        AND start_time IS NOT NULL
                """;
        Object o = jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> rs.getInt("count"), name);
        try {
            return Integer.parseInt(o.toString());
        }
        catch (Exception e){
            return 0;
        }
    }
    public List<Map<String, Object>> solveProblem(String name, int problemId, String platform){
        return jdbcTemplate.queryForList("SELECT m.id as id, m.problemid as pid, " +
                        "DATE_FORMAT(m.start_time, '%Y-%m-%d %H:%i:%s') as st, DATE_FORMAT(m.end_time, '%Y-%m-%d %H:%i:%s') as end, "+
                        "TIMESTAMPDIFF(MINUTE, start_time, end_time) as duration, " +
                        "TIMESTAMPDIFF(HOUR, start_time, end_time) as hour, m.status, m.memory, m.time "+
                        "FROM (" +
                        "    SELECT id, userid, problemid, start_time, end_time, status, memory, time " +
                        "    FROM my " +
                        "    WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1) " +
                        "    and problemid = ? " +
                        ") AS m " +
                        "JOIN user u ON m.userid = u.id JOIN "+ platform +" p on m.problemid = p.problemid ORDER BY start_time IS NULL DESC, start_time DESC",
                name, problemId);
    }
}
