package com.foodfinder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:original/application.properties")
public class SanityTest {

    @Test
    public void contextLoads() throws Exception {
    }

}