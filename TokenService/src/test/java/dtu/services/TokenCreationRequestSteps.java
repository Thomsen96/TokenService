package dtu.services;
import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Presentation.TokenEventHandler;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import messaging.Event;
import messaging.MessageQueue;

public class TokenCreationRequestSteps {

	String customerId = null;
	int numOfTokens;
	String sessionId;
	Token token = null;

	private MessageQueue messageQueue = mock(MessageQueue.class);
	private TokenService tokenService = new TokenService(new LocalTokenRepository());
	private TokenEventHandler service = new TokenEventHandler(messageQueue, tokenService);


//	@Given("^A customer with id \"([^\"]*)\" requests (\\d+) token with sessionId \"([^\"]*)\"$")
//	public void aCustomerWithIdRequestsTokenWithSessionId(String arg1, int arg2, String arg3) throws Throwable {
//		throw new PendingException();
//	}
	@Given("A customer with id {string} requests {int} token with sessionId {string}")
	public void aCustomerWithId(String customerId, int numOfTokens, String sessionId) {
		this.customerId = customerId;
		this.numOfTokens = numOfTokens;
		this.sessionId = sessionId;
	}

	@When("a request to create and receive new tokens is received")
	public void aRequestToCreateAndReceiveNewTokensIsReceived() {
		token = tokenService.createTokens(customerId, 1).iterator().next();
		Event incommingEvent = new Event("TokenCreationRequest",new Object[] {customerId, numOfTokens, sessionId});
		service.handleTokenCreationRequest(incommingEvent);
	}

	@Then("the tokens are sent")
	public void theTokensAreSent() {
		Event event = new Event("TokenCreationResponse" + "#" + sessionId, new Object[] { token });
		verify(messageQueue).publish(event);
	}
}