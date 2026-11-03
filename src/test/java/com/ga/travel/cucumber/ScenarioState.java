package com.ga.travel.cucumber;

import io.cucumber.spring.ScenarioScope;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;

@ScenarioScope
@Getter
@Setter
public class ScenarioState {
    private String token; // JWT of the logged-in user
    private Response response; // the last repose we got back
    private Long tripId; // the trip created in this scenario
    private Long activityId; // the activity created in this scenario

    public RequestSpecification request() {
        return RestAssured.given().header
                ("Authorization", "Bearer " + token);
    }
}
