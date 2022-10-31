package electricexpansion.common.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;

public class SlotUniversalElectricItem extends Slot {
    public SlotUniversalElectricItem(final IInventory par2IInventory,
            final int par3, final int par4,
            final int par5) {
        super(par2IInventory, par3, par4, par5);
    }

    public boolean isItemValid(final ItemStack par1ItemStack) {
        return par1ItemStack.getItem() instanceof IItemElectric;
    }
}
