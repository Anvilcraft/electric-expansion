package electricexpansion.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.tile.TileEntityElectrical;

public class TileEntityMultimeter
    extends TileEntityElectrical implements IRotatable {
  public ElectricityPack electricityReading;
  private ElectricityPack lastReading;

  public TileEntityMultimeter() {
    this.electricityReading = new ElectricityPack();
    this.lastReading = new ElectricityPack();
  }

  @Override
  public void updateEntity() {
    super.updateEntity();
    if (super.ticks % 20L == 0L) {
      this.lastReading = this.electricityReading;
      if (!this.getWorldObj().isRemote) {
        if (!this.isDisabled()) {
          final ForgeDirection inputDirection =
              ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
          final TileEntity inputTile = VectorHelper.getTileEntityFromSide(
              this.getWorldObj(), new Vector3(this), inputDirection);
          if (inputTile != null) {
            if (inputTile instanceof IConductor) {
              this.electricityReading = ((IConductor)inputTile)
                                            .getNetwork()
                                            .getProduced(new TileEntity[0]);
              final ElectricityPack electricityReading =
                  this.electricityReading;
              electricityReading.amperes *= 20.0;
            } else {
              this.electricityReading = new ElectricityPack();
            }
          } else {
            this.electricityReading = new ElectricityPack();
          }
        }
        if (this.electricityReading.amperes != this.lastReading.amperes) {
          this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord,
                                           this.zCoord);
        }
      }
    }
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setDouble("amperes", this.electricityReading.amperes);
    nbt.setDouble("voltage", this.electricityReading.voltage);

    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
                                         this.getBlockMetadata(), nbt);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    NBTTagCompound nbt = pkt.func_148857_g();

    this.electricityReading.amperes = nbt.getDouble("amperes");
    this.electricityReading.voltage = nbt.getDouble("voltage");
  }

  @Override
  public boolean canConnect(final ForgeDirection direction) {
    return direction.getOpposite() ==
        ForgeDirection.getOrientation(this.getBlockMetadata());
  }

  @Override
  public void setDirection(final World world, final int x, final int y,
                           final int z, final ForgeDirection facingDirection) {
    this.getWorldObj().setBlock(this.xCoord, this.yCoord, this.zCoord,
                                this.getBlockType(), facingDirection.ordinal(),
                                2);
  }

  @Override
  public ForgeDirection getDirection(final IBlockAccess world, final int x,
                                     final int y, final int z) {
    return ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
  }
}
