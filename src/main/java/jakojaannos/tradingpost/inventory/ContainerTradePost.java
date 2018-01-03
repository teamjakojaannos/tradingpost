package jakojaannos.tradingpost.inventory;

import jakojaannos.tradingpost.tileentity.TileEntityTradePost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTradePost extends Container {

    private final IInventory tileTradePost;
    private final int sizeInventory;

    public ContainerTradePost(InventoryPlayer playerInventory, IInventory inventory) {
        tileTradePost = inventory;
        sizeInventory = tileTradePost.getSizeInventory();

        // Add player inventory slots
        for (int row = 0; row < 4; ++row) {
            for (int column = 0; column < 9; ++column) {
                final int slotIndex = column + row * 9;
                // The first row (the first 9 slots) is the hotbar
                if (slotIndex < 9) {
                    addSlotToContainer(new Slot(playerInventory, slotIndex, 8 + column * 18, 142));
                }
                // The rest are the 3x9 inventory
                else {
                    addSlotToContainer(new Slot(playerInventory, slotIndex, 8 + column * 18, 84 + (row - 1) * 18));
                }
            }
        }

        // Add tile-entity slots
        addSlotToContainer(new Slot(tileTradePost, TileEntityTradePost.ESlots.INPUT_1.ordinal(), 36, 53));
        addSlotToContainer(new Slot(tileTradePost, TileEntityTradePost.ESlots.INPUT_2.ordinal(), 62, 53));

        addSlotToContainer(new Slot(tileTradePost, TileEntityTradePost.ESlots.OUTPUT.ordinal(), 120, 53) {
            // Prevent placing items in the output slot
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileTradePost.isUsableByPlayer(playerIn);
    }

    /**
     * Moves items around when shift-clicked
     * <p>
     * Slot indices:
     * 0-35: Player inventory
     * - 0-8:   hotbar
     * - 9-35:  inventory
     * 36-37: tile-entity
     * - 36-37:	input
     * - 38:	output
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        final Slot clickedSlot = getSlot(slotIndex);

        // Don't move anything if there is nothing to move
        if (!clickedSlot.getHasStack()) {
            return ItemStack.EMPTY;
        }

        final ItemStack clickedStack = clickedSlot.getStack();

        // If slot in player inventory was clicked
        if (slotIndex < 36) {
            // Try to merge with input slots
            if (!mergeItemStack(clickedStack, 36, 38, false)) {
                return ItemStack.EMPTY;
            }
        }
        // Slot in tile-entity inventory was clicked
        else {
            // Try to merge with player inventory. Hotbar slots are first so they are tested first, allowing us to use
            // a single mergeItemStack call for whole inventory.
            if (!mergeItemStack(clickedStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        }

        // None of the above returned ItemStack.EMPTY, which means that the stack was successfully transferred
        // Now, if stack is empty, we know that the whole stack was moved and we need to mark the source slot empty
        if (clickedStack.isEmpty()) {
            clickedSlot.putStack(ItemStack.EMPTY);
        }
        // Stack is not empty, it was transferred only partially. Notify listeners of that change.
        else {
            clickedSlot.onSlotChanged();
        }

        clickedSlot.onTake(player, clickedStack);
        return clickedStack.copy();
    }
}
