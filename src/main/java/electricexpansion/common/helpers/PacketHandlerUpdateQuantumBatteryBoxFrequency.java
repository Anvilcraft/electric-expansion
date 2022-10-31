package electricexpansion.common.helpers;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import electricexpansion.common.tile.TileEntityQuantumBatteryBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketHandlerUpdateQuantumBatteryBoxFrequency
        implements IMessageHandler<PacketUpdateQuantumBatteryBoxFrequency, IMessage> {

    @Override
    public IMessage onMessage(PacketUpdateQuantumBatteryBoxFrequency message,
            MessageContext ctx) {
        World world = ctx.getServerHandler().playerEntity.worldObj;
        TileEntity te = message.pos.getTileEntity(world);

        if (te instanceof TileEntityQuantumBatteryBox) {
            ((TileEntityQuantumBatteryBox) te).setFrequency(message.freq);
        }

        return null;
    }
}
