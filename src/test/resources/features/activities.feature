Feature: Planning activities
  As a traveller
  I want to add activities to my trip, day by day
  So that I have a clear itinerary
  Background:
  Page 15 of 21
  Code-Along: Cucumber API T ests for TravelApp
    Given I am logged in as "sara@mail.com" with password "password123"
    And I have a trip called "Food trip to Lisbon" to "Lisbon, Portugal" from "2027-06-01" to "2027-06-04"
  Scenario: Activities come back in itinerary order
    When I add the activity "Pasteis de Belem" on day 2 at "10:00"
    And I add the activity "Tram 28 ride" on day 1 at "15:00"
    And I add the activity "Time Out Market lunch" on day 1 at "12:30"
    And I ask for the trip's activities
    Then the response status is 200
    And the activities are in this order:
      | Time Out Market lunch |
      | Tram 28 ride |
      | Pasteis de Belem |
  Scenario: Booking an activity
    When I add the activity "Fado night" on day 3 at "21:00"
    And I mark the activity as booked
    Then the response status is 200
    And the activity is booked