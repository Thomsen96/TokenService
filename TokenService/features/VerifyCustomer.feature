Feature: Verify customer
  
  Scenario: Customer verification
  # Create customer object
    Given a customer has an id "1234561234" 
  # Send request
    When the customer is being verified
  # Verificer that it have been sent
    Then the "CustomerVerificationRequested" event is sent

  # Reveive the response
    When the "CustomerVerified" event is sent with customerId
  # Assert that the response is valid.
  	Then the customer is verified