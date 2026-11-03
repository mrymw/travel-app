package com.ga.travel.cucumber;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class TripSteps {
    private final ScenarioState state;
    public TripSteps(ScenarioState state) {
        this.state = state;
    }
    private String tripUrl() {
        return "/api/trips/" + state.getTripId();
    }
    // ---------- READ ----------
    @When("I ask for my trips")
    public void iAskForMyTrips() {
        state.setResponse(state.request().when().get("/api/trips"));
    }
    @Then("I see a trip called {string}")
    public void iSeeATripCalled(String name) {
        List<String> names = state.getResponse().jsonPath().getList("name");
        assertTrue(names.contains(name),
                "Trips found: " + names);
    }
    @Then("I do not see a trip called {string}")
    public void iDoNotSeeATripCalled(String name) {
        List<String> names = state.getResponse().jsonPath().getList("name");
        assertFalse(names.contains(name),
                "Trips found: " + names);
    }
    // ---------- CREATE (form-data, like Postman) ----------
    @When("I create a trip called {string} to {string} from {string} to {string}")
            public void iCreateATrip(String name, String destination, String
            startDate, String endDate) {
        Response response = state.request()
                .multiPart("name"
                        , name)
                .multiPart("destination"
                        , destination)
                .multiPart("description"
                        ,
                        "Created by a Cucumber test")
                .multiPart("startDate"
                        , startDate)
                .multiPart("endDate"
                        , endDate)
                .when()
                .post("/api/trips");
        state.setResponse(response);
        if (response.statusCode() == 200) {
            state.setTripId(response.jsonPath().getLong("id")); // remember
        }
    }
    @Given("I have a trip called {string} to {string} from {string} to {string}")
            public void iHaveATrip(String name, String destination, String startDate, String endDate) {
        iCreateATrip(name, destination, startDate, endDate);
        assertEquals(200, state.getResponse().statusCode(),
                "Could not create the trip: " +
                        state.getResponse().asString());
    }
    // ---------- UPDATE (read the trip first, then send it back with a new name) ----------
    @When("I rename the trip to {string}")
    public void iRenameTheTripTo(String newName) {
        JsonPath trip = state.request().when().get(tripUrl()).jsonPath();
        String body = """
            { "name": "%s","destination": "%s","description": "%s","startDate": "%s","endDate": "%s" }"""
                .formatted(newName, trip.getString("destination"),
                        trip.getString("description"),
                        trip.getString("startDate"), trip.getString("endDate"));
        state.setResponse(state.request()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(tripUrl()));
    }
    @Then("the trip is called {string}")
    public void theTripIsCalled(String expectedName) {
        String actualName =
                state.request().when().get(tripUrl()).jsonPath().getString("name");
        assertEquals(expectedName, actualName);
    }
    // ---------- DELETE ----------
    @When("I delete the trip")
    public void iDeleteTheTrip() {
        state.setResponse(state.request().when().delete(tripUrl()));
    }
    @Then("the trip no longer exists")
    public void theTripNoLongerExists() {
        assertEquals(404,
                state.request().when().get(tripUrl()).statusCode());
        state.setTripId(null); // already gone, nothing to clean up
    }
    // ---------- SECURITY: someone else's trip ----------
    @When("I open the trip {string} that belongs to {string}")
    public void iOpenSomeoneElsesTrip(String tripName, String ownerEmail) {// Log in as the owner only to find the trip's id (every seeded user uses password123)
        String ownerToken = AuthSteps.login(ownerEmail,
                "password123").jsonPath().getString("message");
        Long otherTripId = given()
                .header("Authorization"
                        ,
                        "Bearer " + ownerToken)
                .when()
                .get("/api/trips")
                .jsonPath()
                .getLong("find { it.name == '" + tripName + "' }.id");
        // Now try to open it with OUR token
        state.setResponse(state.request().when().get("/api/trips/" +
                otherTripId));
    }
// ---------- CLEAN UP: runs after every scenario, even a failed one ----------
    @After
    public void deleteTestTrip() {
        if (state.getTripId() != null) {
            state.request().when().delete(tripUrl());
        }
    }
}