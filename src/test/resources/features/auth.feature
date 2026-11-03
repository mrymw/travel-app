Feature: Logging in
  As a traveller
  I want to log in with my email and password
  So that only I can see my trips

  Scenario: Logging in with the right password gives a token
    When I log in as "sara@mail.com" with password "password123"
    Then the response status is 200
    And I receive a token

  Scenario: Logging in with the wrong password gives no token
    When I log in as "sara@mail.com" with password "wrong-password"
    Then I do not receive a token

  Scenario: You can't see trips without logging in
    When I ask for my trips without a token
    Then the response status is 403