package electricexpansion.common.containers;

import electricexpansion.common.tile.TileEntityQuantumBatteryBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDistribution extends Container {
    private TileEntityQuantumBatteryBox tileEntity;

    public ContainerDistribution(final InventoryPlayer par1InventoryPlayer,
            final TileEntityQuantumBatteryBox tileEntity2) {
        this.tileEntity = tileEntity2;
        // TODO: WTF
        //for (int var3 = 0; var3 < 3; ++var3) {
        //for (int var4 = 0; var4 < 9; ++var4) {
        //this.addSlotToContainer(new Slot((IInventory)
        //par1InventoryPlayer,
        //var4 + var3 * 9 + 9, 8 + var4 * 18,
        //84 + var3 * 18));
        //}
        //}
        //for (int var3 = 0; var3 < 9; ++var3) {
        //this.addSlotToContainer(
        //new Slot((IInventory) par1InventoryPlayer, var3, 8 + var3 *
        //18, 142));
        //}
        tileEntity2.openInventory();
    }

    public void onContainerClosed(final EntityPlayer entityplayer) {
        this.tileEntity.closeInventory();
    }

    public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
        return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
    }

    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer,
            final int par1) {
        return null;
    }
}
