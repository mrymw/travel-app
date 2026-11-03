package com.ga.travel.cucumber;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class ActivitySteps {
    private final ScenarioState state;
    public ActivitySteps(ScenarioState state) {
        this.state = state;
    }
    private String activitiesUrl() {
        return "/api/trips/" + state.getTripId() + "/activities";
    }
    @When("I add the activity {string} on day {int} at {string}")
    public void iAddTheActivity(String name, int day, String time) {
        String body = """
            { "name": "%s","day": %d,"location": "City centre","time": "%s","cost": 0,"notes": "","booked": false }"""
                .formatted(name, day, time);
        Response response = state.request()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(activitiesUrl());
        state.setResponse(response);
        if (response.statusCode() == 200) {
            state.setActivityId(response.jsonPath().getLong("id"));
        }
    }
    @When("I ask for the trip's activities")
    public void iAskForTheTripsActivities() {
        state.setResponse(state.request().when().get(activitiesUrl()));
    }
    // The data table in the feature file arrives here as a List<String>
    @Then("the activities are in this order:")
    public void theActivitiesAreInThisOrder(List<String> expectedNames) {
        List<String> actualNames =
                state.getResponse().jsonPath().getList("name");
        assertEquals(expectedNames, actualNames);
    }
    @When("I mark the activity as booked")
    public void iMarkTheActivityAsBooked() {
        String url = activitiesUrl() + "/" + state.getActivityId();
        JsonPath activity = state.request().when().get(url).jsonPath();
        String body = """
            { "name": "%s", "day": %s, "time": "%s",
              "location": "%s", "cost": %s, "notes": "%s", "booked": true }
            """.formatted(
                activity.getString("name"),      // 1st %s  → name
                activity.getInt("day"),          // 2nd %s  → day
                activity.getString("time"),      // 3rd %s  → time
                activity.getString("location"),  // 4th %s  → location
                activity.getDouble("cost"),      // 5th %s  → cost
                activity.getString("notes"));    // 6th %s  → notes
        state.setResponse(state.request()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(url));
    }
    @Then("the activity is booked")
    public void theActivityIsBooked() {
        String url = activitiesUrl() + "/" + state.getActivityId();
        assertTrue(state.request().when().get(url).jsonPath().getBoolean("booked"));
    }
}