package com.ga.travel.cucumber;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@CucumberContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8081"
)
@Import(ScenarioState.class)
public class CucumberSpringConfiguration {
    @Before
    public void setUpRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
    }
}
