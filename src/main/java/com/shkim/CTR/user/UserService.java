package com.shkim.CTR.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    public static JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate){
        UserService.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT * FROM user WHERE name = ?",
                    (rs, rowNum) -> new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            "{bcrypt}"+rs.getString("password")
                    ),
                    username
            );

            return new CustomUserDetails(user);

        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("유저가 없습니다: " + username);
        }
    }

    public static class CustomUserDetails extends User implements UserDetails, Serializable {
//        @Serial
//        private static final long serialVersionUID = 1L;
        CustomUserDetails(User user){
            super(user.getId(), user.getName(), user.getPassword());
        }
        private static final List<GrantedAuthority> ROLE_USER = Collections
                .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER"));

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return ROLE_USER;
        }

        @Override
        public String getUsername() {
            return getName();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
