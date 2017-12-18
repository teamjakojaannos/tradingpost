package jakojaannos.tradingpost;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
    /**
     * Registers item renderer for given item.
     * Does nothing on the server.
     */
    public void registerItemRenderer(Item item, int meta, ResourceLocation registryName) {
        // NO-OP
    }
}
