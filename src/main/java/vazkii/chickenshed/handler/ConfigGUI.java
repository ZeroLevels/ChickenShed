package vazkii.chickenshed.handler;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import vazkii.chickenshed.ChickenShed;

public class ConfigGUI extends GuiConfig {

	public ConfigGUI(GuiScreen parentScreen) {
		super(
				parentScreen,
				//configElements
				new ConfigElement(ConfigurationHandler.config.getCategory("general")).getChildElements(),
				//modID
				ChickenShed.MODID,
				//allRequireWorldRestart
				false,
				//allRequireMcRestart
				false,
				//title
				GuiConfig.getAbridgedConfigPath(ConfigurationHandler.config.toString())
				);
	}

	

}
