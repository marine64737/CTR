package com.shkim.CTR.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        List<User> user = jdbcTemplate.query("select * from user where username = ?", (rs, rowNum) ->
                new User(rs.getInt("id"), rs.getString("username"), rs.getString("password")), username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("Username "+username+" is not found.");
        }
        return new CustomUserDetails(user.get(0));
    }

    static class CustomUserDetails extends User implements UserDetails{
        CustomUserDetails(User user){
            super(user.getId(), user.getUsername(), user.getPassword());
        }
        private static final List<GrantedAuthority> ROLE_USER = Collections
                .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER"));

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return ROLE_USER;
        }

        @Override
        public String getUsername() {
            return getUsername();
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
