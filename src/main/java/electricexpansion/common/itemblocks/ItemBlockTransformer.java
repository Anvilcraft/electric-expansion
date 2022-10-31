package electricexpansion.common.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTransformer extends ItemBlock {
    public ItemBlockTransformer(final Block par1) {
        super(par1);
        this.setHasSubtypes(true);
    }

    public int getMetadata(final int par1) {
        return par1;
    }

    public String getUnlocalizedName(final ItemStack i) {
        String name = null;
        final int j = i.getItemDamage();
        final int tier = j - (j & 0x3);
        if (tier == 0) {
            name = "2x";
        }
        if (tier == 4) {
            name = "4x";
        }
        if (tier == 8) {
            name = "8x";
        }
        return i.getItem().getUnlocalizedName() + "." + name;
    }
}
