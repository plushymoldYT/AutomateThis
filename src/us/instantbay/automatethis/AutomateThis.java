package us.instantbay.automatethis;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import us.instantbay.automatethis.Event.EventManager;
import us.instantbay.automatethis.Events.PreAddonLoadEvent;
import us.instantbay.automatethis.Events.PreEventProcessEvent;
import us.instantbay.automatethis.Events.TickEvent;
import us.instantbay.automatethis.sdk.addon.Addon;
import us.instantbay.automatethis.sdk.addon.AddonManager;

public class AutomateThis implements Runnable {
	
	public static final float VERSION = 1.3f;
	
	static AutomateThis instance;
	
	private boolean running;
	private static EventManager eventManager = new EventManager();
	private AddonManager addonManager = new AddonManager(eventManager.getListenerManager());
	
	public void run() {
		while (running) {
			eventManager.tick();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void start() {
		this.running = true;
		eventManager.getEvents().add(new TickEvent());
		eventManager.getEvents().add(new PreEventProcessEvent());
		eventManager.getEvents().add(new PreAddonLoadEvent());
		//eventManager.getListenerManager().getListeners().add(new DebugListener());
		new Thread(this).start();
	}
	
	public void stop() {
		this.running = false;
	}
	
	public static EventManager getEventManager() {
		return eventManager;
	}
	
	public static void main(String[] args) {
		AutomateThis automateThis = new AutomateThis();
		AutomateThis.instance = automateThis;
		
		File addonsFolder = new File("addons");
		if (!addonsFolder.exists()) {
			addonsFolder.mkdirs();
		}
		
		for (File f : addonsFolder.listFiles()) {
			if (f.getAbsolutePath().toString().endsWith(".jar")) {
				Addon addon = new Addon();
				addon.setAddonFile(f);
				
				try {
					automateThis.addonManager.enableAddon(addon);
				} 
				catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
		
		automateThis.start();
	}
	
	public static AutomateThis getInstance() {
		return instance;
	}

}
