package messaging.implementations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import messaging.Event;
import messaging.MessageQueue;

public class MockMessageQueue implements MessageQueue {

  static Map<String, Consumer<Event>> handlers = new ConcurrentHashMap<>();

  static Set<Event> events = Collections.newSetFromMap(new ConcurrentHashMap<Event, Boolean>());



  public static Stack<Event> eventStack = new Stack<>();

  public Boolean verify(Event message)
  {
    System.err.println(Arrays.toString(events.toArray()));
    return events.contains(message);
  }

  public Event getEvent(String topic)
  {
    return events.stream().filter(t -> t.getType().equals(topic)).findFirst().get();
  }

  
  @Override
  public void publish(Event message) {
    System.err.println("publish " + message);
    events.add(message);
    eventStack.push(message);

    //handlers.get(message.getType()).accept(message);
  }
  
  @Override
  public void addHandler(String eventType, Consumer<Event> handler) {
    System.err.println("addHandler " + eventType);
    handlers.put(eventType, handler);
  }

  
}

