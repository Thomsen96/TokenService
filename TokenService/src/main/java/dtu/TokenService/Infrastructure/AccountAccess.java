package dtu.TokenService.Infrastructure;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

public class AccountAccess {

	private MessageQueue messageQueue;
	private CompletableFuture<Event> customerVerified;

	public AccountAccess(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

	public Event customerVerificationRequest(String customerId, String sessionId) {
		customerVerified = new CompletableFuture<Event>();
		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId);
		Event outgoingEvent = new Event("CustomerVerificationRequest", eventResponse);
		
		messageQueue.addHandler("CustomerVerificationResponse." + sessionId, this::handleCustomerVerificationResponse);
		messageQueue.publish(outgoingEvent);

		(new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
					EventResponse eventResponse = new EventResponse(sessionId, false, "No response from AccountService");
					Event value = new Event("CustomerVerificationResponse." + sessionId, eventResponse);
					customerVerified.complete(value);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return customerVerified.join();
	}

	// Handler for verification response from CustomerService in the form of a boolean to see if the customer is registered.
	public void handleCustomerVerificationResponse(Event e) {
		System.err.println("CustomerVerificationResponse" + e);
		customerVerified.complete(e);
	}

}
