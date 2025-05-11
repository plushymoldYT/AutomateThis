package us.instantbay.automatethis.Event;

/**
 * @since 1.0
 * @author plushymold
 * @implNote 'setCancelled' will only work on the 'preAddonload event'
 */

public abstract class Event {
	
	private String name = null;
	private long timeMils = System.currentTimeMillis();
	private EventPriority eventPriority = EventPriority.LOW;
	private boolean cancelled = false;
	
	public void fireEvent() {};
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getTimeMils() {
		return this.timeMils;
	}
	
	public EventPriority getEventPriority() {
		return this.eventPriority;
	}
	
	public void setEventPriority(EventPriority eventPriority) {
		this.eventPriority = eventPriority;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public void setCancelled(boolean bool) {
		this.cancelled = bool;
	}

}
