Feature: Verify customer

  Scenario: Customer verification
    Given a customer has an id "1234561234"
    When the customer is being verified
    Then the "CustomerVerificationRequested" event is sent 