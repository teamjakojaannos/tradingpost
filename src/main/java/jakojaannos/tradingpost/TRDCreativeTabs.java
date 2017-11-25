package jakojaannos.tradingpost;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class TRDCreativeTabs {

	public static final CreativeTabs DEVNULL = new CreativeTabs("dev/null") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.COOKIE);
		}
	};

}
