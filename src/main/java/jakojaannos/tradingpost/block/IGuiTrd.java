package jakojaannos.tradingpost.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class IGuiTrd implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity != null) {
			switch (GUI_IDS.values()[ID]) {
			case TRADING_POST:
				return new ContainerTrd(player.inventory, (IInventory) tileEntity);

			default:
				break;
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity != null) {
			switch (GUI_IDS.values()[ID]) {
			case TRADING_POST:
				return new GuiTrd(player.inventory, (IInventory) tileEntity);
			default:
				break;
			}

		}
		return null;
	}

	public enum GUI_IDS {
		TRADING_POST
	}

}
