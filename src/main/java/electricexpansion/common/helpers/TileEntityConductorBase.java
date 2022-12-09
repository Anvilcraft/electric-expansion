package electricexpansion.common.helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.EnumWireMaterial;
import electricexpansion.api.EnumWireType;
import electricexpansion.api.IAdvancedConductor;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.misc.EENetwork;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.compat.CompatHandler;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.block.ISelfDriven;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public abstract class TileEntityConductorBase
        extends TileEntityAdvanced implements IAdvancedConductor {
    public ItemStack textureItemStack;
    public boolean isIconLocked;
    public EENetwork smartNetwork;
    protected final String channel;
    public boolean[] visuallyConnected;
    public TileEntity[] connectedBlocks;

    @Override
    public IElectricityNetwork getNetwork() {
        if (this.smartNetwork == null) {
            this.setNetwork(new EENetwork(new IConductor[] { this }));
        }
        return this.smartNetwork;
    }

    @Override
    public void setNetwork(final IElectricityNetwork network) {
        if (network instanceof EENetwork) {
            this.smartNetwork = (EENetwork) network;
        } else {
            this.smartNetwork = new EENetwork(network);
        }
    }

    public TileEntityConductorBase() {
        this.isIconLocked = false;
        this.visuallyConnected = new boolean[] { false, false, false, false, false, false };
        this.connectedBlocks = new TileEntity[] { null, null, null, null, null, null };
        this.channel = "ElecEx";
    }

    @Override
    public void initiate() {
        super.initiate();
        this.updateAdjacentConnections();
        this.getWorldObj().markBlockRangeForRenderUpdate(this.xCoord, this.yCoord,
                this.zCoord, this.xCoord,
                this.yCoord, this.zCoord);
    }

    @Override
    public double getResistance() {
        return this
                .getWireMaterial(this.getWorldObj().getBlockMetadata(
                        this.xCoord, this.yCoord, this.zCoord)).resistance;
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("isIconLocked", this.isIconLocked);
        if (this.textureItemStack != null) {
            this.textureItemStack.writeToNBT(tag);
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        try {
            this.textureItemStack = ItemStack.loadItemStackFromNBT(tag);
        } catch (final Exception e) {
            this.textureItemStack = null;
        }
        try {
            this.isIconLocked = tag.getBoolean("isIconLocked");
        } catch (final Exception e) {
            this.isIconLocked = false;
        }
    }

    @Override
    public double getCurrentCapcity() {
        final int meta = this.getWorldObj().getBlockMetadata(
                this.xCoord, this.yCoord, this.zCoord);
        if (meta < EnumWireMaterial.values().length - 1) {
            return EnumWireMaterial.values()[meta].maxAmps;
        }
        return EnumWireMaterial.UNKNOWN.maxAmps;
    }

    @Override
    public EnumWireType getWireType(final int metadata) {
        return EnumWireType.values()[metadata];
    }

    @Override
    public EnumWireMaterial getWireMaterial(final int metadata) {
        if (metadata < EnumWireMaterial.values().length - 1) {
            return EnumWireMaterial.values()[metadata];
        }
        return EnumWireMaterial.UNKNOWN;
    }

    public void updateConnection(final TileEntity tileEntity,
            final ForgeDirection side) {
        if (!this.getWorldObj().isRemote) {
            if (tileEntity instanceof TileEntityInsulatedWire &&
                    this instanceof TileEntityInsulatedWire) {
                final TileEntityInsulatedWire tileEntityIns = (TileEntityInsulatedWire) tileEntity;
                if ((tileEntityIns.colorByte == ((TileEntityInsulatedWire) this).colorByte ||
                        ((TileEntityInsulatedWire) this).colorByte == -1 ||
                        tileEntityIns.colorByte == -1) &&
                        tileEntityIns.getWireMaterial(tileEntity.getBlockMetadata()) == this
                                .getWireMaterial(this.getBlockMetadata())
                        &&
                        ((IConnector) tileEntity).canConnect(side.getOpposite())) {
                    this.connectedBlocks[side.ordinal()] = tileEntity;
                    this.visuallyConnected[side.ordinal()] = true;
                    if (tileEntity.getClass() == this.getClass() &&
                            tileEntity instanceof INetworkProvider) {
                        this.getNetwork().mergeConnection(
                                ((INetworkProvider) tileEntity).getNetwork());
                    }
                    return;
                }
            } else if (tileEntity instanceof IAdvancedConductor) {
                final IAdvancedConductor tileEntityWire = (IAdvancedConductor) tileEntity;
                if (tileEntityWire.getWireMaterial(tileEntity.getBlockMetadata()) == this
                        .getWireMaterial(this.getBlockMetadata()) &&
                        ((IConnector) tileEntity).canConnect(side.getOpposite())) {
                    this.connectedBlocks[side.ordinal()] = tileEntity;
                    this.visuallyConnected[side.ordinal()] = true;
                    if (tileEntity.getClass() == this.getClass() &&
                            tileEntity instanceof INetworkProvider) {
                        this.getNetwork().mergeConnection(
                                ((INetworkProvider) tileEntity).getNetwork());
                    }
                    return;
                }
            } else if (ElectricityNetworkHelper.canConnect(tileEntity, side.getOpposite(), this)) {
                this.connectedBlocks[side.ordinal()] = tileEntity;
                this.visuallyConnected[side.ordinal()] = true;
                if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider) {
                    this.getNetwork().mergeConnection(
                            ((INetworkProvider) tileEntity).getNetwork());
                } else if (!(tileEntity instanceof ISelfDriven)) {
                    CompatHandler.registerTile(tileEntity);
                }

                return;
            }

            if(this.connectedBlocks[side.ordinal()] != null) {
                this.getNetwork().stopProducing(this.connectedBlocks[side.ordinal()]);
                this.getNetwork().stopRequesting(this.connectedBlocks[side.ordinal()]);
             }
    
             this.connectedBlocks[side.ordinal()] = null;
             this.visuallyConnected[side.ordinal()] = false;
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.visuallyConnected[0] = nbt.getBoolean("bottom");
        this.visuallyConnected[1] = nbt.getBoolean("top");
        this.visuallyConnected[2] = nbt.getBoolean("back");
        this.visuallyConnected[3] = nbt.getBoolean("front");
        this.visuallyConnected[4] = nbt.getBoolean("left");
        this.visuallyConnected[5] = nbt.getBoolean("right");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("bottom", this.visuallyConnected[0]);
        nbt.setBoolean("top", this.visuallyConnected[1]);
        nbt.setBoolean("back", this.visuallyConnected[2]);
        nbt.setBoolean("front", this.visuallyConnected[3]);
        nbt.setBoolean("left", this.visuallyConnected[4]);
        nbt.setBoolean("right", this.visuallyConnected[5]);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
                this.getBlockMetadata(), nbt);
    }

    @Override
    public void invalidate() {
        if (!this.getWorldObj().isRemote) {
            this.getNetwork().splitNetwork(this);
        }
        super.invalidate();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.getWorldObj().isRemote && super.ticks % 300L == 0L) {
            this.updateAdjacentConnections();
        }
    }

    @Override
    public boolean canConnect(final ForgeDirection direction) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(
                (double) this.xCoord, (double) this.yCoord, (double) this.zCoord,
                (double) (this.xCoord + 1), (double) (this.yCoord + 1),
                (double) (this.zCoord + 1));
    }

    @Override
    public TileEntity[] getAdjacentConnections() {
        return this.connectedBlocks;
    }

    public void updateAdjacentConnections() {
        if (this.getWorldObj() != null && !this.worldObj.isRemote) {
            final boolean[] previousConnections = this.visuallyConnected.clone();
            for(byte i = 0; i < 6; ++i) {
                this.updateConnection(VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), ForgeDirection.getOrientation(i)), ForgeDirection.getOrientation(i));
             }
            if (!Arrays.equals(previousConnections, this.visuallyConnected)) {
                this.getWorldObj().markBlockForUpdate(this.xCoord, this.yCoord,
                        this.zCoord);
            }
        }
    }
}
