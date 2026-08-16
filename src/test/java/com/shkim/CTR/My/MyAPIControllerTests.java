package com.shkim.CTR.My;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class MyAPIControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @DisplayName("ToggleTest")
    public void testToggle() throws Exception {
        mockMvc.perform(post("/toggle/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    @Transactional
    @DisplayName("TimelapsTest1")
    public void testTimelaps_1() throws Exception {
        mockMvc.perform(post("/solve/timelaps/1/0/0"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
    @Test
    @Transactional
    @DisplayName("TimelapsTest2")
    public void testTimelaps_2() throws Exception {
        mockMvc.perform(post("/solve/timelaps/1/0/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
    @Test
    @Transactional
    @DisplayName("TimelapsTest3")
    public void testTimelaps_3() throws Exception {
        mockMvc.perform(post("/solve/timelaps/1/1/0"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
    @Test
    @Transactional
    @DisplayName("TimelapsTest4")
    public void testTimelaps_4() throws Exception {
        mockMvc.perform(post("/solve/timelaps/1/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
    @Test
    @Transactional
    @DisplayName("SolveAddTest")
    public void testSolveAdd() throws Exception {
        mockMvc.perform(post("/solve/solveadd/tkdgh/1005"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
}
