package jakojaannos.tradingpost;

import jakojaannos.tradingpost.client.gui.inventory.GuiTradePost;
import jakojaannos.tradingpost.inventory.ContainerTradePost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class TPostGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if (tileEntity != null) {
            switch (EGuiIds.values()[id]) {
                case TRADING_POST:
                    return new ContainerTradePost(player.inventory, (IInventory) tileEntity);

                default:
                    break;
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if (tileEntity != null) {
            switch (EGuiIds.values()[id]) {
                case TRADING_POST:
                    return new GuiTradePost(player.inventory, (IInventory) tileEntity);
                default:
                    break;
            }

        }
        return null;
    }

    public enum EGuiIds {
        TRADING_POST
    }
}
