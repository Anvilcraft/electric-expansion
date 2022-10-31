package electricexpansion.common.helpers;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import universalelectricity.core.vector.Vector3;

public class PacketLogisticsWireButton implements IMessage {
    Vector3 pos;
    int buttonId;
    boolean status;

    public PacketLogisticsWireButton(Vector3 pos, int buttonId, boolean status) {
        this.pos = pos;
        this.buttonId = buttonId;
        this.status = status;
    }

    public PacketLogisticsWireButton() {
        this(null, 0, false);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = new Vector3(buf.readInt(), buf.readInt(), buf.readInt());
        this.buttonId = buf.readInt();
        this.status = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pos.intX());
        buf.writeInt(this.pos.intY());
        buf.writeInt(this.pos.intZ());
        buf.writeInt(this.buttonId);
        buf.writeBoolean(this.status);
    }
}
