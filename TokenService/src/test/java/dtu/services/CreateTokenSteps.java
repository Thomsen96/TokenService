package dtu.services;
import dtu.application.TokenService;
import dtu.domain.Token;
import dtu.infrastructure.AccountAccess;
import dtu.infrastructure.LocalTokenRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.assertEquals;

import messaging.implementations.MockMessageQueue;

import java.util.ArrayList;
public class CreateTokenSteps {
		String customerId = null;
		String merchantId = null;
		String sessionId;
		private static MockMessageQueue messageQueue = new MockMessageQueue();
		private static AccountAccess accountAccess = new AccountAccess(messageQueue);
		private TokenService tokenService = new TokenService(messageQueue, new LocalTokenRepository(), accountAccess);
		ArrayList<Token> tokens = new ArrayList<>();
	
		@Given("a customer with id {string}")
		public void aCustomerWithId(String customerId) {
			this.customerId = customerId;
			this.sessionId = "uniqueSessionId";
		}
	
		@Given("the customer already has {int} tokens")
		public void theCustomerAlreadyHasTokens(Integer numOfTokens) {
			tokenService.generateTokenEventResponse(sessionId, customerId, numOfTokens);
		}
	
		@When("the customer requests {int} tokens")
		public void theCustomerRequestsTokens(Integer numOfTokens) {
			tokenService.generateTokenEventResponse(sessionId, customerId, numOfTokens);
			tokens = tokenService.getTokens(customerId);
		}
	
		@Then("the customer has {int} tokens")
		public void theCustomerHasTokens(Integer expectedNumOfTokens) {
			assertEquals(expectedNumOfTokens, tokens.size());
		}
}
