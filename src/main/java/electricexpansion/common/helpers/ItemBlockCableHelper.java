package electricexpansion.common.helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.EnumWireMaterial;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import universalelectricity.core.electricity.ElectricityDisplay;

public abstract class ItemBlockCableHelper extends ItemBlock {
    protected HashMap<String, IIcon> icons;

    public ItemBlockCableHelper(final Block id) {
        super(id);
        this.icons = new HashMap<>();
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public int getMetadata(final int damage) {
        return damage;
    }

    public String getUnlocalizedName(final ItemStack itemStack) {
        return this.getUnlocalizedName() + "." +
                EnumWireMaterial.values()[itemStack.getItemDamage()].name;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemstack,
            final EntityPlayer player, final List par3List,
            final boolean par4) {
        par3List.add(
                "Resistance: " +
                        ElectricityDisplay.getDisplay(
                                EnumWireMaterial.values()[itemstack.getItemDamage()].resistance,
                                ElectricityDisplay.ElectricUnit.RESISTANCE));
        par3List.add(
                "Max Amps: " +
                        ElectricityDisplay.getDisplay(
                                EnumWireMaterial.values()[itemstack.getItemDamage()].maxAmps,
                                ElectricityDisplay.ElectricUnit.AMPERE));
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IconRegister) {
        if (this.getUnlocalizedName().equals("tile.HiddenWire") ||
                this.getUnlocalizedName().equals("tile.SwitchWireBlock")) {
            return;
        }
        for (int i = 0; i < EnumWireMaterial.values().length - 1; ++i) {
            this.icons.put(this.getUnlocalizedName(new ItemStack(this, 1, i)),
                    par1IconRegister.registerIcon(
                            this.getUnlocalizedName(new ItemStack(this, 1, i))
                                    .replaceAll("tile.", "electricexpansion:")));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int meta) {
        return this.icons.get(
                this.getUnlocalizedName(new ItemStack(this, 1, meta)));
    }
}
