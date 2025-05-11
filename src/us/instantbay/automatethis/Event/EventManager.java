package us.instantbay.automatethis.Event;

import java.util.ArrayList;

import us.instantbay.automatethis.Events.PreEventProcessEvent;
import us.instantbay.automatethis.Listener.ListenerManager;

public class EventManager {
	
	private ListenerManager listenerManager = new ListenerManager();
	private ArrayList<Event> events = new ArrayList<Event>();
	
	public ArrayList<Event> getEvents(){
		return this.events;
	}
	
	@SuppressWarnings("deprecation")
	public void fireEvent(Event e) {
		if (e instanceof PreEventProcessEvent) {
			
		}
		else {
			Event processEvent = new PreEventProcessEvent();
			processEvent.fireEvent();
			e.fireEvent();
		}
	}
	
	public ListenerManager getListenerManager() {
		return this.listenerManager;
	}
	
	public void tick() {
		for (Event e : this.getEvents()) {
			e.fireEvent();
			listenerManager.fireEvent(e);
		}
	}

}
