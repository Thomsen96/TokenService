package dtu.TokenService.Infrastructure;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.MessageQueue;

public class AccountAccess {

	private MessageQueue messageQueue;
	private CompletableFuture<Event> customerVerified;

	public AccountAccess(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

	public Event customerVerificationRequest(String customerId, String sessionId) {
		customerVerified = new CompletableFuture<Event>();
		Event event = new Event("CustomerVerificationRequest", new Object[] { customerId, sessionId }); 
		messageQueue.addHandler("CustomerVerificationResponse." + sessionId, this::handleCustomerVerificationResponse);
		messageQueue.publish(event);

		(new Thread() {

			public void run() {

				try {

					Thread.sleep(5000);

					customerVerified.complete(new Event("", new Object[] { "No response from AccountService" }));

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
