package jakojaannos.tradingpost;

import jakojaannos.tradingpost.block.BlockTall;
import jakojaannos.tradingpost.block.BlockTest;
import jakojaannos.tradingpost.item.ItemTall;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber
public class TRDBlocks {
    private static final Logger logger = LogManager.getLogger("trading-post-blocks");

    public static Block TEST_BLOCK = new BlockTest().setRegistryName(TradingPostMod.MODID, "testblock");
    public static Block BLOCK_TALL = new BlockTall().setRegistryName(TradingPostMod.MODID, "tallblock");

    public static Item ITEM_TALL = new ItemTall().setRegistryName(TradingPostMod.MODID, "tallblock");


    /**
     * Registers blocks
     */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                TEST_BLOCK,
                BLOCK_TALL
        );
    }

    /**
     * Registers ItemBlocks
     */
    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                createItemBlockFor(TEST_BLOCK),
                ITEM_TALL
        );
    }

    /**
     * Registers ItemBlock models
     * TODO: Debug if this event handler crashes on the server
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
        registerItemModel(ITEM_TALL);
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
            logger.error(String.format(
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
