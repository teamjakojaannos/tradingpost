package jakojaannos.tradingpost.client;

import jakojaannos.tradingpost.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    /**
     * Registers item renderer for given item.
     */
    @Override
    public void registerItemRenderer(Item item, int meta, ResourceLocation registryName) {
        ModelLoader.setCustomModelResourceLocation(
                item,
                meta,
                new ModelResourceLocation(registryName, "inventory")
        );
    }
}
