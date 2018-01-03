package jakojaannos.tradingpost.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityTrd extends TileEntityLockable implements ITickable, ISidedInventory {

	// enumerate the slots
	public enum slotEnum {
		INPUT_SLOT, OUTPUT_SLOT
	}

	private static final int[] slotsTop = new int[] { slotEnum.INPUT_SLOT.ordinal() };
	private static final int[] slotsBottom = new int[] { slotEnum.OUTPUT_SLOT.ordinal() };
	private static final int[] slotsSides = new int[] {};
	private ItemStack[] postItemStackArray = new ItemStack[2];
	private String postCustomName;

	public TileEntityTrd() {
		for (int i = 0; i < postItemStackArray.length; i++) {
			postItemStackArray[i] = new ItemStack(Blocks.AIR);
		}
	}

	@Override
	public int getSizeInventory() {
		return postItemStackArray.length;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : postItemStackArray) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return postItemStackArray[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (postItemStackArray[index] != null) {
			ItemStack itemstack;

			if (postItemStackArray[index].getCount() <= count) {
				itemstack = postItemStackArray[index];
				postItemStackArray[index] = null;
				return itemstack;
			} else {
				itemstack = postItemStackArray[index].splitStack(count);

				if (postItemStackArray[index].getCount() == 0) {
					postItemStackArray[index] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO:
		return postItemStackArray[index];
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// boolean isSameItemStackAlreadyInSlot = stack != null &&
		// stack.isItemEqual(grinderItemStackArray[index])
		// && ItemStack.areItemStackTagsEqual(stack,
		// grinderItemStackArray[index]);
		postItemStackArray[index] = stack;

		if (stack != null && stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}

		// if input slot, reset the grinding timers
		// if (index == slotEnum.INPUT_SLOT.ordinal() &&
		// !isSameItemStackAlreadyInSlot) {
		// ticksPerItem = timeToGrindOneItem(stack);
		// ticksGrindingItemSoFar = 0;
		// markDirty();
		// }
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) != this ? false
				: player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == slotEnum.INPUT_SLOT.ordinal() ? true : false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < postItemStackArray.length; ++i) {
			postItemStackArray[i] = null;
		}
	}

	@Override
	public String getName() {
		return hasCustomName() ? postCustomName : "container.post";
	}

	@Override
	public boolean hasCustomName() {
		return postCustomName != null && postCustomName.length() > 0;
	}

	public void setCustomInventoryName(String parCustomName) {
		postCustomName = parCustomName;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerTrd(playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return "tradingpost:tall";
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int parSlotIndex, ItemStack parStack, EnumFacing parFacing) {
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
