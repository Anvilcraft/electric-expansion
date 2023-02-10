package electricexpansion.common.tile;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import electricexpansion.api.IBatteryBoxPreipheral;

import java.util.EnumSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.modifier.IModifier;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;

public class TileEntityAdvancedBatteryBox extends TileEntityElectricityStorage
        implements IRedstoneProvider, ISidedInventory, IBatteryBoxPreipheral {
    private ItemStack[] containingItems;
    private int playersUsing;

    public TileEntityAdvancedBatteryBox() {
        this.containingItems = new ItemStack[5];
        this.playersUsing = 0;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.isDisabled()) {
            if (this.containingItems[0] != null && this.getJoules() > 0.0) {
                if (this.containingItems[0].getItem() instanceof IItemElectric) {
                    this.setJoules(this.getJoules() -
                            ElectricItemHelper.chargeItem(this.containingItems[0],
                                    this.getJoules(),
                                    this.getVoltage()));
                }
            }
            if (this.containingItems[1] != null &&
                    this.getJoules() < this.getMaxJoules()) {
                if (this.containingItems[1].getItem() instanceof IItemElectric) {
                    final IItemElectric electricItem = (IItemElectric) this.containingItems[1].getItem();
                    if (electricItem.getProvideRequest(this.containingItems[1])
                            .getWatts() > 0.0) {
                        final ElectricityPack packRecieved = electricItem.onProvide(
                                ElectricityPack.getFromWatts(
                                        this.getMaxJoules() - this.getJoules(), this.getVoltage()),
                                this.containingItems[1]);
                        this.setJoules(this.getJoules() + packRecieved.getWatts());
                    }
                }
            }
            final ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
            final TileEntity outputTile = VectorHelper.getTileEntityFromSide(
                    this.getWorldObj(), new Vector3(this), outputDirection);
            final TileEntity inputTile = VectorHelper.getTileEntityFromSide(
                    this.getWorldObj(), new Vector3(this), outputDirection.getOpposite());
            final IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(
                    inputTile, outputDirection.getOpposite());
            final IElectricityNetwork outputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile,
                    outputDirection);
            if (outputNetwork != null && inputNetwork != outputNetwork) {
                final ElectricityPack actualOutput = new ElectricityPack(
                        Math.min(
                                outputNetwork.getLowestCurrentCapacity(),
                                Math.min(
                                        this.getOutputCap(),
                                        outputNetwork.getRequest(new TileEntity[0]).getWatts()) /
                                        this.getVoltage()),
                        this.getVoltage());
                if (this.getJoules() > 0.0 && actualOutput.getWatts() > 0.0) {
                    outputNetwork.startProducing(this, actualOutput);
                    this.setJoules(this.getJoules() - actualOutput.getWatts());
                } else {
                    outputNetwork.stopProducing(this);
                }
            }
        }
        if (!this.getWorldObj().isRemote && super.ticks % 3L == 0L &&
                this.playersUsing > 0) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public ElectricityPack getRequest() {
        return new ElectricityPack(
                Math.min((this.getMaxJoules() - this.getJoules()) / this.getVoltage(),
                        this.getOutputCap() / 2.0),
                this.getVoltage());
    }

    @Override
    protected EnumSet<ForgeDirection> getConsumingSides() {
        return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)
                .getOpposite());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setDouble("joules", this.getJoules());
        nbt.setInteger("disabledTicks", super.disabledTicks);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
                this.getBlockMetadata(), nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.setJoules(nbt.getDouble("joules"));
        super.disabledTicks = nbt.getInteger("disabledTicks");
    }

    @Override
    public void openInventory() {
        ++this.playersUsing;
    }

    @Override
    public void closeInventory() {
        --this.playersUsing;
    }

    @Override
    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            final NBTTagCompound var4 = (NBTTagCompound) var2.getCompoundTagAt(var3);
            final byte var5 = var4.getByte("Slot");
            if (var5 >= 0 && var5 < this.containingItems.length) {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        final NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.containingItems.length; ++var3) {
            if (this.containingItems[var3] != null) {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                var2.appendTag((NBTBase) var4);
            }
        }
        par1NBTTagCompound.setTag("Items", (NBTBase) var2);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int orientation) {
        ForgeDirection side = ForgeDirection.getOrientation(orientation);

        if (side == ForgeDirection.DOWN) {
            return new int[] { 1 };
        }
        if (side == ForgeDirection.UP) {
            return new int[] { 1 };
        }
        return new int[] { 0 };
    }

    @Override
    public int getSizeInventory() {
        return this.containingItems.length;
    }

    @Override
    public ItemStack getStackInSlot(final int par1) {
        return this.containingItems[par1];
    }

    @Override
    public ItemStack decrStackSize(final int par1, final int par2) {
        if (this.containingItems[par1] == null) {
            return null;
        }
        if (this.containingItems[par1].stackSize <= par2) {
            final ItemStack var3 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var3;
        }
        final ItemStack var3 = this.containingItems[par1].splitStack(par2);
        if (this.containingItems[par1].stackSize == 0) {
            this.containingItems[par1] = null;
        }
        return var3;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int par1) {
        if (this.containingItems[par1] != null) {
            final ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(final int par1,
            final ItemStack par2ItemStack) {
        this.containingItems[par1] = par2ItemStack;
        if (par2ItemStack != null &&
                par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return StatCollector.translateToLocal("tile.advbatbox.name");
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public double getMaxJoules() {
        double slot1 = 0.0;
        double slot2 = 0.0;
        double slot3 = 0.0;
        if (this.containingItems[2] != null &&
                this.containingItems[2].getItem() instanceof IModifier &&
                ((IModifier) this.containingItems[2].getItem())
                        .getType(this.containingItems[2]) == "Capacity") {
            slot1 = ((IModifier) this.containingItems[2].getItem())
                    .getEffectiveness(this.containingItems[2]);
        }
        if (this.containingItems[3] != null &&
                this.containingItems[3].getItem() instanceof IModifier &&
                ((IModifier) this.containingItems[3].getItem())
                        .getType(this.containingItems[3]) == "Capacity") {
            slot2 = ((IModifier) this.containingItems[3].getItem())
                    .getEffectiveness(this.containingItems[3]);
        }
        if (this.containingItems[4] != null &&
                this.containingItems[4].getItem() instanceof IModifier &&
                ((IModifier) this.containingItems[4].getItem())
                        .getType(this.containingItems[4]) == "Capacity") {
            slot3 = ((IModifier) this.containingItems[4].getItem())
                    .getEffectiveness(this.containingItems[4]);
        }
        return 5000000.0 + slot1 + slot2 + slot3;
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer par1EntityPlayer) {
        return this.getWorldObj().getTileEntity(this.xCoord, this.yCoord,
                this.zCoord) == this;
    }

    @Override
    public boolean isPoweringTo(final ForgeDirection side) {
        return this.getJoules() >= this.getMaxJoules();
    }

    @Override
    public boolean isIndirectlyPoweringTo(final ForgeDirection side) {
        return this.isPoweringTo(side);
    }

    @Override
    public double getVoltage() {
        return 120.0 * this.getVoltageModifier("VoltageModifier");
    }

    public double getInputVoltage() {
        return Math.max(
                this.getVoltage(),
                Math.max(120.0, this.getVoltageModifier("InputVoltageModifier") *
                        this.getVoltageModifier("VoltageModifier") *
                        120.0));
    }

    @Override
    public void onReceive(final ElectricityPack electricityPack) {
        if (UniversalElectricity.isVoltageSensitive &&
                electricityPack.voltage > this.getInputVoltage()) {
            this.getWorldObj().createExplosion((Entity) null, (double) this.xCoord,
                    (double) this.yCoord,
                    (double) this.zCoord, 1.5f, true);
            return;
        }
        this.setJoules(this.getJoules() + electricityPack.getWatts());
    }

    private double getVoltageModifier(final String type) {
        double slot1 = 1.0;
        double slot2 = 1.0;
        double slot3 = 1.0;
        if (this.containingItems[2] != null &&
                this.containingItems[2].getItem() instanceof IModifier &&
                ((IModifier) this.containingItems[2].getItem())
                        .getType(this.containingItems[2]) == type) {
            slot1 = ((IModifier) this.containingItems[2].getItem())
                    .getEffectiveness(this.containingItems[2]);
        }
        if (this.containingItems[3] != null &&
                this.containingItems[3].getItem() instanceof IModifier &&
                ((IModifier) this.containingItems[3].getItem())
                        .getType(this.containingItems[3]) == type) {
            slot2 = ((IModifier) this.containingItems[3].getItem())
                    .getEffectiveness(this.containingItems[3]);
        }
        if (this.containingItems[4] != null &&
                this.containingItems[4].getItem() instanceof IModifier &&
                ((IModifier) this.containingItems[4].getItem())
                        .getType(this.containingItems[4]) == type) {
            slot3 = ((IModifier) this.containingItems[4].getItem())
                    .getEffectiveness(this.containingItems[4]);
        }
        return slot1 * slot2 * slot3;
    }

    private double getOutputCap() {
        double slot1 = 0.0;
        double slot2 = 0.0;
        double slot3 = 0.0;
        if (this.containingItems[2] != null &&
                this.containingItems[2].getItem() instanceof IModifier &&
                ((IModifier) this.containingItems[2].getItem())
                        .getType(this.containingItems[2]) == "Unlimiter") {
            slot1 = ((IModifier) this.containingItems[2].getItem())
                    .getEffectiveness(this.containingItems[2]);
        }
        if (this.containingItems[3] != null &&
                this.containingItems[3].getItem() instanceof IModifier &&
                ((IModifier) this.containingItems[3].getItem())
                        .getType(this.containingItems[3]) == "Unlimiter") {
            slot2 = ((IModifier) this.containingItems[3].getItem())
                    .getEffectiveness(this.containingItems[3]);
        }
        if (this.containingItems[4] != null &&
                this.containingItems[4].getItem() instanceof IModifier &&
                ((IModifier) this.containingItems[4].getItem())
                        .getType(this.containingItems[4]) == "Unlimiter") {
            slot3 = ((IModifier) this.containingItems[4].getItem())
                    .getEffectiveness(this.containingItems[4]);
        }
        return (100.0 + slot1) * (100.0 + slot2) * (100.0 + slot3) / 1000000.0 *
                10000.0;
    }

    @Override
    public String getType() {
        return this.getInventoryName().replaceAll(" ", "");
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "getVoltage", "getEnergy", "isFull" };
    }

    @Override
    public Object[] callMethod(final IComputerAccess computer, ILuaContext lctx,
            final int method, final Object[] arguments)
            throws LuaException {
        switch (method) {
            case 0: {
                return new Object[] { this.getVoltage() };
            }
            case 1: {
                return new Object[] { this.getJoules() };
            }
            case 2: {
                return new Object[] { this.getJoules() >= this.getMaxJoules() };
            }
            default: {
                throw new LuaException("Function unimplemented");
            }
        }
    }

    @Override
    public void attach(final IComputerAccess computer) {
    }

    @Override
    public void detach(final IComputerAccess computer) {
    }

    @Override
    public boolean canConnect(final ForgeDirection direction) {
        return this.getBlockMetadata() + 2 == direction.ordinal() ||
                this.getBlockMetadata() + 2 == direction.getOpposite().ordinal();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return this == other;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return getAccessibleSlotsFromSide(side)[0] == slot &&
                this.containingItems[side] == null;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        int slotIdx = getAccessibleSlotsFromSide(side)[0];
        return getAccessibleSlotsFromSide(side)[0] == slot &&
                this.containingItems[slotIdx] != null &&
                this.containingItems[slotIdx].stackSize != 0 &&
                this.containingItems[slotIdx].getItem() == item.getItem();
    }
}
