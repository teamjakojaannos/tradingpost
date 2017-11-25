package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class TRDBlocks {

	public static Block TEST_BLOCK = new BlockTest().setRegistryName(TradingPostMod.MODID, "testblock");
	public static Block BLOCK_TALL = new BlockTall().setRegistryName(TradingPostMod.MODID, "tallblock");

	public static Item ITEM_TALL = new ItemTall().setRegistryName(TradingPostMod.MODID, "tallblock");

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(TEST_BLOCK, BLOCK_TALL);
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(createItemBlockFor(TEST_BLOCK), ITEM_TALL);
	}

	private static Item createItemBlockFor(Block block) {
		return new ItemBlock(block).setRegistryName(block.getRegistryName());
	}

}
