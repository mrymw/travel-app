Feature: Planning trips
  As a traveller
  I want to create and manage my trips
  So that I can plan my holidays
  Background:
    Given I am logged in as "sara@mail.com" with password "password123"
  Scenario: Sara only sees her own trips
    When I ask for my trips
    Then the response status is 200
    And I see a trip called "Winter in Tokyo"
    And I do not see a trip called "Istanbul Food Tour"
  Scenario: Create, rename and delete a trip
    When I create a trip called "Study trip to Rome" to "Rome, Italy" from "2027-05-01" to "2027-05-05"
    Then the response status is 200
    When I rename the trip to "Spring in Rome"
    Then the response status is 200
    And the trip is called "Spring in Rome"
    When I delete the trip
    Then the response status is 200
    And the trip no longer exists
  Scenario: Two trips can't have the same name
    When I create a trip called "Winter in Tokyo" to "Tokyo, Japan" from "2026-12-20" to "2026-12-27"
    Then the response status is 409
  Scenario: Sara can't open Ali's trip
    When I open the trip "Istanbul Food Tour" that belongs to "ali@mail.com"
    Then the response status is 404