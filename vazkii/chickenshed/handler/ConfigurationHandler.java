package vazkii.chickenshed.handler;
import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

	public static Configuration config;
	
	public static boolean isEnabled;
	public static boolean chicksDropFeathers;
	public static boolean forceFeatherDrop;
	public static int dropFreq;
	
	public static void initConfig(File configFile) {
		config = new Configuration(configFile);
		
		// Loading the config into memory
		config.load();
		
		// Sets if the mod to be enable or disabled
		isEnabled = config.get(
				Configuration.CATEGORY_GENERAL,
				"enable",
				true,
				"Enables the mod. The default is true. Set to false to disable the mod."
				).getBoolean(true);
		
		// Sets if feathers are dropped by baby chickens
		chicksDropFeathers = config.get(
				Configuration.CATEGORY_GENERAL,
				"chicksDropFeathers",
				true,
				"Do baby chickens drop feathers? The default is true. Set to false to make them drop nothing at all."
				).getBoolean(true);
		
		// Sets if chickens drop feathers on death, does not affect baby chickens
		forceFeatherDrop = config.get(
				Configuration.CATEGORY_GENERAL,
				"forceFeatherDrop",
				true,
				"Will feathers be a 100% drop when a chicken is killed? The default is true. Set to false to prevent feathers from being a guaranteed drop"
				).getBoolean(true);
		
		// Sets the frequency of feather drops
		dropFreq = config.get(
				Configuration.CATEGORY_GENERAL,
				"dropFrequency",
				26000,
				"How often will feathers be shed? The default is 26000. The minimum is 6000"
				).getInt(26000);
		
		config.save();
		
		// Setting dropFreq to min. value if the given value is less then min.
		if (dropFreq < 6000)
			dropFreq = 6000;
	}
}