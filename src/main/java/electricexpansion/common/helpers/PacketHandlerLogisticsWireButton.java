package electricexpansion.common.helpers;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import electricexpansion.common.cables.TileEntityLogisticsWire;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketHandlerLogisticsWireButton
        implements IMessageHandler<PacketLogisticsWireButton, IMessage> {

    @Override
    public IMessage onMessage(PacketLogisticsWireButton message,
            MessageContext ctx) {
        World world = ctx.getServerHandler().playerEntity.worldObj;
        TileEntity te = message.pos.getTileEntity(world);

        if (te instanceof TileEntityLogisticsWire) {
            ((TileEntityLogisticsWire) te)
                    .onButtonPacket(message.buttonId, message.status);
        }

        return null;
    }
}
