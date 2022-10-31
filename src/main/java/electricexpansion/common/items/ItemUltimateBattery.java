package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.misc.EETab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.ItemElectric;

public class ItemUltimateBattery extends ItemElectric {
    public ItemUltimateBattery() {
        super();
        this.setUnlocalizedName("UltimateBattery");
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
    }

    public double getMaxJoules(final ItemStack i) {
        return 5000000.0;
    }

    public double getVoltage(final ItemStack i) {
        return 75.0;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister reg) {
        this.itemIcon = reg.registerIcon(
                this.getUnlocalizedName().replaceAll("item.", "electricexpansion:"));
    }
}
