package vazkii.chickenshed;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import vazkii.chickenshed.handler.ConfigurationHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(
		modid = "ChickenShed",
		name = "Chicken Shed",
		version = "1.1.4"
	)
public class ChickenShed {

	@Instance("ChickenShed")
	public static ChickenShed instance;
	
	@EventHandler
	public void initPre(FMLPreInitializationEvent event) {
		// Initiating configuration
		ConfigurationHandler.initConfig(event.getSuggestedConfigurationFile());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (ConfigurationHandler.isEnabled)
			MinecraftForge.EVENT_BUS.register(this);
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
