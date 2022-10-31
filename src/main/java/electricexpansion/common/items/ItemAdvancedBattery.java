package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.misc.EETab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.ItemElectric;

public class ItemAdvancedBattery extends ItemElectric {
    public ItemAdvancedBattery() {
        super();
        this.setUnlocalizedName("AdvancedBattery");
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
    }

    public double getMaxJoules(final ItemStack itemStack) {
        return 2000000.0;
    }

    public double getVoltage(final ItemStack itemStack) {
        return 30.0;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(
                this.getUnlocalizedName().replaceAll("item.", "electricexpansion:"));
    }
}
