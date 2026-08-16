package com.shkim.CTR.Domain.My.Repository;

import com.shkim.CTR.Domain.My.DTO.SearchCompleteDTO;
import com.shkim.CTR.Domain.My.DTO.SolvedProblemsDTO;
import com.shkim.CTR.Domain.My.DTO.UnsolvedProblemsDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Repository
public class MyRepository {
    public static JdbcTemplate jdbcTemplate;
    MyRepository (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<SolvedProblemsDTO> solved_problems(Principal principal){
        String sql = """
                    SELECT m.problemid as pid, p.titleKo as title, m.status, p.level
                    FROM (SELECT id, userid, problemid, start_time, end_time, status FROM my WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1)
                    AND start_time IS NOT NULL ORDER BY id DESC LIMIT 100) AS m
                    JOIN user u ON m.userid = u.id JOIN problem p ON m.problemid = p.problemId where p.platform = u.platform order by end_time desc, start_time desc""";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new SolvedProblemsDTO(
                        rs.getInt("pid"),
                        rs.getString("title"),
                        rs.getInt("status"),
                        rs.getInt("level")
                ), principal.getName());
    }
    public List<UnsolvedProblemsDTO> unsolved_problems(Principal principal){
        String sql = """
                       SELECT m.id as id, m.problemid as pid, p.titleKo as title, m.status, m.nonvisible
                       FROM (SELECT id, userid, problemid, start_time, status, nonvisible FROM my
                       WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1)
                       AND start_time is null ORDER BY id DESC LIMIT 100) AS m
                       JOIN user u ON m.userid = u.id JOIN problem p ON m.problemid = p.problemId where p.platform = u.platform order by id desc""";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new SolvedProblemsDTO(
                        rs.getInt("pid"),
                        rs.getString("title"),
                        rs.getInt("status"),
                        rs.getInt("nonvisible")
                ), principal.getName());
    }
    public int totalNum(String name){
        String query = """
                SELECT count(distinct problemid) as count FROM my
                WHERE userid = (SELECT id FROM user WHERE name = ? LIMIT 1)
                AND start_time IS NOT NULL
                """;
        return jdbcTemplate.queryForObject(query, Integer.class, name);
    }
    public int problemNum(String name){
        String query = """
                SELECT count(distinct problemid) as count FROM my m JOIN user u ON u.id = m.userid
                JOIN problem p ON p.platform = u.platform
                WHERE m.userid = (SELECT id FROM user WHERE name = ? LIMIT 1)
                AND start_time IS NOT NULL
                """;
        return jdbcTemplate.queryForObject(query, Integer.class, name);
    }
    public List<SearchCompleteDTO> searchComplete(String word, String name){
        String sql = """
                select p.problemid as pid, p.titleKo as title, m.userid as uid from problem
                as p left join (SELECT distinct problemId, userid FROM my WHERE userid
                = (SELECT id FROM user WHERE name = ? LIMIT 1)) as m on p.problemid = m.problemid
                join user as u on p.platform = u.platform where p.problemId LIKE ?
                or p.titleKo LIKE ?
                """;
        return jdbcTemplate.query(sql, new Object[]{name, word, word}, (rs, rowNum) -> new SearchCompleteDTO(
                rs.getInt("pid"),
                rs.getString("title"),
                rs.getInt("uid")
        ));
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
