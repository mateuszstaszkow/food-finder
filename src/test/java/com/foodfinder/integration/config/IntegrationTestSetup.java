package com.foodfinder.integration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@TestPropertySource(locations="classpath:original/application.properties")
public class IntegrationTestSetup {

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;
    protected ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
        this.mapper = new ObjectMapper();
    }
}
