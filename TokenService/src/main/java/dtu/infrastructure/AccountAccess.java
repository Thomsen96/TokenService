package dtu.infrastructure;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

public class AccountAccess {

	private MessageQueue messageQueue;
	private ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();

	public AccountAccess(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

	public Event customerVerificationRequest(String customerId, String sessionId) {
		sessions.put(sessionId, new CompletableFuture<Event>());
		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId);
		Event outgoingEvent = new Event("CustomerVerificationRequest", eventResponse);
		
		messageQueue.addHandler("CustomerVerificationResponse." + sessionId, this::handleCustomerVerificationResponse);
		messageQueue.publish(outgoingEvent);

        new Thread(() -> {
        	try {
        		Thread.sleep(5000);
        		EventResponse eventResponseThread = new EventResponse(sessionId, false, "No response from AccountService");
        		Event value = new Event("CustomerVerificationResponse." + sessionId, eventResponseThread);
        		sessions.get(sessionId).complete(value);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
			
		}).start();
		return sessions.get(sessionId).join();
	}

	// Handler for verification response from CustomerService in the form of a boolean to see if the customer is registered.
	public void handleCustomerVerificationResponse(Event e) {
		System.err.println("CustomerVerificationResponse" + e);
		String sessionId = e.getArgument(0, EventResponse.class).getSessionId();
		sessions.get(sessionId).complete(e);
	}

}
