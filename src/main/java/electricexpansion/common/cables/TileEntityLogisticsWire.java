package electricexpansion.common.cables;

import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.helpers.TileEntityConductorBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.prefab.implement.IRedstoneProvider;

public class TileEntityLogisticsWire
        extends TileEntityConductorBase implements IRedstoneProvider {
    public boolean buttonStatus0;
    public boolean buttonStatus1;
    public boolean buttonStatus2;
    private double networkProduced;
    private byte tick;

    public TileEntityLogisticsWire() {
        this.buttonStatus0 = false;
        this.buttonStatus1 = false;
        this.buttonStatus2 = false;
        this.networkProduced = 0.0;
        this.tick = 0;
    }

    @Override
    public void initiate() {
        this.getWorldObj().notifyBlocksOfNeighborChange(
                this.xCoord, this.yCoord, this.zCoord,
                ElectricExpansionItems.blockLogisticsWire);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.visuallyConnected[0] = nbt.getBoolean("bottom");
        this.visuallyConnected[1] = nbt.getBoolean("top");
        this.visuallyConnected[2] = nbt.getBoolean("back");
        this.visuallyConnected[3] = nbt.getBoolean("front");
        this.visuallyConnected[4] = nbt.getBoolean("left");
        this.visuallyConnected[5] = nbt.getBoolean("right");

        this.buttonStatus0 = nbt.getBoolean("buttonStatus0");
        this.buttonStatus1 = nbt.getBoolean("buttonStatus1");
        this.buttonStatus2 = nbt.getBoolean("buttonStatus2");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("bottom", this.visuallyConnected[0]);
        nbt.setBoolean("top", this.visuallyConnected[1]);
        nbt.setBoolean("back", this.visuallyConnected[2]);
        nbt.setBoolean("front", this.visuallyConnected[3]);
        nbt.setBoolean("left", this.visuallyConnected[4]);
        nbt.setBoolean("right", this.visuallyConnected[5]);

        nbt.setBoolean("buttonStatus0", this.buttonStatus0);
        nbt.setBoolean("buttonStatus1", this.buttonStatus1);
        nbt.setBoolean("buttonStatus2", this.buttonStatus2);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
                this.getBlockMetadata(), nbt);
    }

    public void onButtonPacket(int buttonId, boolean status) {
        switch (buttonId) {
            case 0:
                this.buttonStatus0 = status;
                break;

            case 1:
                this.buttonStatus1 = status;
                break;

            case 2:
                this.buttonStatus2 = status;
                break;
        }
    }

    // TODO: WTF
    // final byte id = dataStream.readByte();
    // if (id == -1) {
    // this.buttonStatus0 = dataStream.readBoolean();
    // }
    // if (id == 0) {
    // this.buttonStatus1 = dataStream.readBoolean();
    // }
    // if (id == 1) {
    // this.buttonStatus2 = dataStream.readBoolean();
    // }
    // if (id != 7 || dataStream.readBoolean()) {}

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.buttonStatus0 = nbt.getBoolean("buttonStatus0");
        this.buttonStatus1 = nbt.getBoolean("buttonStatus1");
        this.buttonStatus2 = nbt.getBoolean("buttonStatus2");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("buttonStatus0", this.buttonStatus0);
        nbt.setBoolean("buttonStatus1", this.buttonStatus1);
        nbt.setBoolean("buttonStatus2", this.buttonStatus2);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.getWorldObj().isRemote) {
            ++this.tick;
            if (this.tick == 20) {
                this.tick = 0;
                if (this.networkProduced == 0.0 &&
                        this.getNetwork().getProduced(new TileEntity[0]).getWatts() != 0.0) {
                    this.getWorldObj().notifyBlocksOfNeighborChange(
                            this.xCoord, this.yCoord, this.zCoord, this.blockType);
                }
                if (this.networkProduced != 0.0 &&
                        this.getNetwork().getProduced(new TileEntity[0]).getWatts() == 0.0) {
                    this.getWorldObj().notifyBlocksOfNeighborChange(
                            this.xCoord, this.yCoord, this.zCoord, this.blockType);
                }
                this.networkProduced = this.getNetwork().getProduced(new TileEntity[0]).getWatts();
            }
        }
    }

    @Override
    public boolean isPoweringTo(final ForgeDirection side) {
        return this.buttonStatus0 &&
                this.getNetwork().getProduced(new TileEntity[0]).getWatts() > 0.0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(final ForgeDirection side) {
        return this.isPoweringTo(side);
    }
}
