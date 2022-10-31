package electricexpansion.common.tile;

import electricexpansion.api.IItemFuse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityElectrical;

public class TileEntityFuseBox
        extends TileEntityElectrical implements IInventory {
    public ItemStack[] inventory;

    public TileEntityFuseBox() {
        this.inventory = new ItemStack[1];
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.getWorldObj().isRemote) {
            if (this.hasFuse()) {
                final ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2)
                        .getOpposite();
                final TileEntity inputTile = VectorHelper.getTileEntityFromSide(
                        this.getWorldObj(), new Vector3(this), inputDirection);
                final ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
                final TileEntity outputTile = VectorHelper.getTileEntityFromSide(
                        this.getWorldObj(), new Vector3(this), outputDirection);
                final IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(
                        inputTile, outputDirection.getOpposite());
                final IElectricityNetwork outputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile,
                        outputDirection);
                if (outputNetwork != null && inputNetwork != null &&
                        outputNetwork != inputNetwork) {
                    final ElectricityPack request = outputNetwork.getRequest(new TileEntity[0]);
                    inputNetwork.startRequesting(this, request);
                    final ElectricityPack recieved = inputNetwork.consumeElectricity(this);
                    outputNetwork.startProducing(this, recieved);
                    if (recieved.amperes > ((IItemFuse) this.inventory[0].getItem())
                            .getMaxVolts(this.inventory[0])) {
                        ((IItemFuse) this.inventory[0].getItem())
                                .onFuseTrip(this.inventory[0]);
                    }
                } else if (outputNetwork != null && inputNetwork == null) {
                    outputNetwork.stopProducing(this);
                } else if (outputNetwork == null && inputNetwork != null) {
                    inputNetwork.stopRequesting(this);
                }
            }
            if (!this.getWorldObj().isRemote) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            final NBTTagCompound var4 = (NBTTagCompound) var2.getCompoundTagAt(var3);
            final byte var5 = var4.getByte("Slot");
            if (var5 >= 0 && var5 < this.inventory.length) {
                this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        final NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.inventory.length; ++var3) {
            if (this.inventory[var3] != null) {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.inventory[var3].writeToNBT(var4);
                var2.appendTag((NBTBase) var4);
            }
        }
        par1NBTTagCompound.setTag("Items", (NBTBase) var2);
    }

    public boolean hasFuse() {
        return this.inventory[0] != null &&
                this.inventory[0].getItem() instanceof IItemFuse &&
                ((IItemFuse) this.inventory[0].getItem()).isValidFuse(this.inventory[0]);
    }

    @Override
    public String getInventoryName() {
        return "tile.FuseBox.name";
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(final int var1) {
        return this.inventory[var1];
    }

    @Override
    public ItemStack decrStackSize(final int var1, final int var2) {
        if (var1 < this.inventory.length &&
                this.inventory[var1].stackSize >= var2) {
            final ItemStack copy;
            final ItemStack toReturn = copy = this.inventory[var1].copy();
            copy.stackSize -= var2;
            if (this.inventory[var1].stackSize == 0) {
                this.inventory = null;
            }
            return toReturn;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int var1) {
        return this.inventory[var1];
    }

    @Override
    public void setInventorySlotContents(final int var1, final ItemStack var2) {
        if (var1 < this.inventory.length) {
            this.inventory[var1] = var2;
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer par1EntityPlayer) {
        return this.getWorldObj().getTileEntity(this.xCoord, this.yCoord,
                this.zCoord) == this &&
                par1EntityPlayer.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5,
                        this.zCoord + 0.5) <= 64.0;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean canConnect(final ForgeDirection direction) {
        final int meta = this.getWorldObj().getBlockMetadata(
                this.xCoord, this.yCoord, this.zCoord);
        return direction.ordinal() == meta + 2 ||
                direction.getOpposite().ordinal() == meta + 2;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return false;
    }
}
