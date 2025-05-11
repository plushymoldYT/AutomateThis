package us.instantbay.automatethis.sdk;

import us.instantbay.automatethis.Listener.ListenerManager;
import us.instantbay.automatethis.sdk.addon.Addon;
import us.instantbay.automatethis.sdk.addon.AddonManager;
import us.instantbay.automatethis.sdk.config.YamlConfig;

public abstract class AutomateThisSDK {
	
	private AddonManager addonManager;
	private ListenerManager listenerManager;
	private Addon addon;
	public YamlConfig yamlConfig;
	
	public void onEnable() {};
	public void onDisable() {};
	public void onTick() {};
	
	public AddonManager getAddonManager() {
		return this.addonManager;
	}
	
	public void setThisAddon(Addon addon){
		this.addon = addon;
	}
	
	public ListenerManager getListenerManager() {
		return this.listenerManager;
	}
	
	public void setListenerManager(ListenerManager listenerManager) {
		this.listenerManager = listenerManager;
	}
	
	public void setAddonManager(AddonManager addonManager) {
		this.addonManager = addonManager;
	}
	
	public Addon getThisAddon() {
		return this.addon;
	}

}
