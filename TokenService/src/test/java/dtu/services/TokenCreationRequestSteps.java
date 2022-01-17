package dtu.services;
import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Entities.Token;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Presentation.TokenEventHandler;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.assertEquals;
import messaging.Event;
import messaging.implementations.MockMessageQueue;

public class TokenCreationRequestSteps {

	String customerId = null;
	int numOfTokens;
	String sessionId;
	Token token = null;
	private static MockMessageQueue messageQueue = new MockMessageQueue();
	private TokenService tokenService = new TokenService(new LocalTokenRepository());
	private TokenEventHandler messageService = new TokenEventHandler(messageQueue, tokenService);

	@Given("A customer with id {string} requests {int} token with sessionId {string}")
	public void aCustomerWithId(String customerId, int numOfTokens, String sessionId) {
		this.customerId = customerId;
		this.numOfTokens = numOfTokens;
		this.sessionId = sessionId;
	}

	@When("a request to create and receive new tokens is received")
	public void aRequestToCreateAndReceiveNewTokensIsReceived() {
		Event incommingEvent = new Event("TokenCreationRequest",new Object[] {customerId, numOfTokens, sessionId});
		messageService.handleTokenCreationRequest(incommingEvent);
	}

	@Then("the tokens are sent")
	public void theTokensAreSent() {
		assertEquals("TokenCreationResponse#" + sessionId, messageQueue.getEvent("TokenCreationResponse#" + sessionId).getType());
	}
}