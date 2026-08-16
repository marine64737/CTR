package com.shkim.CTR.My;

import com.shkim.CTR.Domain.My.Repository.MyRepository;
import com.shkim.CTR.Domain.User.Mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MyServiceTests {
    @Mock
    private UserMapper userMapper;
    @Mock
    private MyRepository myRepository;

    @Test
    void solved_problems() {
        // 테스트 코드 작성
    }
}
