package mapwriter.forge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mapwriter.Mw;
import mapwriter.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.MOD_GUIFACTORY_CLASS, clientSideOnly = true, updateJSON = Reference.ForgeVersionURL, acceptedMinecraftVersions = Reference.ACCEPTED_VERSION)
public class MwForge
{

	@Instance(Reference.MOD_ID)
	public static MwForge instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS)
	public static CommonProxy proxy;

	public static Logger logger = LogManager.getLogger(Reference.MOD_ID);

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.load();
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START)
		{
			// run the cleanup code when Mw is loaded and the player becomes
			// null.
			// a bit hacky, but simpler than checking if the connection has
			// closed.
			if (Mw.getInstance().ready && Minecraft.getMinecraft().player == null)
			{
				Mw.getInstance().close();
			}
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
		proxy.preInit(event.getSuggestedConfigurationFile());
	}
}
