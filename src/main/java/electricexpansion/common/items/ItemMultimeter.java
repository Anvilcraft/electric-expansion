package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.misc.EETab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.block.IVoltage;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ItemElectric;

public class ItemMultimeter extends ItemElectric {
    public final int JOULES_PER_USE = 5000;

    public ItemMultimeter() {
        super();
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
        this.setUnlocalizedName("Multimeter");
        this.setMaxDamage(200);
    }

    public boolean onItemUseFirst(final ItemStack stack,
            final EntityPlayer player, final World worldObj,
            final int x, final int y, final int z,
            final int side, final float hitX,
            final float hitY, final float hitZ) {
        if (worldObj.isRemote || !this.onUse(stack)) {
            return false;
        }
        final TileEntity tileEntity = worldObj.getTileEntity(x, y, z);
        if (tileEntity instanceof IConductor) {
            final IConductor wireTile = (IConductor) tileEntity;
            final ElectricityPack getProduced = wireTile.getNetwork().getProduced(new TileEntity[0]);
            player.addChatMessage(new ChatComponentText(
                    "Electric Expansion: " +
                            ElectricityDisplay.getDisplay(
                                    getProduced.amperes * 20.0, ElectricityDisplay.ElectricUnit.AMPERE)
                            +
                            ", " +
                            ElectricityDisplay.getDisplay(
                                    getProduced.voltage, ElectricityDisplay.ElectricUnit.VOLTAGE)
                            +
                            ", " +
                            ElectricityDisplay.getDisplay(getProduced.getWatts() * 20.0,
                                    ElectricityDisplay.ElectricUnit.WATT)));
            return true;
        }
        if (tileEntity instanceof IElectricityStorage) {
            final IElectricityStorage tileStorage = (IElectricityStorage) tileEntity;
            player.addChatMessage(new ChatComponentText(
                    "Electric Expansion: " +
                            ElectricityDisplay.getDisplay(
                                    tileStorage.getJoules(), ElectricityDisplay.ElectricUnit.JOULES)
                            +
                            "/" +
                            ElectricityDisplay.getDisplay(
                                    tileStorage.getMaxJoules(),
                                    ElectricityDisplay.ElectricUnit.JOULES)));
        }
        if (tileEntity instanceof IVoltage) {
            player.addChatMessage(
                    new ChatComponentText("Electric Expansion: " +
                            ElectricityDisplay.getDisplay(
                                    ((IVoltage) tileEntity).getVoltage(),
                                    ElectricityDisplay.ElectricUnit.VOLTAGE)));
        }
        return true;
    }

    private boolean onUse(final ItemStack itemStack) {
        final double joules = this.getJoules(itemStack);
        this.getClass();
        if (joules >= 5000.0) {
            final double joules2 = this.getJoules(itemStack);
            this.getClass();
            this.setJoules(joules2 - 5000.0, itemStack);
            return true;
        }
        return false;
    }

    public double getMaxJoules(final ItemStack itemStack) {
        return 1000000.0;
    }

    public double getVoltage(final ItemStack itemStack) {
        return 35.0;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister reg) {
        this.itemIcon = reg.registerIcon(
                this.getUnlocalizedName().replaceAll("item.", "electricexpansion:"));
    }

    @Override
    public void setJoules(final double joules, final ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        final double electricityStored = Math.max(Math.min(joules, this.getMaxJoules(itemStack)), 0.0);
        itemStack.getTagCompound().setDouble("electricity", electricityStored);
        itemStack.setItemDamage(
                (int) (this.getMaxDamage() - electricityStored /
                        this.getMaxJoules(itemStack) *
                        this.getMaxDamage()));
    }

    @Override
    public double getJoules(final ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            return 0.0;
        }
        final double electricityStored = itemStack.getTagCompound().getDouble("electricity");
        itemStack.setItemDamage(
                (int) (this.getMaxDamage() - electricityStored /
                        this.getMaxJoules(itemStack) *
                        this.getMaxDamage()));
        return electricityStored;
    }
}
