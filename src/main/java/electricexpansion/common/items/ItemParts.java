package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.misc.EETab;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemParts extends Item {
    private static String[] names;
    private IIcon[] icons;

    public ItemParts() {
        super();
        this.icons = new IIcon[ItemParts.names.length];
        this.setHasSubtypes(true);
        this.setUnlocalizedName("Parts");
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int meta) {
        if (meta >= this.icons.length)
            return null;
        return this.icons[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IconRegister) {
        for (int i = 0; i < ItemParts.names.length; ++i) {
            this.icons[i] = par1IconRegister.registerIcon("electricexpansion:" +
                    ItemParts.names[i]);
        }
    }

    @Override
    public int getMetadata(final int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(final ItemStack i) {
        return i.getItem().getUnlocalizedName() + "." +
                ((i.getItemDamage() < ItemParts.names.length)
                        ? ItemParts.names[i.getItemDamage()]
                        : "Unknown");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs,
            final List par3List) {
        for (int var4 = 0; var4 < ItemParts.names.length; ++var4) {
            par3List.add(new ItemStack((Item) this, 1, var4));
        }
    }

    static {
        ItemParts.names = new String[] { "DrawPlates", "CondensedElectrumDust",
                "ElectrumIngot", "RawHVAlloy",
                "HVAlloyIngot", "CamoPaste",
                "Insulation", "LeadIngot",
                "Coil", "SilverIngot" };
    }
}
