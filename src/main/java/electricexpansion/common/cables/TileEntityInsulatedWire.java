package electricexpansion.common.cables;

import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.helpers.TileEntityConductorBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityInsulatedWire extends TileEntityConductorBase {
    public byte colorByte;

    public TileEntityInsulatedWire() {
        this.colorByte = -1;
    }

    @Override
    public void initiate() {
        super.initiate();
        this.getWorldObj().notifyBlocksOfNeighborChange(
                this.xCoord, this.yCoord, this.zCoord,
                ElectricExpansionItems.blockInsulatedWire);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();
        this.colorByte = nbt.getByte("colorByte");
        this.visuallyConnected[0] = nbt.getBoolean("bottom");
        this.visuallyConnected[1] = nbt.getBoolean("top");
        this.visuallyConnected[2] = nbt.getBoolean("back");
        this.visuallyConnected[3] = nbt.getBoolean("front");
        this.visuallyConnected[4] = nbt.getBoolean("left");
        this.visuallyConnected[5] = nbt.getBoolean("right");
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.colorByte = nbt.getByte("colorByte");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("colorByte", this.colorByte);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("colorByte", this.colorByte);
        nbt.setBoolean("bottom", this.visuallyConnected[0]);
        nbt.setBoolean("top", this.visuallyConnected[1]);
        nbt.setBoolean("back", this.visuallyConnected[2]);
        nbt.setBoolean("front", this.visuallyConnected[3]);
        nbt.setBoolean("left", this.visuallyConnected[4]);
        nbt.setBoolean("right", this.visuallyConnected[5]);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
                this.getBlockMetadata(), nbt);
    }
}
