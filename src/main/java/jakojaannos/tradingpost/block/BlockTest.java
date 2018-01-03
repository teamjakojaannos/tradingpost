package jakojaannos.tradingpost.block;

import jakojaannos.tradingpost.init.TPostCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockTest extends Block {

	public BlockTest() {
		super(Material.ROCK);
		setUnlocalizedName("testBlock");
		setCreativeTab(TPostCreativeTabs.DEVNULL);
	}
}
