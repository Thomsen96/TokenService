package dtu.TokenService.Presentation;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Repositories.LocalTokenRepository;
import dtu.TokenService.Infrastructure.MessageQueueFactory;
import messaging.MessageQueue;

public class Runner {
  public static void main(String[] args) {
    MessageQueue messageQueue = new MessageQueueFactory().getMessageQueue();
    TokenService tokenService = new TokenService(new LocalTokenRepository());
    TokenEventHandler handler = new TokenEventHandler(messageQueue, tokenService);
    System.out.println(handler.toString() + " Token service started");
  }
}