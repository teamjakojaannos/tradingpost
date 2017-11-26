package jakojaannos.tradingpost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.DependsOn;

@Mod(name = TradingPostMod.NAME, modid = TradingPostMod.MODID, version = TradingPostMod.VERSION)
@DependsOn(TradingPostMod.MODID)
public class TradingPostMod {
	private static final Logger logger = LogManager.getLogger("trading-post-main");
	
	public static final String NAME = "Trading Posts";
    public static final String MODID = "tradepost";
    public static final String VERSION = "0.0.0";
    
	
	@SidedProxy(clientSide = "jakojaannos.tradingpost.client.ClientProxy", serverSide = "jakojaannos.tradingpost.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance(MODID)
	public static TradingPostMod instance;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// some example code
		System.out.println("DIRT BLOCK >> " + Blocks.DIRT.getUnlocalizedName());
	}
}
