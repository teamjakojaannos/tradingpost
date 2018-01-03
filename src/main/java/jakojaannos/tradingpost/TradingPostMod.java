package jakojaannos.tradingpost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakojaannos.tradingpost.tileentity.TileEntityTradePost;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(name = ModInfo.NAME, modid = ModInfo.MODID, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCIES)
public class TradingPostMod {
	private static final Logger LOGGER = LogManager.getLogger("trading-post");

	@SidedProxy(clientSide = "jakojaannos.tradingpost.client.ClientProxy", serverSide = "jakojaannos.tradingpost.CommonProxy")
	public static CommonProxy proxy;

	@Instance(ModInfo.MODID)
	public static TradingPostMod instance;

	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityTradePost.class, ModInfo.MODID + ":tiletradingpost");
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new TPostGuiHandler());
	}
}
