package electricexpansion.common.cables;

import electricexpansion.common.helpers.TileEntityConductorBase;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntitySwitchWire extends TileEntityConductorBase {
    @Override
    public boolean canConnect(final ForgeDirection direction) {
        return this.getWorldObj().isBlockIndirectlyGettingPowered(
                this.xCoord, this.yCoord, this.zCoord);
    }
}
