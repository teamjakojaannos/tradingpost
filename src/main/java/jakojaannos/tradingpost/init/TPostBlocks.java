package jakojaannos.tradingpost.init;

import jakojaannos.tradingpost.ModInfo;
import jakojaannos.tradingpost.TradingPostMod;
import jakojaannos.tradingpost.block.BlockTradingPost;
import jakojaannos.tradingpost.block.BlockTest;
import jakojaannos.tradingpost.item.ItemTradingPost;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber
@ObjectHolder(ModInfo.MODID)
public class TPostBlocks {
    private static final Logger LOGGER = LogManager.getLogger(ModInfo.MODID);

    public static final Block TEST_BLOCK = null;
    public static final Block TRADING_POST = null;


    /**
     * Registers blocks
     */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new BlockTest().setRegistryName(ModInfo.MODID, "test_block"),
                new BlockTradingPost().setRegistryName(ModInfo.MODID, "trading_post")
        );
    }

    /**
     * Registers ItemBlocks
     */
    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                createItemBlockFor(TEST_BLOCK),
                new ItemTradingPost().setRegistryName(ModInfo.MODID, "trading_post")
        );
    }

    /**
     * Registers ItemBlock models
     */
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerItemModels();
        registerBlockModels();
    }

    /**
     * Registers ItemBlock models for blocks with manually created item block
     */
    private static void registerItemModels() {
        registerItemModel(TPostItems.TRADING_POST);
    }

    /**
     * Registers ItemBlock models for blocks with automatically generated item block
     */
    private static void registerBlockModels() {
        registerItemModel(TEST_BLOCK);
    }


    private static Item createItemBlockFor(Block block) {
        return new ItemBlock(block).setRegistryName(block.getRegistryName());
    }

    private static void registerItemModel(Block block) {
        Item itemBlock = Item.getItemFromBlock(block);
        if (itemBlock == Items.AIR) {
            LOGGER.error(String.format(
                    "Tried to register item model for block %s (%s), but getItemFromBlock returned AIR!",
                    block.getUnlocalizedName(),
                    block.getLocalizedName()));
            return;
        }

        registerItemModel(itemBlock);
    }

    private static void registerItemModel(Item item) {
        TradingPostMod.proxy.registerItemRenderer(item, 0, item.getRegistryName());
    }
}
