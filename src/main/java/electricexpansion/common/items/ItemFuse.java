package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.IItemFuse;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemFuse extends Item implements IItemFuse {
    private IIcon[] icons;

    public ItemFuse() {
        super();
        this.icons = new IIcon[16];
        this.setHasSubtypes(true);
        this.setUnlocalizedName("fuse");
    }

    @Override
    public double getMaxVolts(final ItemStack itemStack) {
        switch (itemStack.getItemDamage()) {
            case 0:
            case 1: {
                return 60.0;
            }
            case 2:
            case 3: {
                return 120.0;
            }
            case 4:
            case 5: {
                return 240.0;
            }
            case 6:
            case 7: {
                return 480.0;
            }
            case 8:
            case 9: {
                return 60.0;
            }
            case 10:
            case 11: {
                return 120.0;
            }
            case 12:
            case 13: {
                return 240.0;
            }
            case 14:
            case 15: {
                return 480.0;
            }
            default: {
                return 0.0;
            }
        }
    }

    @Override
    public ItemStack onFuseTrip(final ItemStack itemStack) {
        final ItemStack toReturn = itemStack.copy();
        toReturn.setItemDamage(itemStack.getItemDamage() + 1);
        return toReturn;
    }

    @Override
    public boolean isValidFuse(final ItemStack itemStack) {
        return itemStack.getItemDamage() % 2 == 0;
    }

    @Override
    public boolean canReset(final ItemStack itemStack) {
        return itemStack.getItemDamage() / 8 == 1;
    }

    @Override
    public ItemStack onReset(final ItemStack itemStack) {
        if (this.canReset(itemStack)) {
            final ItemStack toReturn = itemStack.copy();
            toReturn.setItemDamage(itemStack.getItemDamage() - 1);
            return toReturn;
        }
        return null;
    }

    @Override
    public String getUnlocalizedName(final ItemStack itemStack) {
        final double volts = this.getMaxVolts(itemStack);
        final String type = this.canReset(itemStack)
                ? (this.isValidFuse(itemStack) ? "+cb" : "-cb")
                : (this.isValidFuse(itemStack) ? "+f" : "-f");
        return this.getUnlocalizedName() + "." + type + "." + (int) volts;
    }

    @Override
    public IIcon getIconFromDamage(final int meta) {
        if (meta >= this.icons.length)
            return null;
        return this.icons[meta];
    }

    @Override
    public int getMetadata(final int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs,
            final List par3List) {
        for (int var4 = 0; var4 < this.icons.length; ++var4) {
            par3List.add(new ItemStack((Item) this, 1, var4));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IconRegister) {
        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = par1IconRegister.registerIcon(
                    this.getUnlocalizedName(new ItemStack(this, 0, i))
                            .replaceAll("item.", "electricexpansion:"));
        }
    }
}
