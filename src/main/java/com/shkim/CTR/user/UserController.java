package com.shkim.CTR.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public static JdbcTemplate jdbcTemplate;

    UserController(JdbcTemplate jdbcTemplate){
        UserController.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpComplete(@RequestParam String username, @RequestParam String password, Model model){

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        String sql = "INSERT INTO user (name, password) " +
                "SELECT ?, ? FROM DUAL " +
                "WHERE NOT EXISTS (SELECT 1 FROM user WHERE name = ?)";

        int result = jdbcTemplate.update(sql, username, encodedPassword, username);

        if (result == 0) {
            log.info("Sign up Failed! (Duplicate Name)");
            return "redirect:/signup?error";
        } else {
            log.info("Sign up Success!");
            return "redirect:/login";
        }
    }


//        List<User> users = jdbcTemplate.query("SELECT * from user where name = ?",
//                (rs, rowNum) ->
//                        new User(rs.getInt("id"),
//                                rs.getString("name"),
//                                rs.getString("password")), username);
//        if (!users.isEmpty()) {
//            log.info("Sign up Failed!");
//            return "redirect:/signup?error";
//        }
//        else {
//            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            String encodedPassword = passwordEncoder.encode(password);
//            jdbcTemplate.execute("INSERT INTO user(name, password) values('"+username+"', '"+encodedPassword+"')");
//            log.info("Sign up Success!");
//            return "redirect:/login";
//        }
}
