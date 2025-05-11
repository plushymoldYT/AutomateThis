package us.instantbay.automatethis.sdk.addon;

import java.io.File;

public class Addon implements Runnable {
	
	private File addonFile;
	private String name;
	private String addonFolder;
	private Thread addonThread;
	
	public String getName() {
		return this.name;
	}
	
	public Thread getThread() {
		return this.addonThread;
	}
	
	public void setThread(Thread thread) {
		this.addonThread = thread;
	}
	
	public String getAddonFolder() {
		return this.addonFolder;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public File getAddonFile() {
		return this.addonFile;
	}
	
	public void setAddonFile(File addonFile) {
		this.addonFile = addonFile;
	}
	
	public void setAddonFolder(String addonFolder) {
		this.addonFolder = addonFolder;
	}
	
	public void run() {
		
	}

}
