Feature: Verify customer

  Scenario: Sends event to verify customer
    Given A customer with id "1234561234"
    When the customer the event verify the customer have happend
    Then the "VerifyCustomer" is sent 