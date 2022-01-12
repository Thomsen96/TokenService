Feature: Verify customer

  Scenario: Token is valid and verified
    Given A customer with id "1234561234" and token "TOTALLY UNIQUE TOKEN"
    When a verification of token "TOTALLY UNIQUE TOKEN" is received for verification
    Then the token is verified
