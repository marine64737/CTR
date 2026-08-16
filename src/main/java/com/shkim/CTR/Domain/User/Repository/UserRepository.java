package com.shkim.CTR.Domain.User.Repository;

import com.shkim.CTR.Domain.User.DTO.UserDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public static JdbcTemplate jdbcTemplate;
    UserRepository(JdbcTemplate jdbcTemplate) {
        UserRepository.jdbcTemplate = jdbcTemplate;
    }

    public UserDTO user(String name){
        return jdbcTemplate.queryForObject("SELECT * FROM user WHERE name = ?",
                (rs, rowNum) -> new UserDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("platform")
                ),
                name);
    }

    public String getPassword(String name) {
        return jdbcTemplate.queryForObject("SELECT password FROM user WHERE name = ?", String.class, name);
    }
}
