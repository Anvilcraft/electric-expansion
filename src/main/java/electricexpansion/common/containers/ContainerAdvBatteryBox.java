package electricexpansion.common.containers;

import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.modifier.SlotModifier;

public class ContainerAdvBatteryBox extends Container {
    private TileEntityAdvancedBatteryBox tileEntity;

    public ContainerAdvBatteryBox(final InventoryPlayer par1InventoryPlayer,
            final TileEntityAdvancedBatteryBox advBatteryBox) {
        this.tileEntity = advBatteryBox;
        this.addSlotToContainer((Slot) new SlotUniversalElectricItem(
                (IInventory) advBatteryBox, 0, 11, 24));
        this.addSlotToContainer((Slot) new SlotUniversalElectricItem(
                (IInventory) advBatteryBox, 1, 11, 48));
        this.addSlotToContainer((Slot)new SlotModifier((IInventory)advBatteryBox, 2, 149, 7)); this.addSlotToContainer((Slot)new SlotModifier((IInventory)advBatteryBox, 3, 149, 31));
        this.addSlotToContainer((Slot)new SlotModifier((IInventory)advBatteryBox, 4, 149, 55));
        this.addSlotToContainer(new Slot((IInventory) advBatteryBox, 2, 149, 7));
        this.addSlotToContainer(new Slot((IInventory) advBatteryBox, 3, 149, 31));
        this.addSlotToContainer(new Slot((IInventory) advBatteryBox, 4, 149, 55));
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
        this.tileEntity.openInventory();
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
        ItemStack var2 = null;
        final Slot var3 = (Slot) super.inventorySlots.get(par1);
        if (var3 != null && var3.getHasStack()) {
            final ItemStack var4 = var3.getStack();
            var2 = var4.copy();
            if (par1 > 4) {
                if (var4.getItem() instanceof IItemElectric) {
                    if (((IItemElectric) var4.getItem())
                            .getProvideRequest(var2)
                            .getWatts() > 0.0) {
                        if (!this.mergeItemStack(var4, 1, 2, false)) {
                            return null;
                        }
                    } else if (!this.mergeItemStack(var4, 0, 1, false)) {
                        return null;
                    }
                } else if (!this.mergeItemStack(var4, 2, 4, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(var4, 5, 38, false)) {
                return null;
            }
            if (var4.stackSize == 0) {
                var3.putStack((ItemStack) null);
            } else {
                var3.onSlotChanged();
            }
            if (var4.stackSize == var2.stackSize) {
                return null;
            }
            var3.onPickupFromSlot(par1EntityPlayer, var4);
        }
        return var2;
    }
}
