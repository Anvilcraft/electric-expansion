package electricexpansion.common.cables;

import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityRawWire extends TileEntityConductorBase {
    // TODO: WTF
    @Override
    public double getResistance() {
        return super.getResistance() * 2.0;
    }
}
