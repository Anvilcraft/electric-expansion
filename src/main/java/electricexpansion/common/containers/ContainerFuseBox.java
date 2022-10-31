package electricexpansion.common.containers;

import electricexpansion.api.IItemFuse;
import electricexpansion.common.tile.TileEntityFuseBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.SlotSpecific;

public class ContainerFuseBox extends Container {
    private TileEntityFuseBox tileEntity;

    public ContainerFuseBox(final InventoryPlayer par1InventoryPlayer,
            final TileEntityFuseBox tileEntity) {
        this.tileEntity = tileEntity;
        this.addSlotToContainer((Slot) new SlotSpecific(
                (IInventory) tileEntity, 0, 8, 16, new Class[] { IItemFuse.class }));
        for (int var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 9; ++var4) {
                this.addSlotToContainer(new Slot((IInventory) par1InventoryPlayer,
                        var4 + var3 * 9 + 9, 8 + var4 * 18,
                        84 + var3 * 18));
            }
        }
        for (int var3 = 0; var3 < 9; ++var3) {
            this.addSlotToContainer(
                    new Slot((IInventory) par1InventoryPlayer, var3, 8 + var3 * 18, 142));
        }
        tileEntity.openInventory();
    }

    public void onContainerClosed(final EntityPlayer entityplayer) {
        super.onContainerClosed(entityplayer);
        this.tileEntity.closeInventory();
    }

    public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
        return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
    }

    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer,
            final int par1) {
        System.out.println("Slot: " + par1);
        ItemStack var2 = null;
        ItemStack var3 = null;
        ItemStack var4 = null;
        final Slot var5 = (Slot) super.inventorySlots.get(par1);
        final Slot var6 = (Slot) super.inventorySlots.get(0);
        if (var5 != null && var5.getHasStack()) {
            var2 = var5.getStack();
            var3 = var2.copy();
            var4 = var2.copy();
            --var4.stackSize;
            var3.stackSize = 1;
            System.out.println("StackSize: " + var4.stackSize);
            if (par1 == 0) {
                if (!this.mergeItemStack(var3, 1, 37, true)) {
                    return var4;
                }
                var5.onSlotChange(var3, var2);
            } else if (par1 != 0 && !var6.getHasStack()) {
                if (var3.getItem() instanceof IItemFuse) {
                    if (!this.mergeItemStack(var3, 0, 1, false)) {
                        return var4;
                    }
                } else if (par1 >= 28 && par1 < 37 &&
                        !this.mergeItemStack(var3, 1, 28, false)) {
                    return var4;
                }
            } else if (!this.mergeItemStack(var3, 1, 37, false)) {
                return var4;
            }
            if (var4.stackSize == 0) {
                var5.putStack((ItemStack) null);
            } else {
                var5.onSlotChanged();
            }
            if (var3.stackSize == var2.stackSize) {
                return null;
            }
            var5.onPickupFromSlot(par1EntityPlayer, var2);
        }
        return var2;
    }
}
