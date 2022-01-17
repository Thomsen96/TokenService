package dtu.services;
import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Infrastructure.AccountAccess;
import dtu.TokenService.Presentation.TokenEventHandler;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.implementations.MockMessageQueue;

public class TokenCreationRequestSteps {

	String customerId = null;
	int numOfTokens;
	String sessionId;
	Token token = null;
	private static MockMessageQueue messageQueue = new MockMessageQueue();
	private static AccountAccess accountAccess = new AccountAccess(messageQueue);
	private TokenService tokenService = new TokenService(messageQueue, new LocalTokenRepository(), accountAccess);
	private TokenEventHandler tokenEventHandler = new TokenEventHandler(messageQueue, tokenService);
	private CompletableFuture<Boolean> tokenCreationProcess = new CompletableFuture<>();
	
	@Given("A customer with id {string} requests {int} token with sessionId {string}")
	public void aCustomerWithId(String customerId, int numOfTokens, String sessionId) {
		this.customerId = customerId;
		this.numOfTokens = numOfTokens;
		this.sessionId = sessionId;
	}

	@When("a request to create and receive new tokens is received")
	public void aRequestToCreateAndReceiveNewTokensIsReceived() {
		Event incommingEvent = new Event("TokenCreationRequest", new Object[] {customerId, numOfTokens, sessionId});

		new Thread(() -> {
			tokenEventHandler.handleTokenCreationRequest(incommingEvent);
			tokenCreationProcess.complete(true);
		}).start();
	}

	@Then("the customer verification request is sent")
	public void theCustomerVerificationRequestIsSent() throws InterruptedException {
		Event event = new Event("CustomerVerificationRequest", new Object[] { customerId , sessionId}); 
		Thread.sleep(100);
		Event customerVerificationRequest = messageQueue.getEvent("CustomerVerificationRequest");
		assertEquals(event, customerVerificationRequest);
	}

	@When("the verification response event is received")
	public void theVerificationResponseEventIsReceived() {
		Event event = new Event("CustomerVerificationResponse." + sessionId,new Object[] { true });
		accountAccess.handleCustomerVerificationResponse(event);
	}

	@Then("the token creation response is sent")
	public void theTokenCreationResponseIsSent() {
		tokenCreationProcess.join();
		Event responseEvent = new Event("TokenCreationResponse." + sessionId, new Object[] { tokenService.getTokens(customerId) });
		assertEquals(responseEvent, messageQueue.getEvent("TokenCreationResponse." + sessionId));
	}


}