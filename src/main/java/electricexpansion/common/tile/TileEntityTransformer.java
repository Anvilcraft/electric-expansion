package electricexpansion.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.tile.TileEntityElectrical;

public class TileEntityTransformer
        extends TileEntityElectrical implements IRotatable {
    public boolean stepUp;
    public int type;

    public TileEntityTransformer() {
        this.stepUp = false;
    }

    @Override
    public void initiate() {
        final int meta = this.getWorldObj().getBlockMetadata(
                this.xCoord, this.yCoord, this.zCoord);
        this.type = meta - (meta & 0x3);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.getWorldObj().isRemote) {
            final ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - this.type + 2)
                    .getOpposite();
            final TileEntity inputTile = VectorHelper.getTileEntityFromSide(
                    this.getWorldObj(), new Vector3(this), inputDirection);
            final ForgeDirection outputDirection = ForgeDirection.getOrientation(
                    this.getBlockMetadata() - this.type + 2);
            final TileEntity outputTile = VectorHelper.getTileEntityFromSide(
                    this.getWorldObj(), new Vector3(this), outputDirection);
            final IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(
                    inputTile, outputDirection.getOpposite());
            final IElectricityNetwork outputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile,
                    outputDirection);
            if (outputNetwork != null && inputNetwork == null) {
                outputNetwork.stopProducing(this);
            } else if (outputNetwork == null && inputNetwork != null) {
                inputNetwork.stopRequesting(this);
            }
            if (outputNetwork != null && inputNetwork != null) {
                if (outputNetwork != inputNetwork) {
                    if (outputNetwork.getRequest(new TileEntity[0]).getWatts() > 0.0) {
                        inputNetwork.startRequesting(
                                this, outputNetwork.getRequest(new TileEntity[0]));
                        final ElectricityPack actualEnergy = inputNetwork.consumeElectricity(this);
                        if (actualEnergy.getWatts() > 0.0) {
                            double typeChange = 0.0;
                            if (this.type == 0) {
                                typeChange = 2.0;
                            } else if (this.type == 4) {
                                typeChange = 4.0;
                            } else if (this.type == 8) {
                                typeChange = 8.0;
                            }
                            double newVoltage = actualEnergy.voltage * typeChange;
                            if (!this.stepUp) {
                                newVoltage = actualEnergy.voltage / typeChange;
                            }
                            outputNetwork.startProducing(
                                    this, actualEnergy.getWatts() / newVoltage, newVoltage);
                        } else {
                            outputNetwork.stopProducing(this);
                        }
                    } else {
                        inputNetwork.stopRequesting(this);
                        outputNetwork.stopProducing(this);
                    }
                } else {
                    inputNetwork.stopRequesting(this);
                    outputNetwork.stopProducing(this);
                }
            }
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("stepUp", this.stepUp);
        nbt.setInteger("type", this.type);

        System.out.println("get description packet; stepUp: " + this.stepUp);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
                this.getBlockMetadata(), nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.stepUp = nbt.getBoolean("stepUp");
        this.type = nbt.getInteger("type");
    }

    @Override
    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        this.stepUp = par1NBTTagCompound.getBoolean("stepUp");
        this.type = par1NBTTagCompound.getInteger("type");
    }

    @Override
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("stepUp", this.stepUp);
        par1NBTTagCompound.setInteger("type", this.type);
    }

    @Override
    public boolean canConnect(final ForgeDirection direction) {
        final int meta = this.getWorldObj().getBlockMetadata(
                this.xCoord, this.yCoord, this.zCoord);
        return direction.ordinal() - 2 + this.type == meta ||
                direction.getOpposite().ordinal() - 2 + this.type == meta;
    }

    @Override
    public void setDirection(final World world, final int x, final int y,
            final int z, final ForgeDirection facingDirection) {
        this.getWorldObj().setBlock(this.xCoord, this.yCoord, this.zCoord,
                this.getBlockType(),
                facingDirection.ordinal() - 2 + this.type, 0);
    }

    @Override
    public ForgeDirection getDirection(final IBlockAccess world, final int x,
            final int y, final int z) {
        return ForgeDirection.getOrientation(this.getBlockMetadata() - this.type);
    }
}
