package electricexpansion.common.helpers;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import universalelectricity.core.vector.Vector3;

public class PacketUpdateQuantumBatteryBoxFrequency implements IMessage {
    Vector3 pos;
    byte freq;

    public PacketUpdateQuantumBatteryBoxFrequency() {
        this(null, (byte) 0);
    }

    public PacketUpdateQuantumBatteryBoxFrequency(Vector3 pos, byte freq) {
        this.pos = pos;
        this.freq = freq;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = new Vector3(buf.readInt(), buf.readInt(), buf.readInt());
        this.freq = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pos.intX());
        buf.writeInt(this.pos.intY());
        buf.writeInt(this.pos.intZ());
        buf.writeByte(this.freq);
    }
}
