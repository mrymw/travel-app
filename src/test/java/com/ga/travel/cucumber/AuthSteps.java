package com.ga.travel.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


public class AuthSteps {
    private final ScenarioState state;

    public AuthSteps(ScenarioState state){
        this.state = state;
    }
    static Response login(String email, String password) {
        return given().contentType(ContentType.JSON).body("""
                {"email": "%s", "password": "%s"}"""
                . formatted(email, password)).when()
                .post("/auth/users/login");
    }
    @Given("I am logged in as {string} with password {string}")
    public void iAmLoggedInAs(String email, String password){
        String token = login(email, password).jsonPath()
                .getString("message");
        assertFalse(token.startsWith("Error"), "Login failed: " + token);
        state.setToken(token);
    }
    @When("I log in as {string} with password {string}")
    public void iLogInAs(String email, String password) {
        state.setResponse(login(email,password));
    }
    @Then("I receive a token")
    public void iReceiveAToken() {
        String message = state.getResponse().jsonPath()
                .getString("message");
        assertEquals(3, message.split("\\.").length,
                "Not a JWT: " + message);
    }
    @Then("I do not receive a token")
    public void iDoNotReceiveAToken (){
        String message = state.getResponse().jsonPath().getString("message");
        assertTrue(message.startsWith("Error"),
                "Expected an error but got: " + message);

    }
    @When("I ask for my trips without a token")
    public void iAskForMyTripsWithoutAToken() {
        state.setResponse(given().when().get("/api/trips"));
    }
    @Then("the response status is {int}")
        public void theResponseStatusIs(int expectedStatus) {
            assertEquals(expectedStatus, state.getResponse().statusCode(),
                    "Response body: " + state.getResponse().asString());
        }
}

