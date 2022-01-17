package dtu.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Infrastructure.AccountAccess;
import dtu.TokenService.Presentation.TokenEventHandler;
//import dtu.TokenService.Presentation.Resources.TokenMessageService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
//import messaging.MessageQueue;
import messaging.Event;
import messaging.implementations.MockMessageQueue;

public class CreateTokenSteps {
//	String customerId = null;
//	String merchantId = null;
//	String sessionId;
//	private static MockMessageQueue messageQueue = new MockMessageQueue();
//	private static AccountAccess accountAccess = new AccountAccess(messageQueue);
//	private TokenService tokenService = new TokenService(messageQueue, new LocalTokenRepository(), accountAccess);
//	HashSet<Token> tokens = new HashSet<>();
//
//	@Given("a customer with id {string}")
//	public void aCustomerWithId(String customerId) {
//		this.customerId = customerId;
//		this.sessionId = "uniqueSessionId";
//	}
//
//	@Given("the customer already has {int} tokens")
//	public void theCustomerAlreadyHasTokens(Integer numOfTokens) {
//		tokens = tokenService.createTokens(customerId, numOfTokens, sessionId);
//	}
//
//	@When("the customer requests {int} tokens")
//	public void theCustomerRequestsTokens(Integer numOfTokens) {
//		tokens = tokenService.createTokens(customerId, numOfTokens, sessionId);
//	}
//
//	@Then("the customer has {int} tokens")
//	public void theCustomerHasTokens(Integer expectedNumOfTokens) {
//		assertEquals(expectedNumOfTokens, tokens.size());
//	}
}
