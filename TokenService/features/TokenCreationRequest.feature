Feature: Verify token creation request

  Scenario: Token creation request is responded to
    Given A customer with id "1234561234" requests 1 token with sessionId "UniqueSessionId"
  # Send request
    When a request to create and receive new tokens is received
  # Verificer that it have been sent
    Then the customer verification request is sent

  # Reveive the response
    When the verification response event is received
  # Assert that the response is valid.
  	Then the token creation response is sent
    