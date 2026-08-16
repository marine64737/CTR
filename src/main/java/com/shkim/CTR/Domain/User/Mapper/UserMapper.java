package com.shkim.CTR.Domain.User.Mapper;

import com.shkim.CTR.Domain.User.DTO.UserDTO;
import com.shkim.CTR.Domain.User.Entity.User;
import com.shkim.CTR.Domain.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserMapper {
    public static JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository userRepository;

    UserMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public User toEntity(UserDTO userDTO) {
        String password = userRepository.getPassword(userDTO.getName());
        return new User(userDTO.getId(), userDTO.getName(), password, userDTO.getPlatform());
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getPlatform());
    }
}
