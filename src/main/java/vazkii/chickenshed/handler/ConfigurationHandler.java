package vazkii.chickenshed.handler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigurationHandler {

	public static Configuration config;
	
	public static boolean isEnabled;
	public static boolean chicksDropFeathers;
	public static boolean forceFeatherDrop;
	public static int dropFreq;
	
	public static void setConfig(File configFile) {
		config = new Configuration(configFile);
		syncConfig();
	}
	
	public static void syncConfig() {
		List<String> propOrder = new ArrayList<String>();
		Property currProp;
		
		// Sets whether the mod is enabled or disabled
		currProp = config.get(
				Configuration.CATEGORY_GENERAL,
				"enable",
				true,
				"Enables the mod.\nSet to false to disable the mod."
				);
		isEnabled = currProp.getBoolean(true);
		propOrder.add(currProp.getName());
		
		// Sets if feathers are dropped by baby chickens
		currProp = config.get(
				Configuration.CATEGORY_GENERAL,
				"chicksDropFeathers",
				true,
				"Do baby chickens drop feathers?\nSet to false to make them drop nothing at all."
				);
		chicksDropFeathers = currProp.getBoolean(true);
		propOrder.add(currProp.getName());
		
		// Sets if chickens drop feathers on death, does not affect baby chickens
		currProp = config.get(
				Configuration.CATEGORY_GENERAL,
				"forceFeatherDrop",
				true,
				"Will feathers be a 100% drop when a chicken is killed?\nSet to false to prevent feathers from being a guaranteed drop."
				);
		forceFeatherDrop = currProp.getBoolean(true);
		propOrder.add(currProp.getName());
		
		// Sets the frequency of feather drops
		currProp = config.get(
				Configuration.CATEGORY_GENERAL,
				"dropFrequency",
				26000,
				"How often will feathers be shed?\nDrop chance is 1/dropFrequency.",
				// Value range: 6000+
				6000, Integer.MAX_VALUE
				);
		dropFreq = currProp.getInt(26000);
		propOrder.add(currProp.getName());
		
		// Order configurations
		config.setCategoryPropertyOrder(Configuration.CATEGORY_GENERAL, propOrder);
		
		if (config.hasChanged()) {
			config.save();
		}
	}
}