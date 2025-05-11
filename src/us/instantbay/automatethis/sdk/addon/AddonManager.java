package us.instantbay.automatethis.sdk.addon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import us.instantbay.automatethis.AutomateThis;
import us.instantbay.automatethis.Event.Event;
import us.instantbay.automatethis.Events.PreAddonLoadEvent;
import us.instantbay.automatethis.Listener.ListenerManager;
import us.instantbay.automatethis.sdk.AutomateThisSDK;

public class AddonManager {
	
	private ArrayList<Addon> addons = new ArrayList<Addon>();
	private ListenerManager listenerManager;
	
	private List<String> requiredKeys = Arrays.asList(
			"main",
			"name",
			"sdk"
			);
	
	public ArrayList<Addon> getAddons(){
		return this.addons;
	}
	
	public AddonManager(ListenerManager listenerManager) {
		this.listenerManager = listenerManager;
	}
	
	@SuppressWarnings("removal")
	public void enableAddon(Addon addon) throws IOException, URISyntaxException {
		Event preAddonEvent = new PreAddonLoadEvent();
		preAddonEvent.fireEvent();
		
		if (preAddonEvent.isCancelled()) {
			return ;
		}
		
		File addonJar = addon.getAddonFile();
		
		Map<String, Object> yml = this.getAddonYML(addonJar.getAbsolutePath());
		
		if (yml != null) {
			int missingObjects = 0;
			for (String requiredKey : this.requiredKeys) {
				if (!yml.containsKey(requiredKey)) {
					System.out.println("Error 'addon.yml' missing key: " + requiredKey);
					missingObjects++;
				}
			}
			if (missingObjects > 0) {
				System.out.println("Error 'addon.yml' is missing '" + missingObjects + "' var");
				AutomateThis.getInstance().stop();
				return ;
			}
		}
		else {
			System.out.println("Error addon.yml is null");
			AutomateThis.getInstance().stop();
			return ;
		}
		
		String MAIN = (String) yml.get("main");
		String NAME = (String) yml.get("name");
		float SDK_VERSION = 0;
		
		if (yml.get("sdk") instanceof String) {
			System.out.println("Error, in addon.yml 'sdk' cannot be a string only floats");
			AutomateThis.getInstance().stop();
		}
		else if (yml.get("sdk") instanceof Number ) {
			SDK_VERSION = ((Number) yml.get("sdk")).floatValue();
		}
		else {
			System.out.println("Invalid object type for 'sdk' only floats are supported.");
			AutomateThis.getInstance().stop();
		}
		
		try {
			for (Addon addn : this.getAddons()) {
				if (addn.getName() == NAME) {
					System.out.println("error, duplicate addon: '" + addn.getName() + "'" + " & '" + NAME + "' Please remove one of the addons");
					addn.getThread().suspend();
					this.getAddons().remove(addn);
					return ;
				}
			}
			System.out.println("Initializing addon: '" + NAME +"'");
			System.out.println("Checking SDK Version...");
			
			if (SDK_VERSION != AutomateThis.VERSION) {
				System.out.println("Warning: '" + NAME + "' uses SDK: '" + SDK_VERSION + "'\n while you are using application version: " + AutomateThis.VERSION);
				System.out.println(" Some Event handlers may not be supported.");
			}
			else {
				System.out.println("ADDON: '" + NAME + "' is using SDK: '" + SDK_VERSION + "'");
			}
				
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {addonJar.toURI().toURL()}, this.getClass().getClassLoader());
			Class<?> mainClass = Class.forName(MAIN, true, classLoader);
			Constructor<?> constructor = mainClass.getConstructor();
			Object obj = constructor.newInstance();
				
			if (obj instanceof AutomateThisSDK sdk) {
				Addon addn = addon;
				Thread addonThread = new Thread(addn);
				addn.setThread(addonThread);
				addn.setName(NAME);
				addn.setAddonFolder("addons/" + addn.getAddonFile().getName().toLowerCase().toString().substring(0, addon.getAddonFile().getName().length() - 4));
				sdk.setThisAddon(addn);
				this.addons.add(addn);
				sdk.setAddonManager(this);
				sdk.setListenerManager(this.listenerManager);
				sdk.onEnable();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Map<String, Object> getAddonYML(String addonFilePath) throws IOException, URISyntaxException {
	    Yaml yaml = new Yaml();
	    InputStream in;

	    File file = new File(addonFilePath);
	    URL jarUrl = new URI("jar:" + file.toURI().toURL() + "!/addon.yml").toURL();

	    JarURLConnection connection = (JarURLConnection) jarUrl.openConnection();
	    in = connection.getInputStream();

	    return yaml.load(in);
	}

}
