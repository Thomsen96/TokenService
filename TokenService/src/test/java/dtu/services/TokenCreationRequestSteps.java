package dtu.services;
import dtu.application.TokenService;
import dtu.domain.Token;
import dtu.domain.TokenDTO;
import dtu.infrastructure.AccountAccess;
import dtu.infrastructure.LocalTokenRepository;
import dtu.presentation.TokenEventHandler;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.HANDLE.CUSTOMER_VERIFICATION_REQUESTED;
import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.HANDLE.TOKEN_CREATION_REQUESTED;
import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.PUBLISH.CUSTOMER_VERIFICATION_RESPONDED;
import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.PUBLISH.TOKEN_CREATION_RESPONDED;
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
		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId, numOfTokens);
		Event incommingEvent = new Event(TOKEN_CREATION_REQUESTED, eventResponse);

		new Thread(() -> {
			tokenEventHandler.handleTokenCreationRequest(incommingEvent);
			tokenCreationProcess.complete(true);
		}).start();
	}

	@Then("the customer verification request is sent")
	public void theCustomerVerificationRequestIsSent() throws InterruptedException {
		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId);
		Event expectedVerificationRequestEvent = new Event(CUSTOMER_VERIFICATION_REQUESTED, eventResponse);
		Thread.sleep(200);
		Event actualVerificationRequestEvent = messageQueue.getEvent(CUSTOMER_VERIFICATION_REQUESTED);
		assertEquals(expectedVerificationRequestEvent, actualVerificationRequestEvent);
	}

	@When("the verification response event is received")
	public void theVerificationResponseEventIsReceived() {
		EventResponse eventResponse = new EventResponse(sessionId, true, null);
		Event event = new Event(CUSTOMER_VERIFICATION_RESPONDED + sessionId, eventResponse);
		accountAccess.handleCustomerVerificationResponse(event);
	}

	@Then("the token creation response is sent")
	public void theTokenCreationResponseIsSent() {
		tokenCreationProcess.join();
		//EventResponse eventResponse = new EventResponse(sessionId, true, null, tokenService.getTokensJson(customerId));
		EventResponse eventResponse = new EventResponse(sessionId, true, null, new TokenDTO(tokenService.getTokens(customerId)));
		Event expectedCreationResponseEvent = new Event(TOKEN_CREATION_RESPONDED + sessionId, eventResponse);
		assertEquals(expectedCreationResponseEvent, messageQueue.getEvent(TOKEN_CREATION_RESPONDED + sessionId));
	}


}
