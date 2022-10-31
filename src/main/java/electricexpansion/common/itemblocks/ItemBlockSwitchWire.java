package electricexpansion.common.itemblocks;

import electricexpansion.common.helpers.ItemBlockCableHelper;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemBlockSwitchWire extends ItemBlockCableHelper {
    public ItemBlockSwitchWire(Block block) {
        super(block);
    }

    @Override
    public void addInformation(final ItemStack par1ItemStack,
            final EntityPlayer par2EntityPlayer,
            final List par3List, final boolean par4) {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        par3List.add("Only conducts when redstone powered");
    }
}
