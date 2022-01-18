package dtu.services;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Token;
import dtu.TokenService.Infrastructure.AccountAccess;
import dtu.TokenService.Infrastructure.LocalTokenRepository;
import dtu.TokenService.Presentation.TokenEventHandler;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.implementations.MockMessageQueue;

public class VerifyTokenSteps {

	String customerId = null;
	Token token = null;
	String sessionId;
	private static MockMessageQueue messageQueue = new MockMessageQueue();
	private static AccountAccess accountAccess = new AccountAccess(messageQueue);
	private TokenService tokenService = new TokenService(messageQueue, new LocalTokenRepository(), accountAccess);
	private TokenEventHandler tokenEventHandler = new TokenEventHandler(messageQueue, tokenService);
	private CompletableFuture<HashSet<Token>> tokenCreation = new CompletableFuture<>();
	
	@Given("A customer with id {string}")
	public void aCustomerWithId(String customerId) {
		this.customerId = customerId;
		this.sessionId = "uniqueSessionId";
	}
	


	@And("the customer has tokens")
	public void theCustomerHasTokens() throws Throwable {
		new Thread(() -> {
			var tokens = tokenService.createTokens(customerId, 1, sessionId);
			tokenCreation.complete(tokens);
		}).start();
	}
	
	@When("the account verification response event is received")
	public void theAccountVerificationResponseEventIsReceived() throws InterruptedException {
		Event event = new Event("CustomerVerificationResponse." + sessionId,new Object[] { true });
		Thread.sleep(100);
		accountAccess.handleCustomerVerificationResponse(event);
	}

	@When("a request to verify the token is received")
	public void aRequestToVerifyTheTokenIsReceived() {
//		token = tokenService.createTokens(customerId, 1, sessionId).stream().findFirst().get();
		token = tokenCreation.join().iterator().next();
		tokenEventHandler.handleTokenVerificationRequest(new Event("TokenVerificationRequested",new Object[] {token.getUuid(), sessionId }));
	}

	
	@Then("the token is verified")
	public void theTokenIsVerified() {
		Event event = new Event("TokenVerificationResponse." + sessionId, new Object[] { token });
		assertEquals(event, messageQueue.getEvent("TokenVerificationResponse." + sessionId));
	}

}