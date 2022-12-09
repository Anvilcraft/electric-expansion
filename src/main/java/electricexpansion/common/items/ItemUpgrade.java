package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.misc.EETab;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.prefab.modifier.IModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class ItemUpgrade extends Item implements IModifier {
    private String[] names;
    private IIcon[] icons;
    @SideOnly(Side.CLIENT)
    private IIcon defaultIcon;

    public ItemUpgrade(final int texture) {
        super();
        this.names = new String[] { "Storage1", "Storage2", "Storage3",
                "Storage4", "HalfVoltage", "HVUpgrade",
                "HVInputUpgrade", "DoubleVoltage", "Unlimiter1",
                "Unlimiter2", "Unlimiter3", "Unlimiter4" };
        this.icons = new IIcon[this.names.length];
        this.setMaxDamage(0);
        this.setMaxStackSize(16);
        this.setHasSubtypes(true);
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
        this.setUnlocalizedName("Upgrade");
    }

    @Override
    public int getMetadata(final int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(final ItemStack itemstack) {
        return this.getUnlocalizedName() + "." +
                this.names[itemstack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int i) {
        if (i <= this.icons.length) {
            return this.icons[i];
        }
        return this.defaultIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs,
            final List par3List) {
        for (int i = 0; i < this.names.length; ++i) {
            par3List.add(new ItemStack((Item) this, 1, i));
        }
    }

    @Override
    public String getType(final ItemStack itemstack) {
        switch (itemstack.getItemDamage()) {
            case 0:
            case 1:
            case 2:
            case 3: {
                return "Capacity";
            }
            case 4:
            case 5: {
                return "VoltageModifier";
            }
            case 6: {
                return "InputVoltageModifier";
            }
            case 7: {
                return "VoltageModifier";
            }
            case 8:
            case 9:
            case 10:
            case 11: {
                return "Unlimiter";
            }
            default: {
                return "Unknown";
            }
        }
    }

    @Override
    public double getEffectiveness(final ItemStack itemstack) {
        switch (itemstack.getItemDamage()) {
            case 0: {
                return 1000000.0;
            }
            case 1: {
                return 2000000.0;
            }
            case 2: {
                return 3000000.0;
            }
            case 3: {
                return 5000000.0;
            }
            case 4: {
                return 0.5;
            }
            case 5: {
                return 20.0;
            }
            case 6: {
                return 20.0;
            }
            case 7: {
                return 2.0;
            }
            case 8: {
                return 5.0;
            }
            case 9: {
                return 10.0;
            }
            case 10: {
                return 20.0;
            }
            case 11: {
                return 40.0;
            }
            default: {
                return 0.0;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemstack,
            final EntityPlayer player, final List par3List,
            final boolean par4) {
        String strength = "";
        final double effectiveness = this.getEffectiveness(itemstack);
        if (this.getType(itemstack).equals("Capacity")) {
            strength = UnitDisplay.getDisplay(this.getEffectiveness(itemstack),
                    UnitDisplay.Unit.JOULES);
        } else if (effectiveness < 0.0) {
            strength = "1/" + String.valueOf(effectiveness * -1.0);
        } else {
            strength = effectiveness + "";
        }
        par3List.add("§2" + StatCollector
                .translateToLocal("upgrades.description." +
                        this.getType(itemstack))
                .replaceAll("<>", strength));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        for (int i = 0; i < this.names.length; ++i) {
            this.icons[i] = iconRegister.registerIcon("electricexpansion:" + this.names[i]);
        }
    }

    @Override
    public int getTier(final ItemStack i) {
        switch (i.getItemDamage()) {
            case 0: {
                return 0;
            }
            case 1: {
                return 1;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 3;
            }
            case 4: {
                return 0;
            }
            case 5: {
                return 1;
            }
            case 6: {
                return 1;
            }
            case 7: {
                return 0;
            }
            case 8: {
                return 0;
            }
            case 9: {
                return 1;
            }
            case 10: {
                return 2;
            }
            case 11: {
                return 3;
            }
            default: {
                return -1;
            }
        }
    }
}
