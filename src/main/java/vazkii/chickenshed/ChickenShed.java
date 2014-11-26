package vazkii.chickenshed;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.chickenshed.handler.ConfigurationHandler;

@Mod(
		modid      = ChickenShed.MODID,
		name       = ChickenShed.NAME,
		version    = ChickenShed.VERSION,
		
		guiFactory = "vazkii.chickenshed.handler.GUIFactory"
	)
public class ChickenShed {
	public static final String MODID   = "ChickenShed";
	public static final String NAME    = "Chicken Shed";
	public static final String VERSION = "1.1.4";
	
	public static Configuration config;

	@Mod.Instance(MODID)
	public static ChickenShed instance;
	
	@Mod.EventHandler
	public void initPre(FMLPreInitializationEvent event) {
		// Initiating configuration
		ConfigurationHandler.setConfig(event.getSuggestedConfigurationFile());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(instance);
		
		if (ConfigurationHandler.isEnabled)
			MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(MODID)) {
			ConfigurationHandler.syncConfig();
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		EntityChicken chicken;
		
		// Breaks if code is running on client or entity is not a chicken
		if (event.entity.worldObj.isRemote || !(event.entity instanceof EntityChicken))
			return;
		
		chicken = (EntityChicken) event.entity;
		
		// Breaks if the chicken is a baby and baby chicken drops are not enabled
		if (chicken.isChild() && !ConfigurationHandler.chicksDropFeathers)
			return;
		
		// Picking a random number and dropping feather is 0
		if(chicken.worldObj.rand.nextInt(ConfigurationHandler.dropFreq) == 0)
			chicken.dropItem(Items.feather, 1);
	}
	
	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {
		boolean setFeather = false;
		EntityChicken chicken;
		
		// Break checking
		if (event.entity.worldObj.isRemote || !ConfigurationHandler.forceFeatherDrop || !(event.entity instanceof EntityChicken) || (!((EntityChicken)event.entity).isChild() && !ConfigurationHandler.chicksDropFeathers))
			return;
		
		chicken = (EntityChicken) event.entity;
		
		// Checking if drops contain feather(s)
		for (EntityItem item : event.drops) {
			if (item != null && item.getEntityItem().getItem().equals(Items.feather)) {
				setFeather = true;
				item.getEntityItem().stackSize = MathHelper.getRandomIntegerInRange(item.worldObj.rand, 1, 1);
			}
		}
		
		// Adding feathers if they don't exist
		if (!setFeather) {
			event.drops.add(new EntityItem(event.entity.worldObj, chicken.posX, chicken.posY, chicken.posZ, new ItemStack(Items.feather, MathHelper.getRandomIntegerInRange(event.entity.worldObj.rand, 1, 1))));
		}
	}
}
