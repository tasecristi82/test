package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DemoController.class)
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHomeEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello from Simple Spring Boot App!"))
                .andExpect(jsonPath("$.status").value("running"));
    }

    @Test
    void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/hello").param("name", "Jenkins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Jenkins!"))
                .andExpect(jsonPath("$.service").value("Simple Spring Boot App"));
    }

    @Test
    void testInfoEndpoint() throws Exception {
        mockMvc.perform(get("/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application").value("Simple Spring Boot App"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }
}
