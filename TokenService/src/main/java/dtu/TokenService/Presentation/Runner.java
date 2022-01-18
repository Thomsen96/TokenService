package dtu.TokenService.Presentation;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Infrastructure.AccountAccess;
import dtu.TokenService.Infrastructure.LocalTokenRepository;
import dtu.TokenService.Infrastructure.MessageQueueFactory;
import messaging.MessageQueue;

public class Runner {
  public static void main(String[] args) {
    MessageQueue messageQueue = new MessageQueueFactory().getMessageQueue();
	AccountAccess accountAccess = new AccountAccess(messageQueue);
    TokenService tokenService = new TokenService(messageQueue, new LocalTokenRepository(), accountAccess);
    TokenEventHandler handler = new TokenEventHandler(messageQueue, tokenService);
    System.out.println(handler.toString() + " Token service started");
  }
}