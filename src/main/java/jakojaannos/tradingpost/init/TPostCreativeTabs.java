package jakojaannos.tradingpost.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class TPostCreativeTabs {

	public static final CreativeTabs DEVNULL = new CreativeTabs("devnull") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.COOKIE);
		}
	};

}
