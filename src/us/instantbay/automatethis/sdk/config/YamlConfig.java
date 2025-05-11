package us.instantbay.automatethis.sdk.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import us.instantbay.automatethis.sdk.addon.Addon;

public class YamlConfig {
	
	Addon addon;
	String yamlName;
	Map<String, Object> values;
	
	public YamlConfig(Addon addon, String yamlName) {
		this.addon = addon;
		this.yamlName = yamlName;
	}
	
	public void setKey(String key, Object object) {
	    this.values.put(key, object);
	}

	
	public Object getKey(String key) {
		return this.values.get(key);
	}
	
	public void saveConfig() {		
		if (this.values == null) {
		    this.values = new java.util.HashMap<>();
		}

		
	    DumperOptions options = new DumperOptions();
	    options.setIndent(2);
	    options.setPrettyFlow(true);
	    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

	    Yaml yaml = new Yaml(options);

	    File configFile = new File(this.addon.getAddonFolder(), this.yamlName);
	    try (FileWriter writer = new FileWriter(configFile)) {
	        yaml.dump(this.values, writer);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	public void loadConfig() {
	    Yaml yaml = new Yaml();
	    
	    if (this.values == null) {
	        this.values = new java.util.HashMap<>();
	    }
	    
	    File configFolder = new File(this.addon.getAddonFolder());
	    if (!configFolder.exists()) {
	        configFolder.mkdirs();
	    }

	    File configFile = new File(configFolder, this.yamlName);
	    if (!configFile.exists()) {
	        try {
	            configFile.createNewFile();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return;
	        }
	    }

	    try (InputStream in = new FileInputStream(configFile)) {
	        this.values = yaml.load(in);
	        if (this.values == null) {
	            this.values = new java.util.HashMap<>();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


}
