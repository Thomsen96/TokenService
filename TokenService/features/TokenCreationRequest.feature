Feature: Verify token creation request

  Scenario: Token creation request is responded to
    Given A customer with id "1234561234" requests 1 token with sessionId "UniqueSessionId"
    When a request to create and receive new tokens is received
    Then the tokens are sent
