package electricexpansion.common.tile;

import electricexpansion.api.IRedstoneNetAccessor;
import electricexpansion.common.misc.EENetwork;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.prefab.tile.TileEntityElectrical;

public class TileEntityRedstoneNetworkCore
        extends TileEntityElectrical implements INetworkProvider {
    private EENetwork network;

    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        if (super.ticks % 5L == 0L) {
            if (this.network != null) {
                final int netRs = this.network.rsLevel;
                for (final IRedstoneNetAccessor rsCable : this.network.getRedstoneInterfacers()) {
                    final int worldRs = rsCable.getRsSignalFromBlock();
                    this.network.rsLevel = (byte) Math.max(netRs, worldRs);
                }
            } else {
                final ForgeDirection facing = ForgeDirection.getOrientation(this.blockMetadata);
                if (this.getWorldObj().getTileEntity(this.xCoord + facing.offsetX,
                        this.yCoord + facing.offsetY,
                        this.zCoord + facing.offsetZ) instanceof INetworkProvider &&
                        ((INetworkProvider) this.worldObj.getTileEntity(
                                this.xCoord + facing.offsetX, this.yCoord + facing.offsetY,
                                this.zCoord + facing.offsetZ))
                                .getNetwork() != null) {
                    this.setNetwork(
                            ((INetworkProvider) this.getWorldObj().getTileEntity(
                                    this.xCoord + facing.offsetX, this.yCoord + facing.offsetY,
                                    this.zCoord + facing.offsetZ))
                                    .getNetwork());
                }
            }
        }
        if (super.ticks % 300L == 0L && this.network != null) {
            this.network.cleanUpConductors();
        }
    }

    @Override
    public IElectricityNetwork getNetwork() {
        return this.network;
    }

    @Override
    public void setNetwork(final IElectricityNetwork network) {
        if (network instanceof EENetwork &&
                ((EENetwork) network).coreProcessor == null) {
            this.network = (EENetwork) network;
            ((EENetwork) network).coreProcessor = this;
            this.network.cleanUpConductors();
        } else if (network instanceof ElectricityNetwork) {
            this.network = new EENetwork(network);
            this.network.coreProcessor = this;
        }
    }

    @Override
    public boolean canConnect(final ForgeDirection direction) {
        return direction.ordinal() == this.getBlockMetadata();
    }
}
