package electricexpansion.common.tile;

import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.misc.InsulationRecipes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
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
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

public class TileEntityInsulatingMachine extends TileEntityElectricityRunnable
        implements ISidedInventory, IElectricityStorage {
    public static final double WATTS_PER_TICK = 500.0;
    public static final double TRANSFER_LIMIT = 1250.0;
    private int processTicks;
    private double joulesStored;
    private int recipeTicks;
    public static double maxJoules;
    private ItemStack[] inventory;
    private int playersUsing;
    public int orientation;
    private Item baseItem;
    private int baseMeta;

    public TileEntityInsulatingMachine() {
        this.processTicks = 0;
        this.joulesStored = 0.0;
        this.recipeTicks = 0;
        this.inventory = new ItemStack[3];
        this.playersUsing = 0;
        this.baseItem = Item.getItemFromBlock(Blocks.air);
        this.baseMeta = 0;
    }

    @Override
    public void updateEntity() {
        // TODO: WTF
        // super.updateEntity();
        if (!this.getWorldObj().isRemote) {
            final ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
            final TileEntity inputTile = VectorHelper.getTileEntityFromSide(
                    this.getWorldObj(), new Vector3(this), inputDirection);
            final IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(
                    inputTile, inputDirection.getOpposite());
            if (inputNetwork != null) {
                if (this.joulesStored < TileEntityInsulatingMachine.maxJoules) {
                    inputNetwork.startRequesting(
                            this,
                            Math.min(this.getMaxJoules() - this.getJoules(), 1250.0) /
                                    this.getVoltage(),
                            this.getVoltage());
                    final ElectricityPack electricityPack = inputNetwork.consumeElectricity(this);
                    this.setJoules(this.joulesStored + electricityPack.getWatts());
                    if (UniversalElectricity.isVoltageSensitive &&
                            electricityPack.voltage > this.getVoltage()) {
                        this.getWorldObj().createExplosion(
                                (Entity) null, (double) this.xCoord, (double) this.yCoord,
                                (double) this.zCoord, 2.0f, true);
                    }
                } else {
                    inputNetwork.stopRequesting(this);
                }
            }
        }
        if (this.inventory[0] != null && this.joulesStored < this.getMaxJoules()) {
            if (this.inventory[0].getItem() instanceof IItemElectric) {
                final IItemElectric electricItem = (IItemElectric) this.inventory[0].getItem();
                if (electricItem.getProvideRequest(this.inventory[0]).getWatts() > 0.0) {
                    final double joulesReceived = electricItem
                            .onProvide(ElectricityPack.getFromWatts(
                                    Math.max(electricItem.getMaxJoules(
                                            this.inventory[0]) *
                                            0.005,
                                            1250.0),
                                    electricItem.getVoltage(this.inventory[0])),
                                    this.inventory[0])
                            .getWatts();
                    this.setJoules(this.joulesStored + joulesReceived);
                }
            }
        }
        if (this.joulesStored >= 500.0 - 50.0 && !this.isDisabled()) {
            if (this.inventory[1] != null && this.canProcess() &&
                    (this.processTicks == 0 ||
                            this.baseItem != this.inventory[1].getItem() ||
                            this.baseMeta != this.inventory[1].getItemDamage())) {
                this.baseItem = this.inventory[1].getItem();
                this.baseMeta = this.inventory[1].getItemDamage();
                this.processTicks = this.getProcessingTime();
                this.recipeTicks = this.getProcessingTime();
            }
            if (this.canProcess() && this.processTicks > 0) {
                --this.processTicks;
                if (this.processTicks < 1) {
                    this.processItem();
                    this.processTicks = 0;
                }
                this.getClass();
                this.joulesStored -= 500.0;
            } else {
                this.processTicks = 0;
            }
        }
        if (!this.getWorldObj().isRemote && super.ticks % 3L == 0L &&
                this.playersUsing > 0) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        this.joulesStored = Math.min(this.joulesStored, this.getMaxJoules());
        this.joulesStored = Math.max(this.joulesStored, 0.0);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger("processTicks", this.processTicks);
        nbt.setInteger("disabledTicks", super.disabledTicks);
        nbt.setDouble("joulesStored", this.joulesStored);
        nbt.setInteger("recipeTicks", this.recipeTicks);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
                this.getBlockMetadata(), nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.processTicks = nbt.getInteger("processTicks");
        super.disabledTicks = nbt.getInteger("disabledTicks");
        this.joulesStored = nbt.getDouble("joulesStored");
        this.recipeTicks = nbt.getInteger("recipeTicks");
    }

    @Override
    public void openInventory() {
        if (!this.getWorldObj().isRemote) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        ++this.playersUsing;
    }

    @Override
    public void closeInventory() {
        --this.playersUsing;
    }

    public boolean canProcess() {
        boolean canWork = false;
        final ItemStack inputSlot = this.inventory[1];
        final int outputSlot = (this.inventory[2] != null) ? this.inventory[2].stackSize : 0;
        if (inputSlot != null &&
                InsulationRecipes.INSTANCE.getProcessResult(inputSlot) > 0 &&
                InsulationRecipes.INSTANCE.getProcessResult(inputSlot) + outputSlot <= 64) {
            canWork = true;
        }
        return canWork;
    }

    public void processItem() {
        if (this.canProcess()) {
            final int result = InsulationRecipes.INSTANCE.getProcessResult(this.inventory[1]);
            if (this.inventory[2] == null) {
                this.inventory[2] = new ItemStack(ElectricExpansionItems.itemParts, result, 6);
            } else if (this.inventory[2].stackSize + result <= 64) {
                final ItemStack itemStack = this.inventory[2];
                itemStack.stackSize += result;
            }
            final ItemStack itemStack2 = this.inventory[1];
            itemStack2.stackSize -= InsulationRecipes.INSTANCE.getInputQTY(this.inventory[1]);
            if (this.inventory[1].stackSize <= 0) {
                this.inventory[1] = null;
            }
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        this.processTicks = par1NBTTagCompound.getInteger("processTicks");
        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        try {
            this.joulesStored = par1NBTTagCompound.getDouble("joulesStored");
        } catch (final Exception ex) {
        }
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            final NBTTagCompound var4 = (NBTTagCompound) var2.getCompoundTagAt(var3);
            final byte var5 = var4.getByte("Slot");
            if (var5 >= 0 && var5 < this.inventory.length) {
                this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("processTicks", this.processTicks);
        final NBTTagList var2 = new NBTTagList();
        par1NBTTagCompound.setDouble("joulesStored", this.getJoules());
        for (int var3 = 0; var3 < this.inventory.length; ++var3) {
            if (this.inventory[var3] != null) {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.inventory[var3].writeToNBT(var4);
                var2.appendTag((NBTBase) var4);
            }
        }
        par1NBTTagCompound.setTag("Items", (NBTBase) var2);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        ForgeDirection side = ForgeDirection.getOrientation(p_94128_1_);

        if (side == ForgeDirection.DOWN || side == ForgeDirection.UP) {
            return new int[] { side.ordinal() };
        }
        return new int[] { 2 };
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(final int par1) {
        return this.inventory[par1];
    }

    @Override
    public ItemStack decrStackSize(final int par1, final int par2) {
        if (this.inventory[par1] == null) {
            return null;
        }
        if (this.inventory[par1].stackSize <= par2) {
            final ItemStack var3 = this.inventory[par1];
            this.inventory[par1] = null;
            return var3;
        }
        final ItemStack var3 = this.inventory[par1].splitStack(par2);
        if (this.inventory[par1].stackSize == 0) {
            this.inventory[par1] = null;
        }
        return var3;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int par1) {
        if (this.inventory[par1] != null) {
            final ItemStack var2 = this.inventory[par1];
            this.inventory[par1] = null;
            return var2;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(final int par1,
            final ItemStack par2ItemStack) {
        this.inventory[par1] = par2ItemStack;
        if (par2ItemStack != null &&
                par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return StatCollector.translateToLocal("tile.insulator.name");
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer par1EntityPlayer) {
        return this.getWorldObj().getTileEntity(this.xCoord, this.yCoord,
                this.zCoord) == this;
    }

    @Override
    public double getVoltage() {
        return 120.0;
    }

    public int getProcessingTime() {
        if (this.inventory[1] != null &&
                InsulationRecipes.INSTANCE.getProcessResult(this.inventory[1]) != 0) {
            return InsulationRecipes.INSTANCE.getProcessTicks(this.inventory[1]);
        }
        return -1;
    }

    public int getProcessTimeLeft() {
        return this.processTicks;
    }

    @Override
    public boolean canConnect(final ForgeDirection direction) {
        return direction.ordinal() == this.getBlockMetadata() + 2;
    }

    @Override
    public double getJoules() {
        return this.joulesStored;
    }

    @Override
    public void setJoules(final double joules) {
        this.joulesStored = joules;
    }

    @Override
    public double getMaxJoules() {
        return TileEntityInsulatingMachine.maxJoules;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return i == 1 &&
                InsulationRecipes.INSTANCE.getProcessResult(itemstack) >= 1;
    }

    static {
        TileEntityInsulatingMachine.maxJoules = 150000.0;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        if (!(getAccessibleSlotsFromSide(side)[0] == slot &&
                this.inventory[side] == null))
            return false;

        switch (slot) {
            case 0:
                return item.getItem() instanceof IItemElectric;

            case 1:
                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        int slotIdx = getAccessibleSlotsFromSide(side)[0];
        return getAccessibleSlotsFromSide(side)[0] == slot &&
                this.inventory[slotIdx] != null &&
                this.inventory[slotIdx].stackSize != 0 &&
                this.inventory[slotIdx].getItem() == item.getItem();
    }
}
