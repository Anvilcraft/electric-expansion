package electricexpansion.common.helpers;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import electricexpansion.api.IBatteryBoxPreipheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BatteryBoxPeripheralProvider implements IPeripheralProvider {

    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IBatteryBoxPreipheral) {
            return ((IPeripheral)tile);
        }
        return null;
    }
    
}
