package jakojaannos.tradingpost.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerTrd extends Container {

	private final IInventory tileGrinder;
	private final int sizeInventory;
	// private int ticksGrindingItemSoFar;
	// private int ticksPerItem;
	// private int timeCanGrind;

	public ContainerTrd(InventoryPlayer parInventoryPlayer, IInventory parIInventory) {
		tileGrinder = parIInventory;
		sizeInventory = tileGrinder.getSizeInventory();
		addSlotToContainer(new Slot(tileGrinder, TileEntityTrd.slotEnum.INPUT_SLOT.ordinal(), 56, 35));
		addSlotToContainer(new Slot(tileGrinder, TileEntityTrd.slotEnum.OUTPUT_SLOT.ordinal(), 116, 35));

		// add player inventory slots
		// note that the slot numbers are within the player inventory so can
		// be same as the tile entity inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(parInventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		// add hotbar slots
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(parInventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	// @Override
	// public void addCraftingToCrafters(ICrafting listener) {
	// super.addCraftingToCrafters(listener);
	// listener.func_175173_a(this, tileGrinder);
	// }

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		// tileGrinder.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileGrinder.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {
		ItemStack itemStack1 = ItemStack.EMPTY;
		Slot slot = (Slot) inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack1 = itemStack2.copy();

			if (slotIndex == TileEntityTrd.slotEnum.OUTPUT_SLOT.ordinal()) {
				if (!mergeItemStack(itemStack2, sizeInventory, sizeInventory + 36, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemStack2, itemStack1);
			} else if (slotIndex != TileEntityTrd.slotEnum.INPUT_SLOT.ordinal()) {
				// check if there is a grinding recipe for the stack
				// if (GrinderRecipes.instance().getGrindingResult(itemStack2)
				// != null) {
				// if (!mergeItemStack(itemStack2, 0, 1, false)) {
				// return null;
				// }
				// } else

				if (slotIndex >= sizeInventory && slotIndex < sizeInventory + 27) {
					// player
					// inventory
					// slots
					if (!mergeItemStack(itemStack2, sizeInventory + 27, sizeInventory + 36, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotIndex >= sizeInventory + 27 && slotIndex < sizeInventory + 36
						&& !mergeItemStack(itemStack2, sizeInventory + 1, sizeInventory + 27, false)) // hotbar
				// slots
				{
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemStack2, sizeInventory, sizeInventory + 36, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemStack2.getCount() == itemStack1.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemStack2);
		}

		return itemStack1;
	}

}
