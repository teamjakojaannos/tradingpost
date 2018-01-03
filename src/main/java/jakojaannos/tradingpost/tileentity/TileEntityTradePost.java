package jakojaannos.tradingpost.tileentity;

import jakojaannos.tradingpost.ModInfo;
import jakojaannos.tradingpost.inventory.ContainerTradePost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class TileEntityTradePost extends TileEntityLockable implements ITickable, ISidedInventory {

	private static final Logger LOGGER = LogManager.getLogger(ModInfo.MODID);

	// enumerate the slots
	public enum slotEnum {
		INPUT_SLOT, OUTPUT_SLOT
	}

	private static final int[] slotsTop = new int[] { slotEnum.INPUT_SLOT.ordinal() };
	private static final int[] slotsBottom = new int[] { slotEnum.OUTPUT_SLOT.ordinal() };
	private static final int[] slotsSides = new int[] {};
	private ItemStack[] itemsInSlots = new ItemStack[2];

	@Nonnull
	private String customName;

	public TileEntityTradePost() {
		customName = "";

		for (int i = 0; i < itemsInSlots.length; i++) {
			itemsInSlots[i] = ItemStack.EMPTY;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// Write items to a list
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < itemsInSlots.length; i++) {
			// Write the slot index and item stack for each slot
			NBTTagCompound slotCompound = new NBTTagCompound();
			slotCompound.setByte("Slot", (byte) i);
			itemsInSlots[i].writeToNBT(slotCompound);

			itemList.appendTag(slotCompound);
		}

		// Add item list to the compound
		compound.setTag("Items", itemList);

		// If we have a custom name, write it too
		if (hasCustomName()) {
			compound.setString("CustomName", customName);
		}

		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		// Read items
		NBTTagList itemList = compound.getTagList("Items", 10);
		Arrays.fill(itemsInSlots, ItemStack.EMPTY);

		for (int i = 0; i < itemList.tagCount(); i++) {
			NBTTagCompound slotCompound = itemList.getCompoundTagAt(i);
			int slot = slotCompound.getByte("Slot");

			if (slot >= 0 && slot < itemsInSlots.length) {
				itemsInSlots[slot].deserializeNBT(slotCompound);
			} else {
				LOGGER.warn("Invalid slot index while deserializing Trade Post items: {}", slot);
			}
		}

		// Read custom name
		customName = compound.getString("CustomName");
	}

	@Override
	public int getSizeInventory() {
		return itemsInSlots.length;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : itemsInSlots) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return itemsInSlots[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack;

		if (itemsInSlots[index].getCount() <= count) {
			itemstack = itemsInSlots[index];
			itemsInSlots[index] = ItemStack.EMPTY;
		} else {
			itemstack = itemsInSlots[index].splitStack(count);

			if (itemsInSlots[index].getCount() == 0) {
				itemsInSlots[index] = ItemStack.EMPTY;
			}
		}

		return itemstack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO:
		return itemsInSlots[index];
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// boolean isSameItemStackAlreadyInSlot = stack != null &&
		// stack.isItemEqual(grinderItemStackArray[index])
		// && ItemStack.areItemStackTagsEqual(stack,
		// grinderItemStackArray[index]);
		itemsInSlots[index] = stack;

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
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == slotEnum.INPUT_SLOT.ordinal();
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
		for (int i = 0; i < itemsInSlots.length; ++i) {
			itemsInSlots[i] = ItemStack.EMPTY;
		}
	}

	@Override
	public String getName() {
		return hasCustomName() ? customName : "container.post";
	}

	@Override
	public boolean hasCustomName() {
		return !customName.isEmpty();
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerTradePost(playerInventory, this);
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
	}
}
