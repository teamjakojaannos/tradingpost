package jakojaannos.tradingpost.tileentity;

import jakojaannos.tradingpost.ModInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class TileEntityTradePost extends TileEntity implements ITickable, IInventory {

    private static final Logger LOGGER = LogManager.getLogger(ModInfo.MODID);

    public enum ESlots {
        INPUT_1, INPUT_2, OUTPUT
    }

    private ItemStack[] itemsInSlots = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};

    @Nonnull
    private String customName = "";

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

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

        return compound;
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
                itemsInSlots[slot] = new ItemStack(slotCompound);
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

        markDirty();
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        // TODO:
        return itemsInSlots[index];
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        itemsInSlots[index] = stack;

        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        markDirty();
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
        return index == ESlots.INPUT_1.ordinal();
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
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
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
    public void update() {
    }


    // Network sync

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound updateTag = getUpdateTag();
        return new SPacketUpdateTileEntity(pos, 0, updateTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound updateTag = pkt.getNbtCompound();
        handleUpdateTag(updateTag);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound updateTag = new NBTTagCompound();
        writeToNBT(updateTag);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound updateTag) {
        readFromNBT(updateTag);
    }
}
