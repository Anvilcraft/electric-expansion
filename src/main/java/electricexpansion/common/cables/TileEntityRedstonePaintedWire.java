package electricexpansion.common.cables;

import electricexpansion.api.IRedstoneNetAccessor;
import electricexpansion.common.helpers.TileEntityConductorBase;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRedstonePaintedWire
        extends TileEntityConductorBase implements IRedstoneNetAccessor {
    private boolean isRegistered;

    public TileEntityRedstonePaintedWire() {
        this.isRegistered = false;
    }

    @Override
    public void initiate() {
        super.initiate();
        if (super.smartNetwork != null) {
            super.smartNetwork.addRsInterfacer(this);
            this.isRegistered = true;
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.isRegistered && super.smartNetwork != null) {
            super.smartNetwork.addRsInterfacer(this);
            this.isRegistered = true;
        }
    }

    public boolean canUpdate() {
        return true;
    }

    @Override
    public int getRsSignalFromBlock() {
        int i = 0;
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            i = Math.max(
                    i, this.getWorldObj().getBlockPowerInput(this.xCoord + side.offsetX,
                            this.yCoord + side.offsetY,
                            this.zCoord + side.offsetZ));
        }
        return i;
    }
}
