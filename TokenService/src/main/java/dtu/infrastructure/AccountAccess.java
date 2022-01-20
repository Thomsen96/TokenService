package dtu.infrastructure;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import dtu.application.ServiceHelper;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

public class AccountAccess {

//	private MessageQueue messageQueue;
//    ServiceHelper serviceHelper = new ServiceHelper();
//	private static ConcurrentHashMap<String, CompletableFuture<Event>> sessions = new ConcurrentHashMap<>();
//
//	public AccountAccess(MessageQueue messageQueue) {
//		this.messageQueue = messageQueue;
//	}
//
//	public Event customerVerificationRequest(String customerId, String sessionId) {
//		sessions.put(sessionId, new CompletableFuture<Event>());
//		EventResponse eventResponse = new EventResponse(sessionId, true, null, customerId);
//		Event outgoingEvent = new Event("CustomerVerificationRequest", eventResponse);
//		
//		messageQueue.addHandler("CustomerVerificationResponse." + sessionId, this::handleCustomerVerificationResponse);
//		messageQueue.publish(outgoingEvent);
//
//		serviceHelper.addTimeOut(sessionId, sessions.get(sessionId), "No response from AccountService");
//
//		return sessions.get(sessionId).join();
//	}
//
//	// Handler for verification response from CustomerService in the form of a boolean to see if the customer is registered.
//	public void handleCustomerVerificationResponse(Event e) {
//		System.err.println("CustomerVerificationResponse " + e);
//		String sessionId = e.getArgument(0, EventResponse.class).getSessionId();
//		sessions.get(sessionId).complete(e);
//	}

}
