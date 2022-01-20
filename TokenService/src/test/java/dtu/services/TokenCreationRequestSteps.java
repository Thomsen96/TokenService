package dtu.services;
import dtu.application.TokenService;
import dtu.domain.Token;
import dtu.infrastructure.AccountAccess;
import dtu.infrastructure.LocalTokenRepository;
import dtu.presentation.TokenEventHandler;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.EventResponse;
import messaging.implementations.MockMessageQueue;

public class TokenCreationRequestSteps {

	String customerId = null;
	int numOfTokens;
	String sessionId;
	Token token = null;
	private static MockMessageQueue messageQueue = new MockMessageQueue();
	private TokenService tokenService = new TokenService(messageQueue, new LocalTokenRepository());
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
		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId, numOfTokens);
		Event incommingEvent = new Event("TokenCreationRequest", eventResponse);

		new Thread(() -> {
			tokenEventHandler.handleTokenCreationRequest(incommingEvent);
			tokenCreationProcess.complete(true);
		}).start();
	}

	@Then("the customer verification request is sent")
	public void theCustomerVerificationRequestIsSent() throws InterruptedException {
		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId);
		Event expectedVerificationRequestEvent = new Event("CustomerVerificationRequest", eventResponse); 
		Thread.sleep(200);
		Event actualVerificationRequestEvent = messageQueue.getEvent("CustomerVerificationRequest");
		assertEquals(expectedVerificationRequestEvent, actualVerificationRequestEvent);
	}

	@When("the verification response event is received")
	public void theVerificationResponseEventIsReceived() {
		EventResponse eventResponse = new EventResponse(sessionId, true, null);
		Event event = new Event("CustomerVerificationResponse." + sessionId, eventResponse);
		tokenService.handleCustomerVerificationResponse(event);
	}

	@Then("the token creation response is sent")
	public void theTokenCreationResponseIsSent() {
		tokenCreationProcess.join();
		EventResponse eventResponse = new EventResponse(sessionId, true, null, tokenService.getTokens(customerId));
		Event expectedCreationResponseEvent = new Event("TokenCreationResponse." + sessionId, eventResponse);
		assertEquals(expectedCreationResponseEvent, messageQueue.getEvent("TokenCreationResponse." + sessionId));
	}


}