package us.instantbay.automatethis.sdk.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import us.instantbay.automatethis.sdk.addon.Addon;

public class YamlConfig {
	
	Addon addon;
	String yamlName;
	Map<String, Object> values;
	public boolean encryptString = false;
	
	public YamlConfig(Addon addon, String yamlName) {
		this.addon = addon;
		this.yamlName = yamlName;
	}
	
	public void setKey(String key, Object object) {
		if (this.encryptString == true) {
			if (object instanceof String) {
				byte[] valueBytes = ((String) object).getBytes();
				byte[] keyBytes = key.getBytes();
				
				this.values.put(Base64.getEncoder().encodeToString(keyBytes), Base64.getEncoder().encodeToString(valueBytes));
				return ;
			}
		}
		
	    this.values.put(key, object);
	}

	
	public Object getKey(String key) {
		if (this.encryptString == true) {
			byte[] keyBytes = key.getBytes();
			String encryptedKey = Base64.getEncoder().encodeToString(keyBytes);
			
			String base64Result = (String) this.values.get(encryptedKey);
			String finalResult = new String(Base64.getDecoder().decode(base64Result));
			
			return finalResult;
		}
		else {
			return this.values.get(key);
		}
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
