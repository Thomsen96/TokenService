package dtu.infrastructure;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.HANDLE.CUSTOMER_VERIFICATION_REQUESTED;
import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.PUBLISH.CUSTOMER_VERIFICATION_RESPONDED;

public class AccountAccess {

	private MessageQueue messageQueue;
	private ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();

	public AccountAccess(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

	public Event customerVerificationRequest(String customerId, String sessionId) {
		sessions.put(sessionId, new CompletableFuture<Event>());
		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId);
		Event outgoingEvent = new Event(CUSTOMER_VERIFICATION_REQUESTED, eventResponse);
		
		messageQueue.addHandler(CUSTOMER_VERIFICATION_RESPONDED + sessionId, this::handleCustomerVerificationResponse);
		messageQueue.publish(outgoingEvent);

        new Thread(() -> {
        	try {
        		Thread.sleep(5000);
        		EventResponse eventResponseThread = new EventResponse(sessionId, false, "No response from AccountService");
        		Event value = new Event(CUSTOMER_VERIFICATION_RESPONDED + sessionId, eventResponseThread);
        		sessions.get(sessionId).complete(value);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
			
		}).start();
		return sessions.get(sessionId).join();
	}

	// Handler for verification response from CustomerService in the form of a boolean to see if the customer is registered.
	public void handleCustomerVerificationResponse(Event e) {
		System.err.println(CUSTOMER_VERIFICATION_RESPONDED + e);
		String sessionId = e.getArgument(0, EventResponse.class).getSessionId();
		sessions.get(sessionId).complete(e);
	}

}
