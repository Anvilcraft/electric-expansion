package electricexpansion.common.containers;

import electricexpansion.common.misc.InsulationRecipes;
import electricexpansion.common.tile.TileEntityInsulatingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;

public class ContainerInsulationMachine extends Container {
    private TileEntityInsulatingMachine tileEntity;

    public ContainerInsulationMachine(final InventoryPlayer par1InventoryPlayer,
            final TileEntityInsulatingMachine tileEntity) {
        this.tileEntity = tileEntity;
        this.addSlotToContainer((Slot) new SlotUniversalElectricItem(
                (IInventory) tileEntity, 0, 55, 49));
        this.addSlotToContainer(new Slot((IInventory) tileEntity, 1, 55, 25));
        this.addSlotToContainer((Slot) new SlotSpecific(
                (IInventory) tileEntity, 2, 108, 25, new ItemStack[] { null }));
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
        ItemStack var2 = null;
        final Slot var3 = (Slot) super.inventorySlots.get(par1);
        if (var3 != null && var3.getHasStack()) {
            final ItemStack var4 = var3.getStack();
            var2 = var4.copy();
            if (par1 == 2) {
                if (!this.mergeItemStack(var4, 3, 39, true)) {
                    return null;
                }
                var3.onSlotChange(var4, var2);
            } else if (par1 != 1 && par1 != 0) {
                if (var4.getItem() instanceof IItemElectric) {
                    if (!this.mergeItemStack(var4, 0, 1, false)) {
                        return null;
                    }
                } else if (InsulationRecipes.INSTANCE.getProcessResult(var4) > 0) {
                    if (!this.mergeItemStack(var4, 1, 2, false)) {
                        return null;
                    }
                } else if (par1 >= 3 && par1 < 30) {
                    if (!this.mergeItemStack(var4, 30, 39, false)) {
                        return null;
                    }
                } else if (par1 >= 30 && par1 < 39 &&
                        !this.mergeItemStack(var4, 3, 30, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(var4, 3, 39, false)) {
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
