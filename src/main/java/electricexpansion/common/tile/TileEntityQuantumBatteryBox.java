package electricexpansion.common.tile;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import electricexpansion.api.IWirelessPowerMachine;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.helpers.PacketUpdateQuantumBatteryBoxFrequency;
import electricexpansion.common.misc.DistributionNetworks;
import java.util.EnumSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;

public class TileEntityQuantumBatteryBox extends TileEntityElectricityStorage
    implements IWirelessPowerMachine, IInventory, IPeripheral {
  private ItemStack[] containingItems;
  private int playersUsing;
  private byte frequency;
  private double joulesForDisplay;
  private String owningPlayer;

  public TileEntityQuantumBatteryBox() {
    this.containingItems = new ItemStack[2];
    this.playersUsing = 0;
    this.frequency = 0;
    this.joulesForDisplay = 0.0;
    this.owningPlayer = null;
  }

  @Override
  public void setPlayer(final EntityPlayer player) {
    this.owningPlayer = player.getDisplayName();
  }

  @Override
  public void updateEntity() {
    super.updateEntity();
    if (!this.isDisabled()) {
      final ForgeDirection outputDirection =
          ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
      final TileEntity outputTile = VectorHelper.getTileEntityFromSide(
          this.getWorldObj(), new Vector3(this), outputDirection);
      final TileEntity inputTile = VectorHelper.getTileEntityFromSide(
          this.getWorldObj(), new Vector3(this), outputDirection.getOpposite());
      final IElectricityNetwork inputNetwork =
          ElectricityNetworkHelper.getNetworkFromTileEntity(
              inputTile, outputDirection.getOpposite());
      final IElectricityNetwork outputNetwork =
          ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile,
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

  private double getOutputCap() { return 10000.0; }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound nbt = new NBTTagCompound();

    nbt.setByte("frequency", this.getFrequency());
    nbt.setInteger("disabledTicks", super.disabledTicks);
    nbt.setDouble("joules", this.getJoules());
    if (this.owningPlayer != null)
      nbt.setString("owningPlayer", this.owningPlayer);

    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
                                         this.getBlockMetadata(), nbt);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    NBTTagCompound nbt = pkt.func_148857_g();

    this.frequency = nbt.getByte("frequency");
    super.disabledTicks = nbt.getInteger("disabledTicks");
    this.joulesForDisplay = nbt.getDouble("joules");
    if (nbt.hasKey("owningPlayer"))
      this.owningPlayer = nbt.getString("owningPlayer");
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
  public void readFromNBT(final NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    this.frequency = nbt.getByte("frequency");
    if (nbt.hasKey("owner"))
      this.owningPlayer = nbt.getString("owner");
  }

  @Override
  public void writeToNBT(final NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setShort("frequency", (short)this.frequency);
    if (this.owningPlayer != null)
      nbt.setString("owner", this.owningPlayer);
  }

  @Override
  public double getJoules() {
    return ElectricExpansion.DistributionNetworksInstance.getJoules(
        this.owningPlayer, this.frequency);
  }

  @Override
  public void removeJoules(final double outputWatts) {
    ElectricExpansion.DistributionNetworksInstance.removeJoules(
        this.owningPlayer, this.frequency, outputWatts);
  }

  @Override
  public void setJoules(final double joules) {
    ElectricExpansion.DistributionNetworksInstance.setJoules(
        this.owningPlayer, this.frequency, joules);
  }

  @Override
  public double getMaxJoules() {
    return DistributionNetworks.getMaxJoules();
  }

  public double getJoulesForDisplay(final Object... data) {
    return this.joulesForDisplay;
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
    return "tile.Distribution.name";
  }

  @Override
  public int getInventoryStackLimit() {
    return 1;
  }

  @Override
  public boolean isUseableByPlayer(final EntityPlayer par1EntityPlayer) {
    return this.getWorldObj().getTileEntity(this.xCoord, this.yCoord,
                                            this.zCoord) == this &&
        par1EntityPlayer.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5,
                                       this.zCoord + 0.5) <= 64.0;
  }

  @Override
  public byte getFrequency() {
    return this.frequency;
  }

  @Override
  public void setFrequency(final byte newFrequency) {
    this.frequency = newFrequency;
    if (this.getWorldObj().isRemote) {
      ElectricExpansion.channel.sendToServer(
          new PacketUpdateQuantumBatteryBoxFrequency(new Vector3(this),
                                                     newFrequency));
    }
  }

  public void setFrequency(final int frequency) {
    this.setFrequency((byte)frequency);
  }

  public void setFrequency(final short frequency) {
    this.setFrequency((byte)frequency);
  }

  private int setFrequency(final Object frequency) {
    if (frequency instanceof Double) {
      final Double freq = (Double)frequency;
      this.setFrequency((int)Math.floor(freq));
    }
    return this.frequency;
  }

  public String getOwningPlayer() { return this.owningPlayer; }

  @Override
  public String getType() {
    return this.getInventoryName().replaceAll(" ", "");
  }

  @Override
  public String[] getMethodNames() {
    return new String[] {"getVoltage",   "isFull",       "getJoules",
                         "getFrequency", "setFrequency", "getPlayer"};
  }

  @Override
  public void attach(final IComputerAccess computer) {}

  @Override
  public void detach(final IComputerAccess computer) {}

  @Override
  public Object[] callMethod(final IComputerAccess computer, ILuaContext lctx,
                             final int method, final Object[] arguments)
      throws LuaException {
    if (this.isDisabled()) {
      throw new LuaException("Please wait for the EMP to run out.");
    }
    switch (method) {
    case 0: {
      return new Object[] {this.getVoltage()};
    }
    case 1: {
      return new Object[] {this.getJoules() >= this.getMaxJoules()};
    }
    case 2: {
      return new Object[] {this.getJoules()};
    }
    case 3: {
      return new Object[] {this.getFrequency()};
    }
    case 4: {
      return new Object[] {
          (arguments.length == 1)
              ? Integer.valueOf(this.setFrequency(arguments[0]))
              : "Expected args for this function is 1. You have provided %s."
                    .replace("%s", arguments.length + "")};
    }
    case 5: {
      return new Object[] {this.getOwningPlayer()};
    }
    default: {
      throw new LuaException("Function unimplemented");
    }
    }
  }

  @Override
  public boolean canConnect(final ForgeDirection direction) {
    return direction ==
        ForgeDirection.getOrientation(this.getBlockMetadata() + 2) ||
        direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2)
                         .getOpposite();
  }

  @Override
  protected EnumSet<ForgeDirection> getConsumingSides() {
    return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)
                          .getOpposite());
  }

  @Override
  public boolean hasCustomInventoryName() {
    return false;
  }

  @Override
  public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
    return false;
  }

  @Override
  public boolean equals(IPeripheral other) {
    return this == other;
  }
}
